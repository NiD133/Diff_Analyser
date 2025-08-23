package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the IllegalFieldValueException class.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor accepting a DurationFieldType handles null
     * values for the illegal value, lower bound, and upper bound without crashing.
     * It also verifies that the exception's internal state is set correctly.
     */
    @Test
    public void constructorWithDurationFieldAndNullNumberArgsShouldSucceed() {
        // Arrange
        DurationFieldType fieldType = DurationFieldType.seconds();
        Number illegalValue = null;
        Number lowerBound = null;
        Number upperBound = null;

        // Act: Create the exception instance. The primary test is that this does not throw
        // an unexpected error (like a NullPointerException).
        IllegalFieldValueException exception = new IllegalFieldValueException(
                fieldType, illegalValue, lowerBound, upperBound);

        // Assert: Verify that the properties of the created exception are set as expected.
        assertEquals("The field type should be stored correctly.", fieldType, exception.getDurationFieldType());
        assertNull("The DateTimeFieldType should be null for this constructor.", exception.getDateTimeFieldType());
        assertEquals("The field name should be derived from the field type.", "seconds", exception.getFieldName());

        assertNull("The illegal number value should be null as provided.", exception.getIllegalNumberValue());
        assertNull("The lower bound should be null as provided.", exception.getLowerBound());
        assertNull("The upper bound should be null as provided.", exception.getUpperBound());

        // Also, verify the generated message is as expected.
        String expectedMessage = "Value null for seconds is not supported";
        assertEquals("The exception message was not formatted as expected.", expectedMessage, exception.getMessage());
    }
}