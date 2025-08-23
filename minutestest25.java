package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test cases for the {@link Minutes#dividedBy(int)} method.
 */
public class MinutesDividedByTest {

    @Test
    public void dividedBy_withValidDivisor_returnsCorrectlyCalculatedMinutes() {
        // Arrange
        final Minutes twelveMinutes = Minutes.minutes(12);

        // Act & Assert
        assertEquals(Minutes.minutes(6), twelveMinutes.dividedBy(2));
        assertEquals(Minutes.minutes(4), twelveMinutes.dividedBy(3));
        assertEquals(Minutes.minutes(3), twelveMinutes.dividedBy(4));
        assertEquals(Minutes.minutes(2), twelveMinutes.dividedBy(6));
    }

    @Test
    public void dividedBy_withUnevenDivisor_truncatesResult() {
        // Arrange
        final Minutes twelveMinutes = Minutes.minutes(12);

        // Act
        Minutes result = twelveMinutes.dividedBy(5);

        // Assert
        // 12 / 5 = 2.4, which truncates to 2
        assertEquals("Division should use integer division, truncating the result.",
                Minutes.minutes(2), result);
    }

    @Test
    public void dividedBy_isAnImmutableOperation() {
        // Arrange
        final Minutes originalMinutes = Minutes.minutes(12);
        final Minutes expectedOriginalMinutes = Minutes.minutes(12);

        // Act
        originalMinutes.dividedBy(2);

        // Assert
        assertEquals("The original Minutes object should not be modified.",
                expectedOriginalMinutes, originalMinutes);
    }

    @Test
    public void dividedBy_one_returnsSameInstance() {
        // Arrange
        final Minutes testMinutes = Minutes.minutes(12);

        // Act
        Minutes result = testMinutes.dividedBy(1);

        // Assert
        assertSame("Dividing by 1 should return the same instance.", testMinutes, result);
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_throwsArithmeticException() {
        // Arrange
        Minutes oneMinute = Minutes.ONE;

        // Act
        oneMinute.dividedBy(0);
        // Assert: Handled by the 'expected' parameter of the @Test annotation.
    }
}