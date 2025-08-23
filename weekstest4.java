package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#weeksBetween(ReadablePartial, ReadablePartial)} factory method.
 */
public class WeeksTest {

    @Test
    public void weeksBetween_withEndAfterStart_calculatesPositiveWeeks() {
        // A 21-day period is exactly 3 weeks.
        ReadablePartial start = new LocalDate(2006, 6, 9);
        ReadablePartial end = new LocalDate(2006, 6, 30);

        Weeks expected = Weeks.THREE;
        Weeks actual = Weeks.weeksBetween(start, end);

        assertEquals(expected, actual);
    }

    @Test
    public void weeksBetween_withSameStartAndEnd_returnsZeroWeeks() {
        ReadablePartial date = new LocalDate(2006, 6, 9);

        Weeks expected = Weeks.ZERO;
        Weeks actual = Weeks.weeksBetween(date, date);

        assertEquals(expected, actual);
    }

    @Test
    public void weeksBetween_withEndBeforeStart_calculatesNegativeWeeks() {
        // The same 21-day period, but with start and end dates swapped.
        ReadablePartial start = new LocalDate(2006, 6, 30);
        ReadablePartial end = new LocalDate(2006, 6, 9);

        Weeks expected = Weeks.weeks(-3);
        Weeks actual = Weeks.weeksBetween(start, end);

        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("deprecation") // Using deprecated YearMonthDay for test compatibility
    public void weeksBetween_withDifferentPartialTypes_calculatesCorrectly() {
        // A 42-day period (6 weeks) between a LocalDate and a YearMonthDay.
        ReadablePartial start = new LocalDate(2006, 6, 9);
        ReadablePartial end = new YearMonthDay(2006, 7, 21);

        Weeks expected = Weeks.weeks(6);
        Weeks actual = Weeks.weeksBetween(start, end);

        assertEquals(expected, actual);
    }
}