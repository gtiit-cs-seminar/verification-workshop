// 2. The Zune bug. On December 31, 2008, every Microsoft Zune 30 (a music player)
// froze for the whole day. Its clock driver turned the number of days since
// January 1, 1980 into a year with this loop (translated from C).
public class Zune {

    static final int ORIGIN_YEAR = 1980;

    static /*@ pure @*/ boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    // The year of a day: day 1 is January 1, 1980. Only for the first 30 years.
    //@ requires 1 <= days && days <= 365 * 30;
    //@ ensures ORIGIN_YEAR <= \result && \result < ORIGIN_YEAR + 30;
    static int yearOf(int days) {
        int year = ORIGIN_YEAR;
        //@ loop_invariant 1 <= days && ORIGIN_YEAR <= year && days + 365 * (year - ORIGIN_YEAR) <= \old(days);
        while (days > 365) {
            if (isLeapYear(year)) {
                if (days > 366) {
                    days -= 366;
                    year += 1;
                }
            } else {
                days -= 365;
                year += 1;
            }
        }
        return year;
    }
}
