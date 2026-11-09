import org.cprover.CProver;

// 2. Equivalence harnesses: the fast version must agree with the reference on
// EVERY input (2^32 or 2^64 of them).
//   ./bmc BitTricksCheck.isPowerOfTwo --unwind 32
public class BitTricksCheck {

    public static void isPowerOfTwo(int x) {
        assert BitTricks.isPowerOfTwo(x) == BitTricks.isPowerOfTwoRef(x);
    }

    // TODO (2): harnesses average, nextPowerOfTwo, absEqualsMathAbs, absIsNonNegative
}
