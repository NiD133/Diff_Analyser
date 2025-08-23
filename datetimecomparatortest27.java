package org.joda.time;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DateTimeComparator}.
 * This suite focuses on verifying the sorting behavior of different standard comparators.
 */
public class DateTimeComparatorTest {

    // Note: Using "Z" at the end of the date-time string specifies the UTC time zone.
    private static final DateTime DATE_1998_01_20 = new DateTime("1998-01-20T10:00:00Z");
    private static final DateTime DATE_1999_02_01 = new DateTime("1999-02-01T10:00:00Z");

    /**
     * Tests the default comparator, which should sort chronologically based on the full date and time.
     * This is equivalent to the natural ordering of DateTime objects.
     */
    @Test
    public void sortUsingDefaultComparator_sortsByChronology() {
        // Arrange
        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(DATE_1999_02_01, DATE_1998_01_20));
        Comparator<Object> defaultComparator = DateTimeComparator.getInstance();

        // Act
        dateTimes.sort(defaultComparator);

        // Assert
        List<DateTime> expectedOrder = Arrays.asList(DATE_1998_01_20, DATE_1999_02_01);
        assertEquals(expectedOrder, dateTimes);
    }

    /**
     * Tests the date-only comparator, which should ignore the time part of a DateTime.
     * Two DateTimes on the same day but with different times should be considered equal.
     */
    @Test
    public void sortUsingDateOnlyComparator_ignoresTime() {
        // Arrange
        DateTime sameDayEarlierTime = new DateTime("2022-06-09T10:00:00Z");
        DateTime sameDayLaterTime = new DateTime("2022-06-09T15:00:00Z");
        DateTime nextDay = new DateTime("2022-06-10T12:00:00Z");

        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(nextDay, sameDayLaterTime, sameDayEarlierTime));
        Comparator<Object> dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        // Act
        dateTimes.sort(dateOnlyComparator);

        // Assert
        // The two dates on June 9th are considered equal, so their relative order is preserved by the stable sort.
        List<DateTime> expectedOrder = Arrays.asList(sameDayLaterTime, sameDayEarlierTime, nextDay);
        assertEquals(expectedOrder, dateTimes);

        // Also, explicitly verify the comparison logic
        assertEquals("Dates on the same day should be equal", 0, dateOnlyComparator.compare(sameDayEarlierTime, sameDayLaterTime));
        assertTrue("A date should be less than the next day's date", dateOnlyComparator.compare(sameDayEarlierTime, nextDay) < 0);
    }

    /**
     * Tests the time-only comparator, which should ignore the date part of a DateTime.
     * Two DateTimes with the same time of day but on different dates should be considered equal.
     */
    @Test
    public void sortUsingTimeOnlyComparator_ignoresDate() {
        // Arrange
        DateTime sameTimeEarlierDate = new DateTime("2022-06-09T10:00:00Z");
        DateTime sameTimeLaterDate = new DateTime("2023-08-20T10:00:00Z");
        DateTime laterTime = new DateTime("2022-01-01T11:00:00Z");

        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(laterTime, sameTimeLaterDate, sameTimeEarlierDate));
        Comparator<Object> timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act
        dateTimes.sort(timeOnlyComparator);

        // Assert
        // The two dates at 10:00 are considered equal, so their relative order is preserved by the stable sort.
        List<DateTime> expectedOrder = Arrays.asList(sameTimeLaterDate, sameTimeEarlierDate, laterTime);
        assertEquals(expectedOrder, dateTimes);

        // Also, explicitly verify the comparison logic
        assertEquals("Times that are the same should be equal, regardless of date", 0, timeOnlyComparator.compare(sameTimeEarlierDate, sameTimeLaterDate));
        assertTrue("10:00 should be less than 11:00", timeOnlyComparator.compare(sameTimeEarlierDate, laterTime) < 0);
    }
}