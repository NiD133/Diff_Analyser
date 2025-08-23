package org.joda.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test cases for the {@link Seconds#isLessThan(Seconds)} method.
 */
public class SecondsIsLessThanTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenThisIsSmaller() {
        // Arrange
        Seconds two = Seconds.TWO;
        Seconds three = Seconds.THREE;

        // Act & Assert
        assertTrue("2 seconds should be less than 3 seconds", two.isLessThan(three));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenThisIsLarger() {
        // Arrange
        Seconds three = Seconds.THREE;
        Seconds two = Seconds.TWO;

        // Act & Assert
        assertFalse("3 seconds should not be less than 2 seconds", three.isLessThan(two));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenValuesAreEqual() {
        // Arrange
        Seconds three = Seconds.THREE;

        // Act & Assert
        assertFalse("3 seconds should not be less than 3 seconds", three.isLessThan(three));
    }

    @Test
    public void isLessThan_shouldReturnTrue_whenThisIsNegativeAndComparingToNull() {
        // Arrange: The Javadoc states that a null comparison is equivalent to comparing to zero.
        Seconds negativeOne = Seconds.seconds(-1);

        // Act & Assert: -1 is less than 0.
        assertTrue("-1 seconds should be less than null (0 seconds)", negativeOne.isLessThan(null));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenThisIsPositiveAndComparingToNull() {
        // Arrange: The Javadoc states that a null comparison is equivalent to comparing to zero.
        Seconds one = Seconds.ONE;

        // Act & Assert: 1 is not less than 0.
        assertFalse("1 second should not be less than null (0 seconds)", one.isLessThan(null));
    }
}