import org.cprover.CProver;

// 5. A JBMC harness for Max.max: the JML contract of exercise 3 written as Java code.
// The parameters of check are the inputs: JBMC considers every possible array.
//   ./bmc MaxCheck.check --unwind 3 --no-unwinding-assertions
public class MaxCheck {

    public static void check(int[] a) {
        CProver.assume(a != null && a.length > 0);        // requires a.length > 0

        int r = Max.max(a);

        boolean found = false;
        for (int j = 0; j < a.length; j++) {
            assert r >= a[j];                              // ensures (\forall ...)
            if (r == a[j]) {
                found = true;
            }
        }
        assert found;                                      // ensures (\exists ...)
    }
}
