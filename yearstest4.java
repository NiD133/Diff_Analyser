package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years#yearsBetween(ReadablePartial, ReadablePartial)} factory method.
 */
public class YearsTest {

    private static final LocalDate START_DATE = new LocalDate(2006, 6, 9);
    private static final LocalDate END_DATE_3_YEARS_LATER = new LocalDate(2009, 6, 9);

    @Test
    public void yearsBetween_shouldReturnPositiveYears_whenEndIsAfterStart() {
        // Arrange
        Years expected = Years.THREE;

        // Act
        Years actual = Years.yearsBetween(START_DATE, END_DATE_3_YEARS_LATER);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void yearsBetween_shouldReturnZeroYears_whenStartAndEndAreSame() {
        // Arrange
        Years expected = Years.ZERO;

        // Act
        Years actual = Years.yearsBetween(START_DATE, START_DATE);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void yearsBetween_shouldReturnNegativeYears_whenStartIsAfterEnd() {
        // Arrange
        Years expected = Years.years(-3);

        // Act
        // Note the reversed order of start and end dates
        Years actual = Years.yearsBetween(END_DATE_3_YEARS_LATER, START_DATE);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("deprecation") // Using deprecated YearMonthDay for test variety
    public void yearsBetween_shouldWorkWithDifferentReadablePartialTypes() {
        // Arrange
        LocalDate startDate = new LocalDate(2006, 6, 9);
        YearMonthDay endDate = new YearMonthDay(2012, 6, 9); // A different ReadablePartial type
        Years expected = Years.years(6);

        // Act
        Years actual = Years.yearsBetween(startDate, endDate);

        // Assert
        assertEquals(expected, actual);
    }
}