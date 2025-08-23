package org.joda.time;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DateTimeComparator} specifically focusing on the weekyear field.
 *
 * <p>This test class clarifies the behavior of the weekyear comparator, especially
 * around the boundary of a calendar year, which can be non-intuitive.
 * According to the ISO 8601 standard, the weekyear can differ from the calendar year
 * for dates at the beginning or end of the year.
 */
public class DateTimeComparatorWeekyearTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that the weekyear comparator correctly handles dates across a calendar year boundary
     * where the weekyear remains the same.
     *
     * <p>The ISO 8601 standard defines the first week of a year as the one containing the first
     * Thursday. Consequently, dates like Dec 31st and Jan 1st can belong to the same weekyear.
     * For example, Friday, Jan 1st, 1999, is part of week 53 of the weekyear 1998. The new
     * weekyear (1999) doesn't start until Monday, Jan 4th, 1999.
     */
    @Test
    public void testCompare_Weekyear_AcrossYearBoundary() {
        // A comparator that only considers the weekyear field.
        // It ignores fields finer than weekyear (e.g., weekOfWeekyear, dayOfWeek, time).
        Comparator<Object> weekyearComparator = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());

        // Test case 1: Dates in different calendar years but the same weekyear.
        // Thursday, 31st Dec 1998 is in week 53 of weekyear 1998.
        DateTime dateIn1998 = new DateTime("1998-12-31T23:59:59", UTC);
        // Friday, 1st Jan 1999 is also in week 53 of weekyear 1998.
        DateTime dateInSameWeekyear = new DateTime("1999-01-01T00:00:00", UTC);

        assertEquals(
            "Dates in the same weekyear should be considered equal",
            0,
            weekyearComparator.compare(dateIn1998, dateInSameWeekyear)
        );

        // Test case 2: Dates in different weekyears.
        // Monday, 4th Jan 1999 is the start of week 1 of weekyear 1999.
        DateTime dateInNextWeekyear = new DateTime("1999-01-04T00:00:00", UTC);

        assertEquals(
            "A date in weekyear 1998 should be less than a date in weekyear 1999",
            -1,
            weekyearComparator.compare(dateIn1998, dateInNextWeekyear)
        );

        // Test case 3: Test the reverse comparison for symmetry.
        assertEquals(
            "A date in weekyear 1999 should be greater than a date in weekyear 1998",
            1,
            weekyearComparator.compare(dateInNextWeekyear, dateIn1998)
        );
    }
}