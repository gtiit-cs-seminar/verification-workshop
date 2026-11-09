// 1. Loops whose invariants you will ask an LLM for; OpenJML judges its answers.
// The contracts are complete and the code is correct: only loop invariants
// (and decreases clauses) are missing.
//   ./esc plus/LlmLoops.java --method count
public class LlmLoops {

    // Number of occurrences of x in a.
    //@ ensures \result == (\num_of int k; 0 <= k && k < a.length; a[k] == x);
    public static int count(int[] a, int x) {
        int c = 0;
        // TODO (1)
        for (int i = 0; i < a.length; i++) {
            if (a[i] == x) {
                c++;
            }
        }
        return c;
    }

    // Reverses a in place.
    //@ assignable a[*];
    //@ ensures (\forall int k; 0 <= k && k < a.length; a[k] == \old(a[a.length - 1 - k]));
    public static void reverse(int[] a) {
        int i = 0, j = a.length - 1;
        // TODO (1)
        while (i < j) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
            i++;
            j--;
        }
    }

    // Integer square root.
    //@ requires 0 <= n && n <= 1000000;
    //@ ensures \result * \result <= n && n < (\result + 1) * (\result + 1);
    public static int isqrt(int n) {
        int r = 0;
        // TODO (1)
        while ((r + 1) * (r + 1) <= n) {
            r++;
        }
        return r;
    }

    // Dijkstra's Dutch national flag: sorts an array of 0s, 1s and 2s in one pass.
    //@ requires (\forall int k; 0 <= k && k < a.length; 0 <= a[k] && a[k] <= 2);
    //@ assignable a[*];
    //@ ensures (\forall int p, q; 0 <= p && p < q && q < a.length; a[p] <= a[q]);
    public static void dutchFlag(int[] a) {
        int lo = 0, mid = 0, hi = a.length;
        // TODO (1)
        while (mid < hi) {
            if (a[mid] == 0) {
                int t = a[lo];
                a[lo] = a[mid];
                a[mid] = t;
                lo++;
                mid++;
            } else if (a[mid] == 1) {
                mid++;
            } else {
                hi--;
                int t = a[mid];
                a[mid] = a[hi];
                a[hi] = t;
            }
        }
    }
}
