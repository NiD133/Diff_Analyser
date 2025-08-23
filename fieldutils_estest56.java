package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiplyToInt throws an ArithmeticException when the
     * product of two long values is less than Integer.MIN_VALUE, causing an overflow.
     */
    @Test
    public void safeMultiplyToInt_whenProductIsTooSmallForInt_throwsArithmeticException() {
        // Arrange: Define two long values whose product will not fit in an int.
        // The product is -6,863,357,739,008, which is less than Integer.MIN_VALUE.
        long multiplicand = 3196L;
        long multiplier = Integer.MIN_VALUE;

        try {
            // Act: Attempt the multiplication that is expected to overflow.
            FieldUtils.safeMultiplyToInt(multiplicand, multiplier);
            
            // Assert: If no exception is thrown, the test fails.
            fail("Expected an ArithmeticException due to integer overflow.");
        } catch (ArithmeticException e) {
            // Assert: Verify that the correct exception was thrown with a message
            // indicating the exact value that caused the overflow.
            long expectedProduct = multiplicand * multiplier;
            String expectedMessage = "Value cannot fit in an int: " + expectedProduct;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}