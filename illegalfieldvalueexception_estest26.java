package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    @Test
    public void testMessageIsCorrectlyFormattedForNumericValueAndEmptyFieldName() {
        // Arrange
        String fieldName = "";
        Number illegalValue = Byte.valueOf((byte) 0);
        Number lowerBound = Byte.valueOf((byte) 0);
        Number upperBound = Byte.valueOf((byte) 0);

        String expectedMessage = "Value 0 for  must be in the range [0,0]";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(
                fieldName, illegalValue, lowerBound, upperBound);

        // Assert
        assertEquals("The exception message should be formatted correctly.",
                expectedMessage, exception.getMessage());
    }
}