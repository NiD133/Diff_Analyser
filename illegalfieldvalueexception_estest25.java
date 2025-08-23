package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link IllegalFieldValueException} class.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor using a String field name correctly initializes the exception's state,
     * particularly when the numeric value and bounds are null.
     */
    @Test
    public void testConstructorWithStringFieldNameAndNullValues() {
        // Arrange
        String fieldName = "";
        String expectedMessage = "Value null for  is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, null, null, null);

        // Assert
        // Verify the state of the created exception object
        assertEquals("The exception message should be correctly formatted.", expectedMessage, exception.getMessage());
        assertEquals("The field name should be stored correctly.", fieldName, exception.getFieldName());

        // Verify that fields not set by this constructor are null
        assertNull("The illegal number value should be null.", exception.getIllegalNumberValue());
        assertNull("The illegal string value should be null.", exception.getIllegalStringValue());
        assertNull("The lower bound should be null.", exception.getLowerBound());
        assertNull("The upper bound should be null.", exception.getUpperBound());
        assertNull("The DateTimeFieldType should be null when constructed with a string field name.", exception.getDateTimeFieldType());
        assertNull("The DurationFieldType should be null when constructed with a string field name.", exception.getDurationFieldType());
    }
}