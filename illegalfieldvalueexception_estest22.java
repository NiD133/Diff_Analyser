package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the exception message is correctly formatted when constructed with a
     * null numeric value and an empty field name.
     */
    @Test
    public void testConstructorWithNullNumericValueAndEmptyFieldName() {
        // Arrange
        String fieldName = "";
        Number illegalValue = null;
        String expectedMessage = "Value null for  is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue, null, null);

        // Assert
        assertEquals("The exception message should be correctly formatted.", expectedMessage, exception.getMessage());
        
        // This constructor is for numeric values, so the string value should be null.
        assertNull("The illegal string value should be null.", exception.getIllegalStringValue());
        assertNull("The illegal number value was null.", exception.getIllegalNumberValue());
    }
}