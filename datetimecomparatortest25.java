package org.joda.time;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link DateTimeComparator} specifically for month-based comparisons.
 */
public class DateTimeComparatorMonthTest {

    private Comparator<Object> monthOnlyComparator;

    @Before
    public void setUp() {
        // This comparator is configured to compare instants by the 'monthOfYear' field.
        // The upper limit 'year' is exclusive, so the year field is ignored in comparisons.
        monthOnlyComparator = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
    }

    @Test
    public void compare_byMonth_shouldReturnNegativeWhenFirstMonthIsBeforeSecond() {
        // Arrange
        DateTime april2002 = new DateTime("2002-04-30T00:00:00Z");
        DateTime may2002 = new DateTime("2002-05-01T00:00:00Z");

        // Act & Assert
        assertEquals("Comparison should be negative because April (4) is before May (5)",
                -1, monthOnlyComparator.compare(april2002, may2002));
    }

    @Test
    public void compare_byMonth_shouldReturnPositiveWhenFirstMonthIsAfterSecond() {
        // Arrange
        DateTime april2002 = new DateTime("2002-04-30T00:00:00Z");
        DateTime may2002 = new DateTime("2002-05-01T00:00:00Z");

        // Act & Assert
        assertEquals("Comparison should be positive because May (5) is after April (4)",
                1, monthOnlyComparator.compare(may2002, april2002));
    }

    @Test
    public void compare_byMonth_shouldIgnoreYearAndOnlyCompareMonthField() {
        // Arrange
        // The comparator only considers the month, so the year should not affect the result.
        DateTime january1900 = new DateTime("1900-01-01T00:00:00Z");
        DateTime december1899 = new DateTime("1899-12-31T00:00:00Z");

        // Act & Assert
        // The comparison is between January (1) and December (12).
        assertEquals("Comparison should be negative because January (1) is before December (12), ignoring the year",
                -1, monthOnlyComparator.compare(january1900, december1899));

        assertEquals("Comparison should be positive because December (12) is after January (1), ignoring the year",
                1, monthOnlyComparator.compare(december1899, january1900));
    }
}