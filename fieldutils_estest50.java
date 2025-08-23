package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws an IllegalArgumentException when the provided
     * value is greater than the specified upper bound.
     */
    @Test
    public void verifyValueBounds_whenValueIsAboveUpperBound_throwsIllegalArgumentException() {
        // Arrange: Define the field type, the bounds, and a value outside those bounds.
        final DateTimeFieldType fieldType = DateTimeFieldType.minuteOfHour();
        final int lowerBound = -1584;
        final int upperBound = -473;
        final int valueAboveRange = 1428;

        // Define the expected exception message for clarity and maintainability.
        final String expectedMessage = "Value 1428 for minuteOfHour must be in the range [-1584,-473]";

        // Act & Assert: Call the method and verify that it throws the correct exception
        // with the expected message.
        try {
            FieldUtils.verifyValueBounds(fieldType, valueAboveRange, lowerBound, upperBound);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is exactly as expected.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}