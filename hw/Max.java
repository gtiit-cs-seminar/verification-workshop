// 3. Maximum of an array (the example from the lecture).
//   ./esc hw/Max.java
public class Max {

    //@ requires a.length > 0;
    //@ ensures (\forall int j; 0 <= j && j < a.length; \result >= a[j]);
    //@ ensures (\exists int j; 0 <= j && j < a.length; \result == a[j]);
    public static int max(int[] a) {
        int m = a[0];
        // TODO (3): loop invariants (and a decreases clause)
        for (int i = 1; i < a.length - 1; i++) {
            if (a[i] > m) {
                m = a[i];
            }
        }
        return m;
    }
}
