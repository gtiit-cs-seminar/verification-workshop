# Homework: answers

Name(s):

## OpenJML

### 1. Absolute

Q: What does OpenJML complain about before you write a contract, and for which input?


Q: Which `requires` did you need, and why? Could you avoid it by changing the `ensures`?


### 2. SumX

Q: Which proof obligation fails? Is the program wrong?


Q: Largest N for which `x <= N` verifies? What happens with N + 1, and why?


### 3. Max

Q: Is the remaining failure caused by your invariants or by the code? (Your hypothesis.)


### 4. BinarySearch

Q: What does OpenJML report, in which method, and for which concrete call? How large must the array be?


Q: What happens without `/*@ pure @*/` on `midpoint`, and why?


## JBMC

### 5. Max, again

Q: What input did JBMC find, and what goes wrong? Was your hypothesis of exercise 3 right?


Q: Smallest `--unwind` and `--max-nondet-array-length` that find it? Relation to the small scope hypothesis?


Q: Why does `--unwind 5` with unwinding assertions fail on the fixed version? Smallest K with `--max-nondet-array-length 4`, and why not one less? What has been proved?


### 6. SumX

Q: Which values of `x` were checked with `--unwind 10 --no-unwinding-assertions`? Why does it fail with unwinding assertions? Which K is a proof? Sizes and times for K = 10, 100 and that K.


Q: What did OpenJML need from you, and what did JBMC need?


### 7. BinarySearch

Q: Why does JBMC miss the bug with the `search` harness? Could a larger bound help?


Q: What did the `midpoint` harness find?


## Comparison

### 8. Comparison

| program | OpenJML: result, what you wrote | JBMC: result, bound used |
| --- | --- | --- |
| SumX | | |
| Max | | |
| BinarySearch | | |

Q: When would you use each tool?

