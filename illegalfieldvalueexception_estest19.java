package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * This test verifies that the constructor correctly builds the exception message
     * when the illegal value is null and only an upper bound is provided.
     * It also checks that the illegal value can be retrieved correctly.
     */
    @Test
    public void testConstructorWithNullValueAndUpperBoundGeneratesCorrectMessage() {
        // Arrange
        final String fieldName = "monthOfYear";
        final Long upperBound = 12L;
        final String expectedMessage = "Value null for monthOfYear must not be larger than 12";

        // Act
        // Create an exception for a null value, with no lower bound but a specified upper bound.
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, null, null, upperBound);

        // Assert
        assertEquals("The exception message should be correctly formatted.",
                expectedMessage, exception.getMessage());
        assertNull("The illegal number value should be null as provided.",
                exception.getIllegalNumberValue());
    }
}