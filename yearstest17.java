package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the minus(Years) method of the {@link Years} class.
 */
public class YearsMinusTest {

    @Test
    public void minus_whenSubtractingYears_thenReturnsCorrectResult() {
        // Arrange
        Years twoYears = Years.years(2);
        Years threeYears = Years.years(3);
        Years expected = Years.years(-1);

        // Act
        Years actual = twoYears.minus(threeYears);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void minus_whenCalled_thenOriginalInstancesAreImmutable() {
        // Arrange
        Years initialTwoYears = Years.years(2);
        Years threeYears = Years.years(3);

        // Act
        initialTwoYears.minus(threeYears);

        // Assert
        assertEquals("The original Years object should not be modified.", 2, initialTwoYears.getYears());
        assertEquals("The Years object passed as an argument should not be modified.", 3, threeYears.getYears());
    }

    @Test
    public void minus_whenSubtractingNull_thenTreatsNullAsZero() {
        // Arrange
        Years oneYear = Years.ONE;

        // Act
        Years result = oneYear.minus((Years) null);

        // Assert
        assertEquals("Subtracting null should be equivalent to subtracting zero.", oneYear, result);
    }

    @Test
    public void minus_whenSubtractingZero_thenReturnsSameValue() {
        // Arrange
        Years oneYear = Years.ONE;

        // Act
        Years result = oneYear.minus(Years.ZERO);

        // Assert
        assertEquals("Subtracting zero should not change the value.", oneYear, result);
    }

    @Test(expected = ArithmeticException.class)
    public void minus_whenResultUnderflows_thenThrowsArithmeticException() {
        // Act: Attempt to subtract 1 from the minimum possible value, which will cause an underflow.
        Years.MIN_VALUE.minus(Years.ONE);
    }
}