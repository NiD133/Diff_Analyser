package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for the {@link Weeks} class, focusing on the
 * {@link Weeks#standardWeeksIn(ReadablePeriod)} factory method.
 */
public class WeeksTest {

    @Test
    public void standardWeeksIn_givenNullPeriod_returnsZeroWeeks() {
        assertEquals("A null period should result in zero weeks.",
                     0, Weeks.standardWeeksIn(null).getWeeks());
    }

    @Test
    public void standardWeeksIn_givenPeriodOfWeeks_returnsSameNumberOfWeeks() {
        // Test with a zero-length period
        assertEquals("A zero period should result in zero weeks.",
                     0, Weeks.standardWeeksIn(Period.ZERO).getWeeks());

        // Test with a period of exactly one week
        assertEquals("A one-week period should result in one week.",
                     1, Weeks.standardWeeksIn(Period.weeks(1)).getWeeks());

        // Test with a positive number of weeks
        assertEquals("A 123-week period should result in 123 weeks.",
                     123, Weeks.standardWeeksIn(Period.weeks(123)).getWeeks());

        // Test with a negative number of weeks
        assertEquals("A negative 987-week period should result in -987 weeks.",
                     -987, Weeks.standardWeeksIn(Period.weeks(-987)).getWeeks());
    }

    @Test
    public void standardWeeksIn_givenPeriodOfDays_truncatesToWholeWeeks() {
        // The method calculates the number of *complete* standard weeks.
        // Any remainder of days is truncated.

        // 13 days = 1 week and 6 days. Should truncate to 1 week.
        assertEquals("A 13-day period should truncate to 1 week.",
                     1, Weeks.standardWeeksIn(Period.days(13)).getWeeks());

        // 14 days = exactly 2 weeks.
        assertEquals("A 14-day period is exactly 2 weeks.",
                     2, Weeks.standardWeeksIn(Period.days(14)).getWeeks());

        // 15 days = 2 weeks and 1 day. Should truncate to 2 weeks.
        assertEquals("A 15-day period should truncate to 2 weeks.",
                     2, Weeks.standardWeeksIn(Period.days(15)).getWeeks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void standardWeeksIn_givenPeriodWithImpreciseField_throwsIllegalArgumentException() {
        // The standardWeeksIn() method only accepts periods with precise durations
        // (weeks, days, hours, etc.). Months have a variable length.
        // Therefore, passing a period containing months should fail.
        Weeks.standardWeeksIn(Period.months(1));
    }
}