package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Weeks} class, focusing on the negated() method.
 */
public class WeeksTest {

    /**
     * Tests that negating a standard Weeks value returns a new instance
     * with the opposite value, and that the original instance remains unchanged.
     */
    @Test
    public void negated_returnsNewInstanceWithOppositeValue() {
        // Arrange
        final Weeks originalWeeks = Weeks.weeks(12);
        final Weeks expectedNegatedWeeks = Weeks.weeks(-12);

        // Act
        final Weeks negatedWeeks = originalWeeks.negated();

        // Assert
        assertEquals("The negated value should be correct.", expectedNegatedWeeks, negatedWeeks);
        assertEquals("The original object should remain unchanged (immutable).", Weeks.weeks(12), originalWeeks);
    }

    /**
     * Tests that attempting to negate Weeks.MIN_VALUE throws an ArithmeticException,
     * as the result would overflow the integer range.
     */
    @Test(expected = ArithmeticException.class)
    public void negated_throwsExceptionWhenResultOverflows() {
        // Act: Negating MIN_VALUE causes an overflow because its positive counterpart
        // is larger than MAX_VALUE. This should throw an exception.
        Weeks.MIN_VALUE.negated();

        // Assert: The test will pass if an ArithmeticException is thrown.
        // If no exception is thrown, the test will fail automatically.
    }
    
    /**
     * This is an alternative way to test for exceptions using a try-catch block,
     * which can be more explicit for developers unfamiliar with the @Test(expected) annotation.
     * It achieves the same result as negated_throwsExceptionWhenResultOverflows().
     */
    @Test
    public void negated_throwsExceptionWhenResultOverflows_alternative() {
        try {
            Weeks.MIN_VALUE.negated();
            fail("Expected an ArithmeticException to be thrown for integer overflow.");
        } catch (ArithmeticException e) {
            // Expected exception was caught, test passes.
            // We can add further assertions on the exception if needed.
            assertEquals("Multiplication overflows an int: -2147483648", e.getMessage());
        }
    }
}