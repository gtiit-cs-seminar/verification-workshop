# Hints for the homework plus

Stuck? Open one hint at a time, and try again after each. The hints never give the
whole answer. The [general tips of the homework](../hw/HINTS.md#general-tips) apply here
too.

## 1. Invariants from an LLM

<details>
<summary>Hint 1</summary>

Give the LLM context: the method with its contract, that it is Java with JML checked
by OpenJML, and that you want only `//@ loop_invariant` and `//@ decreases` lines.
Tell it not to change the code or the contract.

</details>

<details>
<summary>Hint 2</summary>

When OpenJML rejects an invariant, paste the whole error to the LLM, including the
`Associated declaration` line. Ask which of the three obligations (initiation,
preservation, exit) fails and why.

</details>

<details>
<summary>Hint 3</summary>

What LLMs often forget: bounds on the variables (`0 <= c`, `c <= i`), what has not
changed yet (the middle of the array in `reverse`), and properties that hold for the
whole array (in `dutchFlag`, every value stays in 0..2). Reject any answer that adds
`//@ assume` or changes the `ensures`: that proves a different, weaker thing.

</details>

## 2. The Zune bug

<details>
<summary>Hint 1</summary>

With `--no-unwinding-assertions`, executions still looping after K iterations are
dropped silently. Which executions might never stop looping?

</details>

<details>
<summary>Hint 2</summary>

Follow `days` and `year` in the counterexample. In the last iteration, which branch
runs, and what changes? Then try a much larger K: does the failure go away?

</details>

<details>
<summary>Hint 3</summary>

Look at the case `days == 366` with `year` a leap year. Which day of the year is
day 366? Where should that day belong, and how can the loop stop there?

</details>

<details>
<summary>Hint 4 (yearOfIsRight and K)</summary>

`daysBefore(year)` is the number of days before `year` starts. The day must be after
all of those, and no later than the last day of `year` (365 or 366 days later).
For K: in 365 · 30 days, how many full years can the loop subtract?

</details>

## 3. Range

<details>
<summary>Hint 1</summary>

What is the largest value `hi - lo + 1` can take, for any `int`s with `lo <= hi`?

</details>

<details>
<summary>Hint 2</summary>

The contract is computed with mathematical integers, the code with 32-bit `int`s.
Can an `int` hold every possible size?

</details>

<details>
<summary>Hint 3</summary>

The contract already casts to `long`. Change the code, not the contract: its return
type and the arithmetic.

</details>

## 4. StackAr

<details>
<summary>Hint 1</summary>

Start with the class invariant: what values can `top` take, given the length of
`elems`? (`top == -1` means empty.)

</details>

<details>
<summary>Hint 2</summary>

Methods that only look (`isEmpty`, `isFull`, `size`, `peek`): `pure`, and an
`ensures` that relates `\result` to `top` (and to `elems`). Methods that change the
stack (`push`, `pop`): a `requires` (not full, not empty), an `assignable` with
exactly what they change, and an `ensures` that uses `\old(top)`.

</details>

<details>
<summary>Hint 3</summary>

A contract that only talks about `top` lets wrong implementations through. For
`push`, also say what happens to the new element and to the ones below it. If the
no-argument constructor does not verify, check what the other constructor's
`ensures` promises: it calls it with `this(...)`.

</details>

## 5. SafeLock

<details>
<summary>Hint 1</summary>

Increase K one at a time, with `--no-unwinding-assertions`. The smallest K that fails
is the length of the shortest attack.

</details>

<details>
<summary>Hint 2</summary>

In the counterexample, list the events in order (`EnterPin`, `Close`, `AdminReset`)
and the value of `failed` after each. Which event changes `failed` when it should
not?

</details>

<details>
<summary>Hint 3 (property 2)</summary>

Build the safe and loop over `anyEvent()` as in property 1. Before `handle(e)`,
remember whether the safe was BLOCKED. After it: if it was blocked and is not any
more, `e` must be an `AdminReset` with the right code
(`e instanceof SafeLock.AdminReset r && r.code() == adminCode`).

</details>
