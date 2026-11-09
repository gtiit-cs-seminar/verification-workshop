import org.cprover.CProver;

// 7. Harnesses for the BinarySearch of exercise 4.
//   ./bmc BinarySearchCheck.search --unwind 6 --max-nondet-array-length 5
public class BinarySearchCheck {

    // The contract of search, for every sorted array of length <= 5.
    public static void search(int[] a, int key) {
        CProver.assume(a != null && a.length <= 5);
        for (int i = 0; i + 1 < a.length; i++) {
            CProver.assume(a[i] <= a[i + 1]);              // requires: a is sorted
        }

        int r = BinarySearch.search(a, key);

        if (r >= 0) {
            assert r < a.length && a[r] == key;
        } else {
            for (int j = 0; j < a.length; j++) {
                assert a[j] != key;
            }
        }
    }

    // TODO (7): the contract of midpoint, as a harness
    //   ./bmc BinarySearchCheck.midpoint --unwind 1
    public static void midpoint(int lo, int hi) {
        assert false;   // remove this line
    }
}
