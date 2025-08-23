package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the comparison logic in {@link AbstractPartial}, specifically the isAfter() method.
 */
public class AbstractPartialComparisonTest {

    /**
     * Verifies that comparing two ReadablePartial objects with different sets of fields
     * throws a ClassCastException, as they are not directly comparable.
     */
    @Test
    public void isAfter_whenComparingPartialsWithMismatchedFieldTypes_throwsClassCastException() {
        // Arrange: Create two partials with incompatible field types.
        // LocalDateTime has fields for date and time (year, month, day, time-of-day).
        // YearMonth only has fields for year and month.
        LocalDateTime localDateTime = new LocalDateTime();
        YearMonth yearMonth = new YearMonth();

        // Act & Assert: Attempting to compare these incompatible types should throw an exception.
        try {
            localDateTime.isAfter(yearMonth);
            fail("Expected a ClassCastException because the partials have different field types.");
        } catch (ClassCastException e) {
            // Verify that the exception message clearly states the reason for the failure.
            assertEquals("ReadablePartial objects must have matching field types", e.getMessage());
        }
    }
}