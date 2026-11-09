// 3. A Java record with a class invariant.
//   ./esc plus/Range.java

/** The closed interval of ints [lo, hi]. */
public record Range(int lo, int hi) {

    //@ public invariant lo <= hi;

    //@ requires lo <= hi;
    public Range {
    }

    // Number of ints in the interval.
    //@ ensures \result == (long) hi - lo + 1;
    public int size() {
        return hi - lo + 1;
    }

    //@ ensures \result <==> (lo <= x && x <= hi);
    public boolean contains(int x) {
        return lo <= x && x <= hi;
    }
}
