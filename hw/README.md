# Homework: the lecture's examples, with both tools

Deadline: **November 17, 2026**. Read the [main README](../README.md) first
(setup and the two commands). Write your answers in [ANSWERS.md](ANSWERS.md).

Exercises 1–4 use OpenJML (way A, loop invariants), exercises 5–7 use JBMC on
the same programs (way B, unrolling), and exercise 8 compares them.

## OpenJML

**1. Absolute.** Run `./esc hw/Absolute.java` *before* writing anything.
**Q** What does OpenJML complain about, and for which input?
Then write the contract of `abs`: the result is non-negative and is `n` or `-n`.
**Q** Which `requires` did you need, and why? Could you avoid it by changing the
`ensures` instead?

**2. SumX** (the example of the lecture). The contract and an invariant are
given.
**Q** Which proof obligations fail? Is the program wrong?
Strengthen the invariant until the method verifies.
**Q** The precondition says `x <= 1000`. What is the largest bound *N* for which
`x <= N` still verifies, and what does OpenJML report for *N + 1*? Why?
Then put back `x <= 1000`: exercise 6 uses it.

**3. Max.** Write loop invariants (and a `decreases` clause) for `max`.
Keep going until OpenJML reports only failures you cannot fix with better
invariants.
**Q** Is the remaining failure caused by your invariants or by the code? Write your
hypothesis — **do not fix the code yet**: you will check it with JBMC in exercise 5.

**4. BinarySearch.** The contracts and loop invariants are given.
**Q** What does OpenJML report, and in which method? Describe a concrete call
that triggers it. How large must the array be?
**Q** `search` is verified using only the *contract* of `midpoint`, not its code.
Remove `/*@ pure @*/` from `midpoint`: what happens, and why?
Put `pure` back, fix `midpoint` (keep the original line in a comment: you will
need it in exercise 7) and verify.

## JBMC

The harnesses `*Check.java` call the code you just verified.

**5. Max, again.** With `Max.java` as you left it in exercise 3:
`./bmc MaxCheck.check --unwind 3 --no-unwinding-assertions`.
**Q** What input did JBMC find, and what goes wrong with it? Was your hypothesis
of exercise 3 right?
**Q** What are the smallest `--unwind` and the smallest
`--max-nondet-array-length` that still find the bug (keep `--no-unwinding-assertions`)? Relate them to the lecture's
small scope hypothesis.
Now fix `Max.max`, and check it with both tools.
**Q** Run `./bmc MaxCheck.check --unwind 5` (with unwinding assertions). Why does
it fail now? Add `--max-nondet-array-length 4`: what is the smallest `--unwind`
that gives VERIFICATION SUCCESSFUL, and why not one less? What exactly has been
proved?

**6. SumX.** Write the harness `SumXCheck.check` (the contract of exercise 2).
**Q** `--unwind 10 --no-unwinding-assertions` says SUCCESSFUL. Which values of
`x` were really checked? Drop `--no-unwinding-assertions`: why does it fail?
Which *K* turns the result into a proof? Try it and compare the formula size
and time for K = 10, 100 and that *K*.
**Q** What did OpenJML need from you to prove `sum` correct, and what did JBMC
need?

**7. BinarySearch.** Put back the original `midpoint` (`(lo + hi) / 2`).
Run `./bmc BinarySearchCheck.search --unwind 6 --max-nondet-array-length 5`.
**Q** It says SUCCESSFUL, but OpenJML found a bug. Why does JBMC miss it with this
harness? Could a larger bound help in practice?
Write the harness `BinarySearchCheck.midpoint` (contract of `midpoint`) and run
it with `--unwind 1`. **Q** What did it find? Restore your fix and check again.

## Comparison

**8.** Fill in the table in ANSWERS.md for `SumX`, `Max` and `BinarySearch`, and
answer: when would you use each tool?

## Before you submit

- `./esc` says OK for every file in `hw/`: `Absolute`, `SumX` (with `x <= 1000`), `Max`
  (invariants and the fix) and `BinarySearch` (the fixed `midpoint`).
- `./bmc` gives VERIFICATION SUCCESSFUL for `MaxCheck.check --unwind 5 --max-nondet-array-length 4`,
  `SumXCheck.check --unwind 1001` and `BinarySearchCheck.midpoint --unwind 1`.
- No `assert false;   // remove this line` is left in a harness.
- Every question in `ANSWERS.md` is answered.
