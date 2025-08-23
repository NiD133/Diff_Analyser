package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

/**
 * Unit tests for {@link DateTimeComparator} focusing on comparisons
 * limited to the day-of-year field.
 *
 * <p>This test class clarifies and isolates the behavior originally tested in
 * the {@code testDOY} method of a larger test suite.
 */
public class DateTimeComparatorDayOfYearTest {

    /**
     * A comparator that only considers the day-of-year field.
     * It is configured to compare fields from {@code dayOfYear} up to (but not including) {@code year}.
     * This means the year, and any larger fields, are ignored in the comparison.
     */
    private static final Comparator<Object> DAY_OF_YEAR_COMPARATOR =
            DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    @Test
    public void compare_returnsCorrectSign_forDifferentDaysInSameYear() {
        // Arrange
        ReadableInstant april12 = new DateTime("2002-04-12T10:00:00", UTC); // Day 102
        ReadableInstant april13 = new DateTime("2002-04-13T08:00:00", UTC); // Day 103

        // Act & Assert
        // Test that april12 is "less than" april13
        assertTrue(
                "Comparator should return negative when first date's day-of-year is smaller",
                DAY_OF_YEAR_COMPARATOR.compare(april12, april13) < 0
        );

        // Test the symmetric case: april13 is "greater than" april12
        assertTrue(
                "Comparator should return positive when first date's day-of-year is larger",
                DAY_OF_YEAR_COMPARATOR.compare(april13, april12) > 0
        );
    }

    @Test
    public void compare_ignoresYearField_whenYearIsTheExclusiveUpperLimit() {
        // Arrange
        // dateOnFeb29 is day 60 of a leap year.
        ReadableInstant dateOnFeb29 = new DateTime("2000-02-29T00:00:00", UTC);
        // dateOnNov30 is day 334 of the year.
        ReadableInstant dateOnNov30 = new DateTime("1814-11-30T00:00:00", UTC);

        // Act & Assert
        // The comparison should be based on the day of the year (60 vs 334), ignoring the
        // absolute instant and the year (2000 vs 1814).

        // Test that day 60 is "less than" day 334
        assertTrue(
                "Comparison should be negative as day 60 is before day 334, ignoring the year",
                DAY_OF_YEAR_COMPARATOR.compare(dateOnFeb29, dateOnNov30) < 0
        );

        // Test the symmetric case: day 334 is "greater than" day 60
        assertTrue(
                "Comparison should be positive as day 334 is after day 60, ignoring the year",
                DAY_OF_YEAR_COMPARATOR.compare(dateOnNov30, dateOnFeb29) > 0
        );
    }

    @Test
    public void compare_returnsZero_forSameDayOfYearInDifferentYears() {
        // Arrange
        // Both dates are on April 12th, but in different years and at different times.
        ReadableInstant date1 = new DateTime("2000-04-12T10:00:00", UTC);
        ReadableInstant date2 = new DateTime("2008-04-12T20:00:00", UTC);

        // Act
        int result = DAY_OF_YEAR_COMPARATOR.compare(date1, date2);

        // Assert
        assertEquals(
                "Comparator should return zero for the same day-of-year, ignoring other fields",
                0,
                result
        );
    }
}