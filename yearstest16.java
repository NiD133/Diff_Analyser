package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the minus(int) method of the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void minus_shouldSubtractValueAndReturnNewInstance() {
        // Arrange
        final Years initialYears = Years.years(2);
        final int yearsToSubtract = 3;
        
        // Act
        final Years result = initialYears.minus(yearsToSubtract);
        
        // Assert
        // Check that the new instance has the correct subtracted value
        assertEquals("2 years - 3 years should be -1 years", -1, result.getYears());
        
        // Verify that the original instance is immutable and remains unchanged
        assertEquals("Original instance should not be mutated", 2, initialYears.getYears());
    }

    @Test
    public void minus_whenSubtractingZero_shouldReturnEqualInstance() {
        // Arrange
        final Years oneYear = Years.ONE;
        
        // Act
        final Years result = oneYear.minus(0);
        
        // Assert
        assertEquals("Subtracting zero should not change the value", 1, result.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_whenResultUnderflows_shouldThrowArithmeticException() {
        // Arrange
        final Years minYears = Years.MIN_VALUE;
        
        // Act: This operation should cause an integer underflow and throw an exception.
        minYears.minus(1);
        
        // Assert: The test passes if an ArithmeticException is thrown.
    }
}