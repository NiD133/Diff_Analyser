package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link IllegalFieldValueException} class.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Verifies that the constructor accepting a String field name and Number values
     * handles all-null inputs correctly and generates the expected message.
     */
    @Test
    public void constructorWithStringFieldName_whenAllArgumentsAreNull_buildsCorrectMessage() {
        // Arrange: Define null inputs for the constructor.
        String fieldName = null;
        Number illegalValue = null;
        Number lowerBound = null;
        Number upperBound = null;

        // Act: Create the exception instance.
        IllegalFieldValueException exception = new IllegalFieldValueException(
                fieldName, illegalValue, lowerBound, upperBound);

        // Assert: Check that the exception state is as expected.
        String expectedMessage = "Value null for null is not supported";
        assertEquals(expectedMessage, exception.getMessage());
        assertNull("The field name should be null as provided to the constructor.", exception.getFieldName());
    }
}