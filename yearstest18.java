package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test cases for the multipliedBy(int) method of the Years class.
 */
public class YearsTest {

    @Test
    public void multipliedBy_withPositiveScalar_returnsCorrectlyScaledYears() {
        // Arrange
        Years twoYears = Years.years(2);
        
        // Act
        Years sixYears = twoYears.multipliedBy(3);
        
        // Assert
        assertEquals(6, sixYears.getYears());
    }

    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectlyScaledYears() {
        // Arrange
        Years twoYears = Years.years(2);
        
        // Act
        Years negativeSixYears = twoYears.multipliedBy(-3);
        
        // Assert
        assertEquals(-6, negativeSixYears.getYears());
    }

    @Test
    public void multipliedBy_one_returnsSameInstance() {
        // Arrange
        Years twoYears = Years.years(2);
        
        // Act
        Years result = twoYears.multipliedBy(1);
        
        // Assert
        assertSame("Multiplying by 1 should return the same instance", twoYears, result);
    }

    @Test
    public void multipliedBy_doesNotChangeOriginalInstance() {
        // Arrange
        Years twoYears = Years.years(2);
        
        // Act
        twoYears.multipliedBy(3); // The result of the multiplication is ignored
        
        // Assert
        assertEquals("Original Years instance should be immutable", 2, twoYears.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange
        Years largeYears = Years.years(Integer.MAX_VALUE / 2 + 1);
        
        // Act
        largeYears.multipliedBy(2); // This should throw an exception
        
        // Assert (handled by the @Test(expected=...) annotation)
    }
}