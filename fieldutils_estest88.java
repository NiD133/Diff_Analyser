package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiplyToInt throws an ArithmeticException if the intermediate
     * multiplication of the two long arguments overflows, which occurs before
     * the result is cast to an int.
     */
    @Test
    public void safeMultiplyToInt_shouldThrowException_whenIntermediateMultiplicationOverflowsLong() {
        // Arrange: Define two long values whose product will exceed Long.MAX_VALUE.
        // The square root of Long.MAX_VALUE is approximately 3,037,000,499.
        // We choose a number slightly larger than that to ensure its square causes an overflow.
        final long valueJustOverSqrtOfMaxLong = 3037000500L;

        try {
            // Act: Attempt to multiply the two large numbers.
            FieldUtils.safeMultiplyToInt(valueJustOverSqrtOfMaxLong, valueJustOverSqrtOfMaxLong);
            fail("Expected an ArithmeticException due to long overflow, but none was thrown.");
        } catch (ArithmeticException e) {
            // Assert: Verify that the correct exception was thrown with a descriptive message
            // confirming that the overflow happened during the long multiplication step.
            String expectedMessage = "Multiplication overflows a long: "
                + valueJustOverSqrtOfMaxLong + " * " + valueJustOverSqrtOfMaxLong;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}