# Homework plus: LLMs, bit tricks, and classes with state

Deadline: **November 24, 2026**. Do [the homework](../hw/README.md) first: this part
uses the same commands and ideas. Write your answers in [ANSWERS.md](ANSWERS.md).

**1. Invariants from an LLM.** `LlmLoops.java` has four correct methods with
complete contracts and no loop invariants: `count`, `reverse`, `isqrt`,
`dutchFlag`. For each one, give an LLM the method with its contract and ask for
the JML `loop_invariant` and `decreases` clauses. Paste them and run
`./esc plus/LlmLoops.java --method NAME`. When OpenJML rejects them, give the
LLM the error message and try again — at most three rounds, then fix them
yourself.
**Q** For each method: which LLM, how many rounds, what kinds of mistakes
(syntax, false invariant, true but too weak, missing `decreases`, …), and who
had the last word.
**Q** Why is it safe to use invariants written by an LLM here, even if the LLM
is often wrong?

**2. Bit tricks.** `BitTricks.java` has four fast functions, each with a slow
reference version. Some fast versions are wrong. `BitTricksCheck.isPowerOfTwo`
checks that the fast and slow versions agree on every input. Write the harnesses
`average`, `nextPowerOfTwo` (only for `1 <= cap <= 2^30`), `absEqualsMathAbs` and
`absIsNonNegative`; run them all with `--unwind 32`. Answer these questions
*before* changing `BitTricks.java`:
**Q** Which functions are wrong, for which inputs?
**Q** For the correct ones JBMC says SUCCESSFUL, with unwinding assertions on.
How many inputs did it cover? Is it a proof?
**Q** Run `./bmc BitTricksCheck.isPowerOfTwo --unwind 5 --no-unwinding-assertions`
on the buggy `isPowerOfTwo`. Why SUCCESSFUL?
**Q** `absIsNonNegative` fails. Is `BitTricks.abs` wrong?
Then fix the functions that are wrong and run all the harnesses again.
★ Write a JML contract for `isPowerOfTwo` and try OpenJML on it.

**3. Range** (a record with a class invariant). Run `./esc plus/Range.java`.
**Q** Why does `size()` not verify? Give concrete values.
Write the harness `RangeCheck.size` and run it with `--unwind 1`.
**Q** Compare JBMC's counterexample with your answer. Then fix the code (not the
contract) and check it with both tools.

**4. StackAr.** Implement the stack of `StackAr.java` (an array and the index of
the top element) and write its specification: a class invariant, and a contract
for each constructor and method (`requires`, `ensures`, and `assignable` for the
methods that change the stack; `\old` refers to the state before the call).
Verify it with `./esc plus/StackAr.java`. (The given file already "verifies":
with no contract there is nothing to prove.)
Then run `./bmc StackArCheck.operations --unwind 5 --no-unwinding-assertions`:
JBMC runs any sequence of `push`, `pop` and `peek` on a stack of capacity ≤ 3
and compares every answer with a plain array and a counter.
**Q** Introduce a typical bug in your implementation (e.g. `isFull` off by one,
or `pop` that does not decrement `top`). Which tool reports it, and how? Could
a bug get past OpenJML but not JBMC, or the other way around? Undo the bug.

**5. SafeLock.** Read `SafeLock.java` (a hotel-room safe) and
`SafeLockCheck.neverOpensAfterTooManyWrongPins`: JBMC plays the user, choosing
any sequence of events; `--unwind K` bounds the number of events.
Run it with `--no-unwinding-assertions` and K = 1, 2, 3, …
**Q** What is the smallest K that finds a bug? Which sequence of events breaks
the property? What is wrong in the code?
Fix `SafeLock` and run it again (still with `--no-unwinding-assertions`) with
K = 8, 10, 12. **Q** Note the formula size and time.
Write `SafeLockCheck.onlyAdminUnblocks` (a blocked safe leaves BLOCKED only by
an `AdminReset` with the right code) and run it with `--unwind 12 --no-unwinding-assertions`.
**Q** Does SUCCESSFUL with K = 12 mean the property holds for every sequence of
events? Can unwinding assertions help here?

## Before you submit

- `./esc` says OK for `LlmLoops`, `Range` (the code fixed, the contract unchanged) and
  `StackAr` (implemented, with your contracts, without the bug you introduced).
- `./bmc` gives VERIFICATION SUCCESSFUL for the five `BitTricksCheck` harnesses except
  `absIsNonNegative` (with `--unwind 32`), for `RangeCheck.size --unwind 1`, for
  `StackArCheck.operations --unwind 5 --no-unwinding-assertions` and, on the fixed
  `SafeLock`, for both `SafeLockCheck` harnesses with `--unwind 8 --no-unwinding-assertions`.
- No `assert false;   // remove this line` is left in a harness.
- Every question in `ANSWERS.md` is answered, and the LLM table is filled in.
