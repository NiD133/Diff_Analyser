package org.joda.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

/**
 * Tests for DateTimeComparator when comparing only the day of the week.
 */
public class DateTimeComparatorDayOfWeekTest {

    private Comparator<Object> dayOfWeekComparator;

    @Before
    public void setUp() {
        // Comparator that only considers the day of the week field.
        // It compares fields from dayOfWeek up to (but not including) weekOfWeekyear.
        dayOfWeekComparator = DateTimeComparator.getInstance(
                DateTimeFieldType.dayOfWeek(),
                DateTimeFieldType.weekOfWeekyear()
        );
    }

    /**
     * Creates a DateTime instance in the UTC time zone from a date string.
     */
    private DateTime createUtcDateTime(String dateString) {
        return new DateTime(dateString, DateTimeZone.UTC);
    }

    @Test
    public void compare_whenFirstDayIsEarlierInWeek_shouldReturnNegative() {
        // 2002-04-12 is a Friday
        DateTime friday = createUtcDateTime("2002-04-12T10:00:00Z");
        // 2002-04-13 is a Saturday
        DateTime saturday = createUtcDateTime("2002-04-13T12:00:00Z");

        int result = dayOfWeekComparator.compare(friday, saturday);

        assertTrue("Comparing Friday to Saturday should be less than zero", result < 0);
    }

    @Test
    public void compare_whenFirstDayIsLaterInWeek_shouldReturnPositive() {
        // 2002-04-12 is a Friday
        DateTime friday = createUtcDateTime("2002-04-12T10:00:00Z");
        // 2002-04-13 is a Saturday
        DateTime saturday = createUtcDateTime("2002-04-13T12:00:00Z");

        int result = dayOfWeekComparator.compare(saturday, friday);

        assertTrue("Comparing Saturday to Friday should be greater than zero", result > 0);
    }

    @Test
    public void compare_whenDaysOfWeekAreSame_shouldReturnZero() {
        // Both dates are Fridays, but in different weeks and with different times.
        DateTime firstFriday = createUtcDateTime("2002-04-12T10:00:00Z");
        DateTime secondFriday = createUtcDateTime("2002-04-19T12:00:00Z");

        int result = dayOfWeekComparator.compare(firstFriday, secondFriday);

        assertEquals("Comparing two different Fridays should be zero", 0, result);
    }
}