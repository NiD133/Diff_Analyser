package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link IllegalFieldValueException} class, focusing on its constructors.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Verifies that the constructor taking a DateTimeFieldType and a String value
     * correctly stores the illegal value and generates the appropriate exception message.
     */
    @Test
    public void whenConstructedWithDateTimeFieldTypeAndStringValue_thenPropertiesAreSetCorrectly() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfMinute();
        String illegalValue = "";
        String expectedMessage = "Value \"\" for secondOfMinute is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        // Verify that the illegal value can be retrieved as a string
        assertEquals("The stored illegal value should match the one provided in the constructor.",
                illegalValue, exception.getIllegalValueAsString());

        // Verify that the exception message is formatted as expected
        assertEquals("The exception message should be correctly formatted.",
                expectedMessage, exception.getMessage());
    }
}