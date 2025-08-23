package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeToInt throws an ArithmeticException if the long value
     * is smaller than Integer.MIN_VALUE.
     */
    @Test
    public void safeToInt_throwsException_whenValueIsLessThanIntegerMinValue() {
        // Arrange: A long value that is just outside the lower bound of an integer.
        final long valueTooSmall = (long) Integer.MIN_VALUE - 1;

        // Act & Assert
        try {
            FieldUtils.safeToInt(valueTooSmall);
            fail("Expected an ArithmeticException because the value is too small to fit in an int.");
        } catch (ArithmeticException e) {
            // Verify that the exception message is informative and correct.
            String expectedMessage = "Value cannot fit in an int: " + valueTooSmall;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}