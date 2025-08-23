package org.joda.time.field;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A test suite for the {@link FieldUtils} class.
 * This improved version focuses on clarity and maintainability.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws an IllegalFieldValueException
     * when the provided value is greater than the allowed upper bound.
     */
    @Test
    public void verifyValueBounds_shouldThrowException_whenValueIsAboveUpperBound() {
        // Arrange: Define a realistic and easy-to-understand scenario for a field value check.
        // Here, we check for an invalid hour-of-day value, which is more intuitive
        // than the original test's abstract negative numbers.
        final String fieldName = "hourOfDay";
        final int valueAboveRange = 24;
        final int lowerBound = 0;
        final int upperBound = 23;
        
        final String expectedMessage = "Value 24 for hourOfDay must be in the range [0,23]";

        // Act & Assert: Verify that the correct exception is thrown with the expected message.
        try {
            FieldUtils.verifyValueBounds(fieldName, valueAboveRange, lowerBound, upperBound);
            fail("Expected an IllegalFieldValueException to be thrown, but no exception was thrown.");
        } catch (IllegalFieldValueException e) {
            // Verify that the exception message is informative and correct.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}