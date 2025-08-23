package org.joda.time;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that multiplying Seconds.MIN_VALUE by a negative number
     * correctly throws an ArithmeticException due to integer overflow.
     */
    @Test
    public void multipliedBy_whenMultiplicationCausesOverflow_throwsArithmeticException() {
        // Arrange: The minimum possible Seconds value and a multiplier that will cause an overflow.
        final Seconds minValue = Seconds.MIN_VALUE; // -2,147,483,648 seconds
        final int multiplier = -2;
        final String expectedMessage = "Multiplication overflows an int: -2147483648 * -2";

        // Assert: Expect an ArithmeticException with a specific overflow message.
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage(expectedMessage);

        // Act: Perform the multiplication that is expected to fail.
        minValue.multipliedBy(multiplier);
    }
}