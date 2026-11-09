# Hints for the homework

Stuck? Open one hint at a time, and try again after each. The hints never give the
whole answer.

## General tips

### OpenJML

- **Check one method at a time:** `./esc hw/Max.java --method max`. It is faster and the
  output is shorter.
- **Read the two lines of a failure together.** The first says *where* the proof fails;
  `Associated declaration` says *which clause* could not be proved.
- **Map the failure to the three obligations of the lecture:**

  | OpenJML says | Obligation | What to look at |
  | --- | --- | --- |
  | `LoopInvariantBeforeLoop` | Initiation: P ⇒ I | The invariant is false before the first iteration |
  | `LoopInvariant` | Preservation: {I ∧ B} S {I} | One iteration can break the invariant: it is false, or too weak to prove itself |
  | `Postcondition` after a loop | Exit: I ∧ ¬B ⇒ Q | The invariant does not say enough about the variables in Q |
  | `ArithmeticOperationRange` | An `int` overflows | Add a precondition, a bound in the invariant, or change the code |

- **Test a guess with `//@ assert`.** Put `//@ assert m >= a[0];` inside the loop: if
  OpenJML proves it, the fact is known there.
- **See the values of a failing proof with `//@ show`.** `//@ show i, s;` before the
  failing line prints the values of a counterexample. Remove it when you are done.
- **After the loop the prover knows only two things:** the invariant and the negation of
  the loop condition. Anything else about the loop's variables is forgotten (the
  `havoc` of the lecture).

### JBMC

- **Hunt bugs first, prove later.** Start with a small K and `--no-unwinding-assertions`.
  Then drop that option to check that K is enough.
- **Read the counterexample from the bottom up.** The `✗` line is the failed assertion;
  go up to see the inputs and the values that led there.
- **`--unwind K` counts evaluations of the loop condition.** A loop that runs n times
  and then exits needs K = n + 1; a failure inside the n-th iteration is found with K = n.
- **A harness is a contract in Java:** `CProver.assume(precondition)`, call the method,
  `assert postcondition`. `MaxCheck.java` is a complete example.
- `BMC_FULL=1 ./bmc ...` lists the properties that hold too; `BMC_RAW=1 ./bmc ...` shows
  JBMC's own output.

## 1. Absolute

<details>
<summary>Hint 1</summary>

The complaint is about one input for which `-n` cannot be represented as an `int`.
Which one?

</details>

<details>
<summary>Hint 2</summary>

In 32-bit two's complement, `-Integer.MIN_VALUE == Integer.MIN_VALUE`. A precondition
(`//@ requires ...;`) can exclude that input.

</details>

<details>
<summary>Hint 3</summary>

The contract has two parts: the result is non-negative, and it is `n` or `-n`
(use `||` inside one `ensures`, or write two `ensures`).

</details>

## 2. SumX

<details>
<summary>Hint 1</summary>

Two obligations fail: the postcondition and an overflow. Is either of them a bug in
the code? Try `sum(3)` by hand.

</details>

<details>
<summary>Hint 2</summary>

After the loop the prover knows `0 <= i && i <= x` and `i >= x`, so `i == x`. What
does it know about `s`? Nothing: the invariant does not mention it.

</details>

<details>
<summary>Hint 3</summary>

Add to the invariant how `s` relates to `i`: the formula of the `ensures`, with `i`
in place of `x`. For the largest *N*: when does `N * (N + 1) / 2` exceed
`Integer.MAX_VALUE` (2³¹ − 1)?

</details>

## 3. Max

<details>
<summary>Hint 1</summary>

The invariant must say, for the elements already looked at (`a[0]` to `a[i - 1]`),
what `m` is with respect to them. Also give the range of `i`.

</details>

<details>
<summary>Hint 2</summary>

The `ensures` has two parts, and the invariant needs both, restricted to
`0 <= j && j < i`: `m` is at least every element seen (`\forall`) and `m` is one of
them (`\exists`). For the `decreases`, which quantity goes down every iteration?

</details>

<details>
<summary>Hint 3</summary>

If the postcondition still fails with those invariants, compare what the loop covers
with what the `ensures` talks about. Which element does the loop never look at?
(Don't fix it yet.)

</details>

## 4. BinarySearch

<details>
<summary>Hint 1</summary>

The failure is in `midpoint`, not in `search`.

</details>

<details>
<summary>Hint 2</summary>

`lo` and `hi` are indices of an array, so they are at most `a.length - 1`. How large
must they be for `lo + hi` to exceed 2³¹ − 1? How long is that array?

</details>

<details>
<summary>Hint 3</summary>

Compute the midpoint so that no intermediate value is larger than `hi`. For the
question about `pure`: when a contract says nothing about what a method changes,
what may the caller assume it changes?

</details>

## 5. Max, again

<details>
<summary>Hint 1</summary>

Run `max` by hand on the array of the counterexample. How many times does the loop
run?

</details>

<details>
<summary>Hint 2</summary>

For the smallest bounds, lower `--unwind` and `--max-nondet-array-length` (keep
`--no-unwinding-assertions`) until JBMC stops finding the bug. How long must an array
be for this bug to show?

</details>

<details>
<summary>Hint 3</summary>

With unwinding assertions, JBMC also checks that K is enough for every input array.
The default `--max-nondet-array-length` is 5. With arrays of length ≤ 4, how many
times does the loop of the harness run, and how many times is its condition
evaluated?

</details>

## 6. SumX, again

<details>
<summary>Hint 1</summary>

The harness is the contract of exercise 2 in Java: `CProver.assume(...)` the
precondition, call `SumX.sum(x)`, `assert` the postcondition. See `MaxCheck.java`.

</details>

<details>
<summary>Hint 2</summary>

How many iterations does `sum(x)` make? With `--unwind 10 --no-unwinding-assertions`,
the executions that need more are dropped. Which values of `x` are left?

</details>

<details>
<summary>Hint 3</summary>

For a proof, K must cover the longest loop the precondition allows, plus one final
evaluation of the condition. Compare the "SAT formula" line for each K.

</details>

## 7. BinarySearch, again

<details>
<summary>Hint 1</summary>

With arrays of length ≤ 5, how large can `lo + hi` be?

</details>

<details>
<summary>Hint 2</summary>

The `midpoint` harness needs no array: two `int` parameters, `assume` the
`requires` of `midpoint`, call it, `assert` its `ensures`.

</details>
