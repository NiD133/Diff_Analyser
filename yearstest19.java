package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the dividedBy(int) method in the Years class.
 */
public class YearsTest {

    private static final Years TWELVE_YEARS = Years.years(12);

    @Test
    public void dividedBy_withEvenDivisor_returnsCorrectlyScaledYears() {
        // Arrange
        Years expected = Years.years(6);

        // Act
        Years actual = TWELVE_YEARS.dividedBy(2);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void dividedBy_withUnevenDivisor_truncatesResult() {
        // Arrange
        // 12 divided by 5 is 2.4, which should be truncated to 2.
        Years expected = Years.years(2);

        // Act
        Years actual = TWELVE_YEARS.dividedBy(5);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void dividedBy_isImmutable() {
        // Arrange
        int originalValue = TWELVE_YEARS.getYears();

        // Act
        // The division should not change the original object.
        TWELVE_YEARS.dividedBy(4);

        // Assert
        assertEquals(originalValue, TWELVE_YEARS.getYears());
    }

    @Test
    public void dividedBy_one_returnsSameInstance() {
        // Act
        Years result = TWELVE_YEARS.dividedBy(1);

        // Assert
        // Dividing by 1 is a no-op, so for immutable objects,
        // it should return the same instance.
        assertSame(TWELVE_YEARS, result);
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_throwsArithmeticException() {
        // Act
        Years.ONE.dividedBy(0);

        // Assert (handled by the @Test annotation)
    }
}