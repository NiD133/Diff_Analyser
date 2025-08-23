package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Test cases for the multipliedBy(int) method of the Weeks class.
 */
public class WeeksTest {

    /**
     * Tests that multiplying by a positive integer returns a new Weeks instance
     * with the correctly calculated number of weeks.
     */
    @Test
    public void multipliedBy_withPositiveScalar_returnsCorrectlyMultipliedWeeks() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final int scalar = 3;
        final Weeks expected = Weeks.weeks(6);

        // Act
        final Weeks result = twoWeeks.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    /**
     * Tests that multiplying by a negative integer returns a new Weeks instance
     * with the correctly calculated negative number of weeks.
     */
    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectlyMultipliedWeeks() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final int scalar = -3;
        final Weeks expected = Weeks.weeks(-6);

        // Act
        final Weeks result = twoWeeks.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    /**
     * Tests that the multipliedBy method does not change the original Weeks instance,
     * verifying its immutability.
     */
    @Test
    public void multipliedBy_doesNotChangeOriginalInstance() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final int originalValue = twoWeeks.getWeeks();

        // Act
        twoWeeks.multipliedBy(3);

        // Assert
        assertEquals("Original Weeks object should be immutable", originalValue, twoWeeks.getWeeks());
    }

    /**
     * Tests the optimization where multiplying by 1 returns the same instance,
     * avoiding unnecessary object creation.
     */
    @Test
    public void multipliedBy_one_returnsSameInstance() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);

        // Act
        final Weeks result = twoWeeks.multipliedBy(1);

        // Assert
        assertSame("Multiplying by 1 should return the same instance", twoWeeks, result);
    }

    /**
     * Tests that an ArithmeticException is thrown when the multiplication result
     * exceeds the maximum value an integer can hold.
     */
    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange
        final Weeks largeWeeks = Weeks.weeks(Integer.MAX_VALUE / 2 + 1);

        // Act
        // This operation should throw an ArithmeticException due to integer overflow.
        largeWeeks.multipliedBy(2);
    }
}