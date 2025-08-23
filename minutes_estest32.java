package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that calling toStandardSeconds() on a Minutes instance with the
     * minimum possible value (Integer.MIN_VALUE) throws an ArithmeticException.
     * This is expected because the internal calculation to convert minutes to
     * seconds (Integer.MIN_VALUE * 60) results in an integer overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_withMinValue_throwsArithmeticExceptionForOverflow() {
        // Arrange
        Minutes minutesWithMinValue = Minutes.MIN_VALUE;

        // Act: This call is expected to throw an exception.
        minutesWithMinValue.toStandardSeconds();

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}