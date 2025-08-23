package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor taking two String arguments handles null inputs gracefully
     * and correctly initializes the exception's state.
     */
    @Test
    public void testConstructorWithStringArgsHandlesNulls() {
        // Arrange: Define the inputs for the constructor
        String fieldName = null;
        String illegalValue = null;

        // Act: Create the exception instance
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue);

        // Assert: Verify the state of the created exception object
        assertNull("The field name should be null as provided.", exception.getFieldName());
        assertNull("The illegal string value should be null as provided.", exception.getIllegalStringValue());

        // The message is part of the exception's public contract.
        // Based on the Joda-Time implementation, this is the expected message.
        assertEquals("Value null for null is not supported", exception.getMessage());

        // Verify that fields related to other constructors are null
        assertNull("The DateTimeFieldType should not be set.", exception.getDateTimeFieldType());
        assertNull("The DurationFieldType should not be set.", exception.getDurationFieldType());
        assertNull("The illegal number value should not be set.", exception.getIllegalNumberValue());
        assertNull("The lower bound should not be set.", exception.getLowerBound());
        assertNull("The upper bound should not be set.", exception.getUpperBound());
    }
}