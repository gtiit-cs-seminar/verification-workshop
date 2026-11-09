import org.cprover.CProver;

// 2. Harnesses for Zune.yearOf, for every day of the first 30 years.
//   ./bmc ZuneCheck.yearOfInRange --unwind 31 --no-unwinding-assertions
public class ZuneCheck {

    static final int MAX_DAYS = 365 * 30;

    // A weak property: the year is in range.
    public static void yearOfInRange(int days) {
        CProver.assume(1 <= days && days <= MAX_DAYS);
        int year = Zune.yearOf(days);
        assert Zune.ORIGIN_YEAR <= year && year < Zune.ORIGIN_YEAR + 30;
    }

    // TODO (2): the right year: days falls after all the days of the years before
    // `year`, and no later than the last day of `year`.
    public static void yearOfIsRight(int days) {
        assert false;   // remove this line
    }

    // The number of days from January 1, 1980 to the end of the year before `year`.
    static int daysBefore(int year) {
        int total = 0;
        for (int y = Zune.ORIGIN_YEAR; y < year; y++) {
            total += Zune.isLeapYear(y) ? 366 : 365;
        }
        return total;
    }
}
