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


### 2. Bit tricks

Q: Which functions are wrong, for which inputs?


Q: For the correct ones, how many inputs were covered? Is it a proof?


Q: Why does `--unwind 5 --no-unwinding-assertions` say SUCCESSFUL on the buggy `isPowerOfTwo`?


Q: `absIsNonNegative` fails. Is `BitTricks.abs` wrong?


### 3. Range

Q: Why does `size()` not verify? Give concrete values.


Q: Compare JBMC's counterexample with your answer above.


### 4. StackAr

Q: Which tool reports your bug, and how? Could a bug get past OpenJML but not JBMC, or the other way around?

### 5. SafeLock

Q: Smallest K that finds a bug? Which sequence of events? What is wrong in the code?


Q: Formula size and time for K = 8, 10, 12.


Q: Does SUCCESSFUL with K = 12 mean the property holds for every sequence? Can unwinding assertions help?


