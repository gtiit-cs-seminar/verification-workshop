# Homework plus: answers

Name(s):

### 1. Invariants from an LLM

| method | LLM | rounds | mistakes | final invariants by |
| --- | --- | --- | --- | --- |
| count | | | | |
| reverse | | | | |
| isqrt | | | | |
| dutchFlag | | | | |


Q: Why is it safe to use invariants written by an LLM here, even if the LLM is often wrong?


### 2. The Zune bug

Q: `--unwind 31 --no-unwinding-assertions` says SUCCESSFUL. Does that mean `yearOf` is correct for every day it checked?


Q: What does JBMC report without `--no-unwinding-assertions`? Is K too small, or is something else wrong?


Q: What is wrong with the loop? Which date froze the Zune?


Q: With your fix, what is the smallest K that gives VERIFICATION SUCCESSFUL with unwinding assertions? What exactly has been proved?


Q: A colleague changes `days > 366` to `days >= 366`. Which harness notices, and why?


Q (★): Why does `./esc` say OK on the original? What changes with `decreases`?


### 3. Range

Q: Why does `size()` not verify? Give concrete values.


Q: Compare JBMC's counterexample with your answer above.


### 4. StackAr

Q: Which tool reports your bug, and how? Could a bug get past OpenJML but not JBMC, or the other way around?

### 5. SafeLock

Q: Smallest K that finds a bug? Which sequence of events? What is wrong in the code?


Q: Formula size and time for K = 8, 10, 12.


Q: Does SUCCESSFUL with K = 12 mean the property holds for every sequence? Can unwinding assertions help?


