package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test cases for the {@link Years} class, focusing on the plus(int) method.
 */
public class YearsTest {

    @Test
    public void plus_shouldAddNewYearsToAnInstance() {
        // Arrange
        final Years twoYears = Years.years(2);
        final int yearsToAdd = 3;
        final int expectedResult = 5;

        // Act
        final Years result = twoYears.plus(yearsToAdd);

        // Assert
        assertEquals(expectedResult, result.getYears());
    }

    @Test
    public void plus_shouldBeImmutable() {
        // Arrange
        final Years twoYears = Years.years(2);
        final int initialValue = 2;

        // Act
        twoYears.plus(3); // This operation should not change the original instance

        // Assert
        assertEquals("Original instance should not be modified (immutability)",
                     initialValue, twoYears.getYears());
    }

    @Test
    public void plus_shouldReturnSameInstanceWhenAddingZero() {
        // Arrange
        final Years oneYear = Years.ONE;

        // Act
        final Years result = oneYear.plus(0);

        // Assert
        assertSame("Adding zero should return the same instance, not a new one",
                   oneYear, result);
    }

    @Test(expected = ArithmeticException.class)
    public void plus_shouldThrowExceptionOnIntegerOverflow() {
        // Act
        Years.MAX_VALUE.plus(1);
        // Assert: The test passes if an ArithmeticException is thrown.
    }
}