package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link IllegalFieldValueException} class.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests the constructor that accepts a DateTimeFieldType, null for numeric values,
     * and a custom explanation message.
     *
     * This test verifies that the constructor correctly initializes the exception's state
     * and generates the expected message without throwing any unexpected errors (like a
     * NullPointerException) when provided with null bounds and value.
     */
    @Test
    public void testConstructorWithDateTimeFieldTypeAndExplanation() {
        // Arrange: Define the inputs for the exception constructor.
        DateTimeFieldType fieldType = DateTimeFieldType.year();
        String explanation = "A custom explanation for the failure.";

        // Act: Create the exception using the constructor under test.
        // The constructor signature being tested is:
        // IllegalFieldValueException(DateTimeFieldType, Number value, Number lowerBound, Number upperBound, String explain)
        IllegalFieldValueException exception = new IllegalFieldValueException(
            fieldType,
            null, // The illegal value
            null, // The lower bound
            null, // The upper bound
            explanation
        );

        // Assert: Verify that the state of the created exception object is correct.
        assertEquals("The field type should be correctly stored.", fieldType, exception.getDateTimeFieldType());
        assertEquals("The field name should be derived from the field type.", "year", exception.getFieldName());
        assertNull("The illegal number value should be null.", exception.getIllegalNumberValue());
        assertNull("The lower bound should be null.", exception.getLowerBound());
        assertNull("The upper bound should be null.", exception.getUpperBound());

        // Also assert that fields related to other constructors are null.
        assertNull("The duration field type should be null.", exception.getDurationFieldType());
        assertNull("The illegal string value should be null.", exception.getIllegalStringValue());

        // Verify that the exception message is constructed as expected.
        String expectedMessage = "Value null for year is not supported: " + explanation;
        assertEquals("The exception message should be formatted correctly.", expectedMessage, exception.getMessage());
    }
}