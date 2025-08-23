package org.joda.time;

import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for DateTimeComparator when configured to compare only the minute-of-hour field.
 *
 * This test class verifies that the comparator correctly identifies equality, less-than,
 * and greater-than relationships based solely on the minute field, while ignoring
 * all other date and time fields.
 */
public class DateTimeComparatorMinuteTest {

    private Comparator<Object> minuteComparator;

    @Before
    public void setUp() {
        // This comparator is configured to only compare the 'minuteOfHour' field.
        // The lower limit (minuteOfHour) is inclusive.
        // The upper limit (hourOfDay) is exclusive.
        minuteComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.minuteOfHour(),
            DateTimeFieldType.hourOfDay()
        );
    }

    @Test
    public void minuteComparator_returnsNegative_whenFirstMinuteIsLesser() {
        // Arrange
        DateTime dateTime1 = new DateTime("2023-10-27T10:30:00", DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime("2023-10-27T10:31:00", DateTimeZone.UTC);

        // Act
        int result = minuteComparator.compare(dateTime1, dateTime2);

        // Assert
        assertTrue("Comparing 30m to 31m should be negative.", result < 0);
    }

    @Test
    public void minuteComparator_returnsPositive_whenFirstMinuteIsGreater() {
        // Arrange
        DateTime dateTime1 = new DateTime("2023-10-27T10:31:00", DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime("2023-10-27T10:30:00", DateTimeZone.UTC);

        // Act
        int result = minuteComparator.compare(dateTime1, dateTime2);

        // Assert
        assertTrue("Comparing 31m to 30m should be positive.", result > 0);
    }

    @Test
    public void minuteComparator_returnsZero_whenMinutesAreEqual() {
        // Arrange
        DateTime dateTime1 = new DateTime("2023-10-27T10:30:00", DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime("2023-10-27T10:30:00", DateTimeZone.UTC);

        // Act
        int result = minuteComparator.compare(dateTime1, dateTime2);

        // Assert
        assertEquals("Comparing 30m to 30m should be zero.", 0, result);
    }

    @Test
    public void minuteComparator_returnsZero_whenMinutesAreEqualButSecondsDiffer() {
        // Arrange: Minutes are equal, but seconds (a smaller field) differ.
        DateTime dateTime1 = new DateTime("2023-10-27T10:30:15", DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime("2023-10-27T10:30:45", DateTimeZone.UTC);

        // Act
        int result = minuteComparator.compare(dateTime1, dateTime2);

        // Assert
        assertEquals("Comparator should ignore the seconds field.", 0, result);
    }

    @Test
    public void minuteComparator_returnsZero_whenMinutesAreEqualButLargerFieldsDiffer() {
        // Arrange: Minutes are equal, but hour, day, month, and year (larger fields) differ.
        DateTime dateTime1 = new DateTime("2022-01-01T08:30:00", DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime("2023-10-27T10:30:00", DateTimeZone.UTC);

        // Act
        int result = minuteComparator.compare(dateTime1, dateTime2);

        // Assert
        assertEquals("Comparator should ignore hour, day, month, and year fields.", 0, result);
    }
}