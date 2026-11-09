#!/usr/bin/env python3
"""Summarise JBMC's JSON output (--json-ui --trace) for the ./bmc script.

Prints the failed properties, the size of the SAT problem, the verdict, and one
short counterexample per failed property. Only steps in the given source files
are shown: the inputs of the entry method, nondet choices, calls to user
methods, and writes to locals, fields and array cells. What JBMC adds to model
the JVM (class initialisation, java.lang, temporaries, zero-initialisation) is
hidden.

usage: jbmc_report.py jbmc-output.json Source1.java [Source2.java ...]
Set BMC_FULL=1 to also list the properties that passed.
"""
import os
import json
import re
import sys

IDENT = re.compile(r"^[A-Za-z_][A-Za-z0-9_]*$")
FIELD = re.compile(r"^(dynamic_object(?:\$\d+)?)\.([A-Za-z_][A-Za-z0-9_]*)$")
CELL = re.compile(r"^(dynamic_\w*(?:\$\d+)?)\[(\d+)L?\]$")
TEMP = re.compile(r"^(return_tmp|new_tmp|malloc_site|anonlocal|stack_tmp|tmp_|this$|arg\d|\$|nondet)")


def short(identifier):
    # java::SafeLock$EnterPin.pin:()I -> SafeLock.EnterPin.pin
    return identifier.replace("java::", "").split(":")[0].replace("$", ".")


def class_of(function):
    # java::SafeLock$EnterPin.<init>:(I)V -> EnterPin
    return function.replace("java::", "").split(":")[0].rsplit(".", 1)[0].split("$")[-1]


def pointee(value):
    return re.sub(r"^\(.*?\)", "", str(value.get("data"))).lstrip("&")


def hidden_call(callee):
    # class initialisation, library code and zero-argument getters
    # (record accessors, state()) only add noise
    return ("<clinit" in callee or callee.startswith("java::java.")
            or callee.startswith("java::org.cprover.")
            or (":()" in callee and not callee.endswith(")V")))


class Trace:
    def __init__(self, sources):
        self.sources = sources            # file name -> path
        self.names = {}                   # object -> readable name
        self.classes = {}                 # object -> simple class name
        self.counts = {}
        self.constructed = set()
        self.arr_len = {}                 # array object -> length
        self.arr_data = {}                # array object -> data block
        self.cells = {}                   # data block -> {index: text}
        self.alias = {}                   # array object -> last local pointing to it
        self.out = []
        self.depth = 0
        self.muted = 0                    # > 0 inside a call we do not show
        self.stack = []
        self.nondet = None
        self.entered = False

    def user(self, loc):
        f = (loc or {}).get("file", "")
        return f.split("/")[-1] in self.sources

    def text(self, v):
        if not v:
            return "?"
        if v.get("name") == "pointer":
            target = pointee(v)
            if target in self.arr_len:
                n = int(self.arr_len[target])
                cells = self.cells.get(self.arr_data.get(target), {})
                return "{" + ", ".join(cells.get(i, "?") for i in range(n)) + "}"
            if v.get("data") == "null":
                return "null"
            return self.names.get(target, "object")
        return str(v.get("data"))

    def emit(self, line, msg):
        self.out.append(f"{'  ' * self.depth}line {line}: {msg}")

    def source(self, file, line):
        path = self.sources.get(file.split("/")[-1])
        try:
            return open(path).read().splitlines()[int(line) - 1].strip()
        except (TypeError, OSError, ValueError, IndexError):
            return None

    def step(self, s):
        kind = s.get("stepType")
        loc = s.get("sourceLocation") or {}
        func = loc.get("function", "") or ""
        line = loc.get("line", "?")

        if kind == "function-call":
            callee = s.get("function", {}).get("identifier", "")
            if callee.startswith("java::org.cprover.CProver.nondet"):
                self.nondet = (short(callee).split(".")[-1], line)
            show = (not self.muted and not hidden_call(callee) and "<clinit" not in func
                    and self.user(s.get("function", {}).get("sourceLocation")))
            self.stack.append(show)
            if not show:
                self.muted += 1
            elif not self.entered:
                self.entered = True
                self.out.append(f"{short(callee)}  ← entry point")
                self.depth += 1
            else:
                self.emit(line, f"call {short(callee)}")
                self.depth += 1
            return
        if kind == "function-return":
            if self.stack:
                if self.stack.pop():
                    self.depth = max(0, self.depth - 1)
                else:
                    self.muted -= 1
            return
        if kind == "failure":
            src = self.source(loc.get("file", ""), line)
            self.out.append(f"✗ line {line}: {src or s.get('reason', 'property violated')}")
            return
        if kind != "assignment" or s.get("hidden"):
            return

        lhs = s.get("lhs", "")
        val = s.get("value") or {}

        # bookkeeping: names of objects, enum constants, arrays
        if "<clinit>" in func and IDENT.match(lhs) and lhs.isupper() and val.get("name") == "pointer":
            self.names[pointee(val)] = class_of(func) + "." + lhs      # enum constant
            return
        if lhs.endswith("@class_identifier") and lhs.startswith("dynamic_object"):
            obj, cls = lhs.split(".")[0], str(val.get("data", "")).replace("java::", "")
            if cls.startswith("array["):
                self.arr_len.setdefault(obj, 0)
            elif not cls.startswith("java.") and obj not in self.names:
                simple = cls.split("$")[-1]
                self.counts[simple] = self.counts.get(simple, 0) + 1
                self.names[obj] = f"{simple}#{self.counts[simple]}"
                self.classes[obj] = simple
            return
        if lhs == "this" and "<init>" in func:
            self.constructed.add(pointee(val))
            return
        m = FIELD.match(lhs)
        if m and m.group(1) in self.arr_len:
            if m.group(2) == "length":
                self.arr_len[m.group(1)] = val.get("data", 0)
            elif m.group(2) == "data" and val.get("name") == "pointer":
                self.arr_data[m.group(1)] = pointee(val)
            return
        c = CELL.match(lhs)
        if c:
            self.cells.setdefault(c.group(1), {})[int(c.group(2))] = self.text(val)
            owner = [o for o, d in self.arr_data.items() if d == c.group(1)]
            if self.entered and not self.muted and not s.get("internal") and self.user(loc):
                name = self.alias.get(owner[0], "array") if owner else "array"
                self.emit(line, f"{name}[{c.group(2)}] = {self.text(val)}")
            return

        if not self.user(loc) or "<clinit" in func:
            return
        if self.nondet and lhs.startswith("return_tmp"):
            if not self.muted or self.depth <= 1:
                self.emit(self.nondet[1], f"{self.nondet[0]}() = {self.text(val)}")
            self.nondet = None
            return
        if self.muted:
            return
        if m:
            # only writes by the object's own code, after it was constructed
            if m.group(1) in self.constructed and self.classes.get(m.group(1)) == class_of(func):
                self.emit(line, f"{self.names[m.group(1)]}.{m.group(2)} = {self.text(val)}")
            return
        if not s.get("internal") and IDENT.match(lhs) and not TEMP.match(lhs):
            if val.get("name") == "pointer" and pointee(val) in self.arr_len:
                self.alias[pointee(val)] = lhs
            self.emit(line, f"{lhs} = {self.text(val)}")


def describe(r, sources):
    # "hw/MaxCheck.java:15   assertion: assert r >= a[j];" (a path VS Code can open)
    desc = re.sub(r" (at file|file) \S+ line \d+.*$", "", r.get("description", ""))
    loc = r.get("sourceLocation") or {}
    name = loc.get("file", "?").split("/")[-1]
    line = loc.get("line", "?")
    if desc == "assertion":
        src = Trace(sources).source(name, line)
        desc = f"assertion: {src}" if src else desc
    return f"{sources.get(name, name) + ':' + str(line):<30} {desc}"


def main():
    if len(sys.argv) < 3:
        sys.exit(__doc__)
    try:
        data = json.load(open(sys.argv[1]))
    except (OSError, ValueError):
        sys.exit("bmc: JBMC produced no readable output (see build/jbmc.json)")
    sources = {a.split("/")[-1]: a for a in sys.argv[2:]}
    messages = [m for m in data if isinstance(m, dict)] if isinstance(data, list) else []

    errors = [m["messageText"] for m in messages if m.get("messageType") == "ERROR"]
    for e in errors:
        print(f"JBMC error: {e}")
    results = [r for m in messages for r in m.get("result", [])]
    if not results:
        if not errors:
            print("JBMC reported no properties (see build/jbmc.json)")
        sys.exit(2)

    failed = [r for r in results if r.get("status") == "FAILURE"]
    passed = [r for r in results if r.get("status") == "SUCCESS"]
    texts = [m.get("messageText", "") for m in messages]
    sizes = [tuple(map(int, s)) for t in texts for s in re.findall(r"(\d+) variables, (\d+) clauses", t)]
    seconds = sum(float(s) for t in texts for s in re.findall(r"Runtime decision procedure: ([\d.e-]+)s", t))
    stats = [f"SAT formula: {max(sizes)[0]} variables, {max(sizes)[1]} clauses, solved in {seconds:.2f}s"] if sizes else []

    if os.environ.get("BMC_FULL"):
        for r in passed:
            print(f"  ok      {describe(r, sources)}")
    for r in failed:
        print(f"  FAILED  {describe(r, sources)}")
    print(f"\n{len(passed)} properties hold, {len(failed)} fail"
          + (f"   ({stats[0]})" if stats else ""))
    print("VERIFICATION FAILED" if failed else "VERIFICATION SUCCESSFUL")

    for r in failed:
        print(f"\nCounterexample for {describe(r, sources)}")
        print("-" * 72)
        t = Trace(sources)
        for s in r.get("trace", []):
            t.step(s)
        previous = None
        for l in t.out:
            if l != previous:
                print("  " + l)
            previous = l
    sys.exit(1 if failed else 0)


if __name__ == "__main__":
    main()
