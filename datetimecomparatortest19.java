package org.joda.time;

import java.util.Comparator;
import junit.framework.TestCase;

/**
 * Contains tests for the DateTimeComparator, focusing on hour-based comparisons.
 *
 * The comparator under test is configured to compare instants based only on the
 * 'hourOfDay' field, ignoring larger fields like day, month, and year.
 */
public class DateTimeComparatorTestTest19 extends TestCase {

    /**
     * Creates a comparator that only considers the hour-of-day field.
     * It compares fields greater than or equal to 'hourOfDay' (lower limit, inclusive)
     * and less than 'dayOfYear' (upper limit, exclusive).
     * This effectively isolates the comparison to the 'hourOfDay'.
     */
    private final Comparator<Object> hourOnlyComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());

    /**
     * Tests that the comparator correctly identifies a time with an earlier hour
     * as "less than" a time with a later hour on the same day.
     */
    public void testHourComparison_EarlierHourOnSameDay() {
        // Arrange
        DateTime time2200 = new DateTime("1969-12-31T22:00:00", DateTimeZone.UTC);
        DateTime time2300 = new DateTime("1969-12-31T23:00:00", DateTimeZone.UTC);

        // Act & Assert
        assertTrue("Comparing 22:00 to 23:00 should be negative",
                hourOnlyComparator.compare(time2200, time2300) < 0);

        assertTrue("Comparing 23:00 to 22:00 should be positive",
                hourOnlyComparator.compare(time2300, time2200) > 0);
    }

    /**
     * Tests that the comparator correctly identifies a time with an earlier hour
     * as "less than" a time with a later hour, even when they are on different days.
     * This confirms that the date part is ignored.
     */
    public void testHourComparison_EarlierHourOnDifferentDay() {
        // Arrange
        DateTime time0000 = new DateTime("1970-01-01T00:00:00", DateTimeZone.UTC);
        DateTime time0100 = new DateTime("1970-01-01T01:00:00", DateTimeZone.UTC);

        // Act & Assert
        assertTrue("Comparing 00:00 to 01:00 should be negative",
                hourOnlyComparator.compare(time0000, time0100) < 0);

        assertTrue("Comparing 01:00 to 00:00 should be positive",
                hourOnlyComparator.compare(time0100, time0000) > 0);
    }

    /**
     * Tests the edge case where the hour field rolls over the day boundary.
     * The comparator should only evaluate the hour (23 vs. 00) and ignore the
     * fact that the second instant occurs on the next calendar day.
     */
    public void testHourComparison_IgnoresDateWhenHourRollsOver() {
        // Arrange
        // Although time2359 is chronologically earlier, its hour-of-day (23) is greater.
        DateTime time2359_day1 = new DateTime("1969-12-31T23:59:59", DateTimeZone.UTC);
        DateTime time0000_day2 = new DateTime("1970-01-01T00:00:00", DateTimeZone.UTC);

        // Act & Assert
        // The comparator sees 23 > 0, so the result is positive.
        assertTrue("Comparing 23:59 (Day 1) to 00:00 (Day 2) should be positive based on hour",
                hourOnlyComparator.compare(time2359_day1, time0000_day2) > 0);

        // The comparator sees 0 < 23, so the result is negative.
        assertTrue("Comparing 00:00 (Day 2) to 23:59 (Day 1) should be negative based on hour",
                hourOnlyComparator.compare(time0000_day2, time2359_day1) < 0);
    }
}