// 2. The sumx example from the lecture: 0 + 1 + ... + x.
//   ./esc hw/SumX.java
public class SumX {

    //@ requires 0 <= x && x <= 1000;
    //@ ensures \result == x * (x + 1) / 2;
    public static int sum(int x) {
        int s = 0, i = 0;
        // TODO (2): this invariant is true, but is it enough?
        //@ loop_invariant 0 <= i && i <= x;
        //@ decreases x - i;
        while (i < x) {
            i = i + 1;
            s = s + i;
        }
        return s;
    }
}
