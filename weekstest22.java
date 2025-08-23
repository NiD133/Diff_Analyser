package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#minus(int)} method.
 */
public class WeeksTest {

    @Test
    public void minus_shouldSubtractValueCorrectly() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);

        // Act
        final Weeks result = twoWeeks.minus(3);

        // Assert
        final Weeks expected = Weeks.weeks(-1);
        assertEquals(expected, result);
    }

    @Test
    public void minus_shouldNotModifyOriginalObject() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);

        // Act
        twoWeeks.minus(3); // The result of the operation is ignored

        // Assert
        assertEquals("The original Weeks object should remain unchanged (be immutable).", 2, twoWeeks.getWeeks());
    }

    @Test
    public void minus_shouldReturnSameValueWhenSubtractingZero() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;

        // Act
        final Weeks result = oneWeek.minus(0);

        // Assert
        assertEquals(Weeks.ONE, result);
    }

    @Test(expected = ArithmeticException.class)
    public void minus_shouldThrowExceptionOnIntegerOverflow() {
        // Act: Attempting to subtract 1 from MIN_VALUE will cause an overflow.
        Weeks.MIN_VALUE.minus(1);

        // Assert: The test passes if an ArithmeticException is thrown.
    }
}