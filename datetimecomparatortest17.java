package org.joda.time;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link DateTimeComparator} focusing on comparisons limited to the second field.
 */
public class DateTimeComparatorTest {

    /**
     * Helper method to create a DateTime instance in the UTC zone from an ISO string.
     * @param isoDateTime The date-time string in ISO 8601 format.
     * @return A new DateTime object.
     */
    private DateTime createUtcDateTime(String isoDateTime) {
        return new DateTime(isoDateTime, DateTimeZone.UTC);
    }

    @Test
    public void compare_whenLimitedToSeconds_shouldIgnoreHigherFields() {
        // Arrange
        // This comparator is configured to consider fields from 'secondOfMinute' (inclusive)
        // up to 'minuteOfHour' (exclusive). Effectively, it only compares the 'secondOfMinute' field.
        Comparator<Object> secondOnlyComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.secondOfMinute(),
            DateTimeFieldType.minuteOfHour()
        );

        // Note: The date with the lower second value (58s) has a higher minute value (59m).
        // This setup specifically tests that the minute field is ignored.
        DateTime dateTimeWith58Seconds = createUtcDateTime("1969-12-31T23:59:58");
        DateTime dateTimeWith59Seconds = createUtcDateTime("1969-12-31T23:50:59");

        // Act & Assert
        // 1. Test that 58s is less than 59s
        assertTrue(
            "Comparison should be negative because 58s < 59s, ignoring that 59m > 50m",
            secondOnlyComparator.compare(dateTimeWith58Seconds, dateTimeWith59Seconds) < 0
        );

        // 2. Test the reverse: 59s is greater than 58s
        assertTrue(
            "Comparison should be positive because 59s > 58s, ignoring that 50m < 59m",
            secondOnlyComparator.compare(dateTimeWith59Seconds, dateTimeWith58Seconds) > 0
        );
    }

    @Test
    public void compare_whenLimitedToSeconds_shouldCorrectlyCompareDifferentSeconds() {
        // Arrange
        Comparator<Object> secondOnlyComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.secondOfMinute(),
            DateTimeFieldType.minuteOfHour()
        );

        DateTime dateTimeAt00Seconds = createUtcDateTime("1970-01-01T00:00:00");
        DateTime dateTimeAt01Seconds = createUtcDateTime("1970-01-01T00:00:01");

        // Act & Assert
        // 1. Test that 00s is less than 01s
        assertTrue(
            "Comparison should be negative for 00s vs 01s",
            secondOnlyComparator.compare(dateTimeAt00Seconds, dateTimeAt01Seconds) < 0
        );

        // 2. Test the reverse: 01s is greater than 00s
        assertTrue(
            "Comparison should be positive for 01s vs 00s",
            secondOnlyComparator.compare(dateTimeAt01Seconds, dateTimeAt00Seconds) > 0
        );
    }

    @Test
    public void compare_whenLimitedToSeconds_shouldReturnZeroForEqualSeconds() {
        // Arrange
        Comparator<Object> secondOnlyComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.secondOfMinute(),
            DateTimeFieldType.minuteOfHour()
        );

        // These dates differ in every field except for the 'secondOfMinute'.
        DateTime dateTime1 = createUtcDateTime("2023-10-27T10:30:15");
        DateTime dateTime2 = createUtcDateTime("1999-01-01T20:00:15");

        // Act & Assert
        assertEquals(
            "Comparison should be zero when seconds are equal, ignoring all other fields",
            0,
            secondOnlyComparator.compare(dateTime1, dateTime2)
        );
    }
}