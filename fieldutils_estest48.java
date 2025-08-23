package org.joda.time.field;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws an exception when the lower bound is greater
     * than the upper bound. In this invalid-bound scenario, any value should be
     * considered out of range.
     */
    @Test
    public void verifyValueBounds_shouldThrowException_whenBoundsAreInverted() {
        // Arrange: Define a field name, a value, and inverted bounds where lower > upper.
        final String fieldName = "testField";
        final int valueToVerify = -1702;
        final int lowerBound = 1472;
        final int upperBound = 0;

        // Act & Assert
        try {
            FieldUtils.verifyValueBounds(fieldName, valueToVerify, lowerBound, upperBound);
            fail("Expected an IllegalFieldValueException because the validation bounds are inverted.");
        } catch (IllegalFieldValueException e) {
            // Verify that the exception message is correctly formatted and contains the details.
            String expectedMessage = "Value -1702 for testField must be in the range [1472,0]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}