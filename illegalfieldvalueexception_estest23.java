package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: The original test class name "IllegalFieldValueException_ESTestTest23"
// and its EvoSuite scaffolding have been removed for clarity and simplicity.
public class IllegalFieldValueExceptionTest {

    /**
     * Verifies that the exception message is correctly formatted when an
     * IllegalFieldValueException is constructed with a DateTimeFieldType,
     * an illegal numeric value, and its corresponding lower and upper bounds.
     */
    @Test
    public void testMessageFormattingForNumericValueWithBounds() {
        // Arrange
        final DateTimeFieldType fieldType = DateTimeFieldType.weekyear();
        final Long illegalValue = -42521587200000L;
        final Long lowerBound = -42521587200000L;
        final Long upperBound = -42521587200000L;

        // The expected message is dynamically constructed to make the test's
        // intent clear and to avoid errors from a long, hardcoded string.
        final String expectedMessage = String.format(
            "Value %d for %s must be in the range [%d,%d]",
            illegalValue, fieldType.getName(), lowerBound, upperBound);

        // Act
        final IllegalFieldValueException exception = new IllegalFieldValueException(
            fieldType, illegalValue, lowerBound, upperBound);
        final String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}