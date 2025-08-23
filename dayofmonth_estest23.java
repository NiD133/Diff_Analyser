package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the DayOfMonth class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class DayOfMonth_ESTestTest23 { // Retaining original class name for context

    /**
     * Tests that isSupported() returns false for a temporal field that is not
     * relevant to a day of the month, such as AMPM_OF_DAY.
     */
    @Test
    public void isSupported_shouldReturnFalse_forUnsupportedField() {
        // Arrange: Create a DayOfMonth instance and define a field it should not support.
        // Using DayOfMonth.of() makes the test more explicit and deterministic than DayOfMonth.now().
        DayOfMonth dayOfMonth = DayOfMonth.of(14);
        TemporalField unsupportedField = ChronoField.AMPM_OF_DAY;

        // Act: Call the method under test.
        boolean isSupported = dayOfMonth.isSupported(unsupportedField);

        // Assert: Verify that the field is correctly reported as not supported.
        assertFalse("DayOfMonth should not support the AMPM_OF_DAY field.", isSupported);
    }
}