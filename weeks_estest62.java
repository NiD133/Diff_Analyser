package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that calling negated() on Weeks.MIN_VALUE throws an ArithmeticException.
     * This is the expected behavior because Integer.MIN_VALUE cannot be safely negated
     * to a positive integer due to two's complement representation, which would cause an overflow.
     */
    @Test
    public void negated_whenValueIsMinValue_throwsArithmeticException() {
        // Arrange: Create a Weeks instance with the minimum possible value.
        Weeks minWeeks = Weeks.MIN_VALUE;

        // Act & Assert: Attempt to negate the value and verify that the correct exception is thrown.
        try {
            minWeeks.negated();
            fail("Expected an ArithmeticException to be thrown, but no exception occurred.");
        } catch (ArithmeticException e) {
            // Verify that the exception message is as expected, confirming the reason for the failure.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}