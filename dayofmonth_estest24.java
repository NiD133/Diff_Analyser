package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the isSupported() method in the DayOfMonth class.
 */
public class DayOfMonthImprovedTest {

    /**
     * Tests that isSupported() correctly identifies DAY_OF_MONTH as a supported field.
     */
    @Test
    public void isSupported_shouldReturnTrueForDayOfMonthField() {
        // Arrange: Create an instance of DayOfMonth. The specific value doesn't matter.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Check if the DAY_OF_MONTH field is supported.
        boolean isSupported = dayOfMonth.isSupported(ChronoField.DAY_OF_MONTH);

        // Assert: The field should be supported.
        assertTrue("DayOfMonth should support the DAY_OF_MONTH field.", isSupported);
    }

    /**
     * Tests that isSupported() correctly identifies an unsupported field like MONTH_OF_YEAR.
     */
    @Test
    public void isSupported_shouldReturnFalseForUnsupportedField() {
        // Arrange: Create an instance of DayOfMonth.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Check if an unsupported field, like MONTH_OF_YEAR, is supported.
        boolean isSupported = dayOfMonth.isSupported(ChronoField.MONTH_OF_YEAR);

        // Assert: The field should not be supported.
        assertFalse("DayOfMonth should not support the MONTH_OF_YEAR field.", isSupported);
    }
}