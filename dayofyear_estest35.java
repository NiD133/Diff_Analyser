package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original test class name is preserved as it might be part of a larger, generated test suite.
public class DayOfYear_ESTestTest35 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that calling getLong() with a field that is not supported by DayOfYear
     * throws an UnsupportedTemporalTypeException.
     *
     * According to the documentation, DayOfYear only supports the DAY_OF_YEAR field.
     * This test uses EPOCH_DAY as an example of an unsupported field.
     */
    @Test
    public void getLong_withUnsupportedField_throwsException() {
        // Arrange: Create a sample DayOfYear and select an unsupported field.
        // The specific day (e.g., 150) is not important for this test's outcome.
        DayOfYear dayOfYear = DayOfYear.of(150);
        ChronoField unsupportedField = ChronoField.EPOCH_DAY;

        // Act & Assert: Verify that calling getLong() with the unsupported field
        // throws the correct exception with the expected message.
        try {
            dayOfYear.getLong(unsupportedField);
            fail("Expected an UnsupportedTemporalTypeException to be thrown, but it wasn't.");
        } catch (UnsupportedTemporalTypeException e) {
            // Verify the exception message correctly identifies the unsupported field.
            assertEquals("Unsupported field: EpochDay", e.getMessage());
        }
    }
}