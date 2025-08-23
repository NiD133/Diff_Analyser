package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests the constructor that accepts a field name and an illegal string value.
     * It verifies that the exception's message and all relevant properties are set correctly.
     */
    @Test
    public void constructorWithFieldNameAndStringValue_shouldSetPropertiesCorrectly() {
        // Arrange
        final String fieldName = "monthOfYear";
        final String illegalValue = "Janubrary"; // An obviously invalid month name

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue);

        // Assert
        // 1. Verify the exception message is formatted as expected.
        String expectedMessage = "Value \"Janubrary\" for monthOfYear is not supported";
        assertEquals(expectedMessage, exception.getMessage());

        // 2. Verify the state of the exception object.
        assertEquals("The field name should be stored.", fieldName, exception.getFieldName());
        assertEquals("The illegal string value should be stored.", illegalValue, exception.getIllegalStringValue());
        
        // 3. Verify that fields not set by this constructor are null.
        assertNull("The numeric value should be null for this constructor.", exception.getIllegalNumberValue());
        assertNull("The DateTimeFieldType should be null.", exception.getDateTimeFieldType());
        assertNull("The DurationFieldType should be null.", exception.getDurationFieldType());
        assertNull("The lower bound should be null.", exception.getLowerBound());
        assertNull("The upper bound should be null.", exception.getUpperBound());
    }
}