package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

/**
 * Tests for {@link DateTimeComparator} when comparing by the year field.
 */
public class DateTimeComparatorYearTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    @Test
    public void yearComparator_shouldCompareBasedOnYearOnly() {
        // Arrange: Create a comparator that only considers the year field.
        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());

        // Test data for various scenarios
        DateTime startOf2023 = new DateTime("2023-01-01T00:00:00", UTC);
        DateTime endOf2023 = new DateTime("2023-12-31T23:59:59", UTC);
        DateTime startOf2024 = new DateTime("2024-01-01T00:00:00", UTC);

        // Act & Assert

        // 1. Test for equality: Dates within the same year should be considered equal.
        assertEquals(
            "Comparison of two dates in the same year should be 0",
            0,
            yearComparator.compare(startOf2023, endOf2023)
        );

        // 2. Test for less-than: A date in an earlier year should be less than one in a later year.
        assertTrue(
            "A 2023 date should be less than a 2024 date",
            yearComparator.compare(startOf2023, startOf2024) < 0
        );

        // 3. Test for greater-than: A date in a later year should be greater than one in an earlier year.
        assertTrue(
            "A 2024 date should be greater than a 2023 date",
            yearComparator.compare(startOf2024, startOf2023) > 0
        );

        // 4. Test for reflexivity: Comparing an object with itself should result in 0.
        assertEquals(
            "Comparing an instance to itself should return 0",
            0,
            yearComparator.compare(startOf2023, startOf2023)
        );
    }
}