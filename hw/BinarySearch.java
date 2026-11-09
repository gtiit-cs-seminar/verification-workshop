// 4. Binary search, with its contract and loop invariants already written.
//   ./esc hw/BinarySearch.java
public class BinarySearch {

    //@ requires 0 <= lo && lo <= hi;
    //@ ensures lo <= \result && \result <= hi;
    static /*@ pure @*/ int midpoint(int lo, int hi) {
        return (lo + hi) / 2;
    }

    //@ requires (\forall int i, j; 0 <= i && i < j && j < a.length; a[i] <= a[j]);
    //@ ensures \result >= 0 ==> a[\result] == key;
    //@ ensures \result < 0 ==> (\forall int j; 0 <= j && j < a.length; a[j] != key);
    public static int search(int[] a, int key) {
        int lo = 0, hi = a.length - 1;
        //@ loop_invariant 0 <= lo && hi < a.length && lo <= hi + 1;
        //@ loop_invariant (\forall int j; 0 <= j && j < lo; a[j] < key);
        //@ loop_invariant (\forall int j; hi < j && j < a.length; a[j] > key);
        //@ decreases hi - lo;
        while (lo <= hi) {
            int mid = midpoint(lo, hi);
            if (a[mid] < key) {
                lo = mid + 1;
            } else if (a[mid] > key) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
