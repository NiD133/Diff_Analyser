package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Unit tests for the Seconds class, focusing on the dividedBy(int) method.
 */
public class SecondsTest {

    @Test
    public void dividedBy_withEvenDivisor_returnsCorrectResult() {
        // Given
        final Seconds twelveSeconds = Seconds.seconds(12);
        final Seconds expected = Seconds.seconds(6);

        // When
        Seconds actual = twelveSeconds.dividedBy(2);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void dividedBy_withUnevenDivisor_truncatesResult() {
        // Given
        final Seconds twelveSeconds = Seconds.seconds(12);
        // 12 / 5 = 2.4, which should be truncated to 2
        final Seconds expected = Seconds.seconds(2);

        // When
        Seconds actual = twelveSeconds.dividedBy(5);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void dividedBy_isImmutable() {
        // Given
        final Seconds originalSeconds = Seconds.seconds(12);
        final int originalValue = originalSeconds.getSeconds();

        // When
        originalSeconds.dividedBy(2);

        // Then
        // Assert that the original object was not modified
        assertEquals(originalValue, originalSeconds.getSeconds());
    }

    @Test
    public void dividedBy_one_returnsSameInstance() {
        // Given
        final Seconds twelveSeconds = Seconds.seconds(12);

        // When
        Seconds result = twelveSeconds.dividedBy(1);

        // Then
        // Dividing by 1 is a no-op and should return the same instance for efficiency
        assertSame(twelveSeconds, result);
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_throwsArithmeticException() {
        // When
        Seconds.ONE.dividedBy(0);
        // Then: an ArithmeticException is expected
    }
}