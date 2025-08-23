package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DateTimeComparator}.
 * This test focuses on the comparison of two identical DateTime objects.
 */
class DateTimeComparatorTest {

    /**
     * Provides a stream of different DateTimeComparator instances for parameterized tests.
     * Each argument consists of a descriptive name and the corresponding comparator instance.
     *
     * @return A stream of arguments for the test.
     */
    private static Stream<Arguments> comparatorProvider() {
        return Stream.of(
            Arguments.of("Default Comparator", DateTimeComparator.getInstance()),
            Arguments.of("Second-based upper limit", DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute())),
            Arguments.of("Second-of-Minute Comparator", DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour())),
            Arguments.of("Minute-of-Hour Comparator", DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay())),
            Arguments.of("Hour-of-Day Comparator", DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear())),
            Arguments.of("Day-of-Week Comparator", DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear())),
            Arguments.of("Day-of-Month Comparator", DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear())),
            Arguments.of("Day-of-Year Comparator", DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year())),
            Arguments.of("Week-of-Weekyear Comparator", DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear())),
            Arguments.of("Weekyear Comparator", DateTimeComparator.getInstance(DateTimeFieldType.weekyear())),
            Arguments.of("Month-of-Year Comparator", DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year())),
            Arguments.of("Year Comparator", DateTimeComparator.getInstance(DateTimeFieldType.year())),
            Arguments.of("Date-Only Comparator", DateTimeComparator.getDateOnlyInstance()),
            Arguments.of("Time-Only Comparator", DateTimeComparator.getTimeOnlyInstance())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("comparatorProvider")
    @DisplayName("Compare two identical DateTime instances should return 0")
    void whenComparingEqualDateTimes_shouldReturnZero(String comparatorName, Comparator<Object> comparator) {
        // Arrange
        long now = System.currentTimeMillis();
        DateTime dateTime1 = new DateTime(now, DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime(now, DateTimeZone.UTC);

        // Act
        int result = comparator.compare(dateTime1, dateTime2);

        // Assert
        assertEquals(0, result, "Comparator should return 0 for identical DateTime objects.");
    }

    /*
     * The following helper methods were part of the original test class but are not used
     * in the refactored test above. They have been improved for clarity and correctness
     * and are kept here for potential future use.
     */

    /**
     * Creates a DateTime object from an ISO 8601 string representation in the UTC time zone.
     *
     * @param isoDateTime The date-time string.
     * @return The corresponding DateTime object.
     */
    private static DateTime createUtcDateTime(String isoDateTime) {
        return new DateTime(isoDateTime, DateTimeZone.UTC);
    }

    /**
     * Creates a list of DateTime objects from an array of ISO 8601 string representations.
     *
     * @param isoDateTimes The array of date-time strings.
     * @return A new list containing the corresponding DateTime objects.
     */
    private static List<DateTime> createUtcDateTimeList(String... isoDateTimes) {
        List<DateTime> dateTimeList = new ArrayList<>();
        for (String dtString : isoDateTimes) {
            dateTimeList.add(createUtcDateTime(dtString));
        }
        return dateTimeList;
    }

    /**
     * Checks if a list of DateTime objects is sorted in chronological order.
     *
     * @param dateTimes The list of DateTime objects to check.
     * @return true if the list is sorted chronologically, false otherwise.
     */
    private static boolean isChronologicallySorted(List<DateTime> dateTimes) {
        if (dateTimes == null || dateTimes.size() <= 1) {
            return true;
        }

        for (int i = 0; i < dateTimes.size() - 1; i++) {
            if (dateTimes.get(i).isAfter(dateTimes.get(i + 1))) {
                return false;
            }
        }
        return true;
    }
}