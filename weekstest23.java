package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#minus(Weeks)} method.
 */
public class WeeksTest {

    @Test
    public void minus_shouldCorrectlySubtractPositiveWeeks() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final Weeks threeWeeks = Weeks.weeks(3);

        // Act
        final Weeks result = twoWeeks.minus(threeWeeks);

        // Assert
        assertEquals("2 weeks - 3 weeks should be -1 weeks", -1, result.getWeeks());
    }

    @Test
    public void minus_shouldPreserveImmutability() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final Weeks threeWeeks = Weeks.weeks(3);

        // Act
        twoWeeks.minus(threeWeeks);

        // Assert that the original instances were not modified
        assertEquals("Original instance should remain unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Parameter instance should remain unchanged", 3, threeWeeks.getWeeks());
    }

    @Test
    public void minus_shouldReturnSameValueWhenSubtractingZero() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;
        
        // Act
        final Weeks result = oneWeek.minus(Weeks.ZERO);

        // Assert
        assertEquals("1 week - 0 weeks should be 1 week", 1, result.getWeeks());
    }

    @Test
    public void minus_shouldTreatNullAsZero() {
        // Arrange
        final Weeks oneWeek = Weeks.ONE;
        
        // Act
        // The method's contract specifies that a null input is treated as zero.
        final Weeks result = oneWeek.minus((Weeks) null);

        // Assert
        assertEquals("1 week - null should be equivalent to 1 week - 0", 1, result.getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_shouldThrowArithmeticExceptionOnUnderflow() {
        // Act: Attempting to subtract a positive value from MIN_VALUE will cause an integer underflow.
        Weeks.MIN_VALUE.minus(Weeks.ONE);
        
        // Assert: The test passes if an ArithmeticException is thrown, as declared by the @Test annotation.
    }
}