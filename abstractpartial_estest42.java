package org.joda.time.base;

import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the comparison logic in {@link AbstractPartial}.
 */
public class AbstractPartialTest {

    /**
     * Verifies that comparing two ReadablePartial objects with different sets of field types
     * throws a ClassCastException, as per the compareTo() contract.
     */
    @Test
    public void compareTo_withMismatchedFieldTypes_shouldThrowClassCastException() {
        // Arrange: Create two partials with incompatible field types.
        // A YearMonth consists of {year, monthOfYear}.
        ReadablePartial yearMonth = new YearMonth();
        // A LocalTime consists of {hourOfDay, minuteOfHour, ...}.
        ReadablePartial localTime = LocalTime.now();

        // Act & Assert
        try {
            yearMonth.compareTo(localTime);
            fail("Expected a ClassCastException because the partials have different field types.");
        } catch (ClassCastException e) {
            // This is the expected behavior.
            // For a more robust test, we also verify the exception message.
            assertEquals("ReadablePartial objects must have matching field types", e.getMessage());
        }
    }
}