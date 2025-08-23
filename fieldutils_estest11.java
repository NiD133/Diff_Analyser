package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeAdd(long, long) throws an ArithmeticException when the
     * addition results in a value less than Long.MIN_VALUE (underflow).
     */
    @Test
    public void safeAdd_shouldThrowArithmeticException_onLongUnderflow() {
        // Arrange: Define values that will cause an underflow when added.
        // Adding any negative number to Long.MIN_VALUE will result in an underflow.
        final long negativeValue = -610L;
        final long minLong = Long.MIN_VALUE;

        // Act & Assert: Verify that the operation throws the expected exception.
        try {
            FieldUtils.safeAdd(negativeValue, minLong);
            fail("Expected an ArithmeticException to be thrown due to long underflow.");
        } catch (ArithmeticException e) {
            // Verify the exception message to ensure the failure is for the correct reason.
            String expectedMessage = "The calculation caused an overflow: " + negativeValue + " + " + minLong;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}