package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that subtracting a negative number of years from {@code Years.MAX_VALUE}
     * correctly throws an {@code ArithmeticException} due to an integer overflow.
     * <p>
     * Subtracting a negative value is equivalent to adding a positive one.
     * Therefore, {@code Integer.MAX_VALUE - (-n)} becomes {@code Integer.MAX_VALUE + n},
     * which is an operation expected to overflow.
     */
    @Test
    public void minus_fromMaxValueWithNegativeYears_throwsArithmeticExceptionOnOverflow() {
        // Arrange: Set up the scenario for an integer overflow.
        Years maxYears = Years.MAX_VALUE;
        int negativeYearsToSubtract = -2133;

        // Act & Assert: Attempt the operation and verify the expected exception.
        try {
            maxYears.minus(negativeYearsToSubtract);
            fail("Expected an ArithmeticException to be thrown due to overflow, but it was not.");
        } catch (ArithmeticException e) {
            // Assert that the exception message is correct. The message "2147483647 + 2133"
            // indicates that the internal implementation correctly identified the overflow
            // when effectively performing an addition.
            final String expectedMessage = "The calculation caused an overflow: 2147483647 + 2133";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}