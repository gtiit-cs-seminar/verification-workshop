// 2. Bit tricks (in the style of "Hacker's Delight" and java.util.HashMap).
// Each fast version comes with a slow, obviously correct reference version.
// Some fast versions are wrong. Check each pair with a harness in BitTricksCheck.
public class BitTricks {

    // Is x a power of two (1, 2, 4, ..., 2^30)?
    static boolean isPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }

    static boolean isPowerOfTwoRef(int x) {
        for (int k = 0; k <= 30; k++) {
            if (x == 1 << k) {
                return true;
            }
        }
        return false;
    }

    // floor((a + b) / 2) without overflow
    static int average(int a, int b) {
        return (a & b) + ((a ^ b) >> 1);
    }

    static int averageRef(int a, int b) {
        long sum = (long) a + b;              // cannot overflow in 64 bits
        long half = sum / 2;                  // rounds toward zero...
        if (sum < 0 && sum % 2 != 0) {
            half--;                           // ...so round negative odd sums down
        }
        return (int) half;
    }

    // The smallest power of two >= cap, for 1 <= cap <= 2^30 (HashMap.tableSizeFor).
    static int nextPowerOfTwo(int cap) {
        int n = cap;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    static int nextPowerOfTwoRef(int cap) {
        int p = 1;
        while (p < cap) {
            p = p * 2;
        }
        return p;
    }

    // Absolute value without branches.
    static int abs(int x) {
        int mask = x >> 31;           // 0 if x >= 0, -1 (all ones) if x < 0
        return (x ^ mask) - mask;
    }
}
