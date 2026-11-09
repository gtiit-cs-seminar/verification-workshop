# Bonus (optional): the heap, and proofs without a bound

Deadline: **December 7, 2026**. Write your answers in [ANSWERS.md](ANSWERS.md).

**1. Composite: a data structure on the heap.** `Composite.java` is a tree in
which every node caches the maximum value of its subtree (`value`, `maxChild`),
and `addChild` updates the caches of the ancestors. It comes from a verification
benchmark (AutoProof's `composite.e`, through the Java version used in research
on assertion inference). `CompositeCheck.addChildren` lets JBMC build trees of
3 nodes with any sequence of `addChild` calls allowed by its precondition, and
checks the class invariant of every node after each call; it checks only one
part of the invariant so far.
Complete `checkInvariant` from the comments on the fields of `Composite`, and
run `./bmc CompositeCheck.addChildren --unwind K --no-unwinding-assertions` with
K = 2, 3, 4, 5.
**Q** Which part of the invariant breaks, after which calls? What is wrong in
`update`? Fix it and check again.
**Q** The bug needs only two calls to `addChild`. Why does JBMC need K = 4 to find it?
**Q** JBMC's input here is a heap: objects, references between them, aliasing.
How large is the formula compared with the previous exercises? What would you
need to verify `Composite` with OpenJML?
★ The original requires `c` to be a single node. Allow subtrees: drop
`c.firstChild == null` from `addChild` and from the harness's `assume`, and run
again. Does your invariant notice what goes wrong? If not, add the missing
property, then fix `addChild` so that subtrees can be added safely.

**2. SafeLock for any number of events.** In homework plus (exercise 5), JBMC
checked `SafeLock` for sequences of up to K events. Run
`./esc plus/SafeLock.java` on your fixed version.
**Q** Why does OpenJML think `failed++` may overflow?
Write a class invariant (`//@ private invariant ...;`) relating `failed` and
`state` that makes `SafeLock` verify. Then put the bug of exercise 5 back:
**Q** Does your invariant catch it? What does this proof give you that JBMC did not?
