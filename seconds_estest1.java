package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that isLessThan() correctly handles a null argument by treating it as zero.
     */
    @Test
    public void isLessThan_whenComparingZeroToNull_returnsFalse() {
        // Arrange
        // The method's contract states that a null comparison is treated as comparing to zero.
        // This test verifies the case where the instance itself is also zero.
        Seconds zeroSeconds = Seconds.ZERO;

        // Act
        // The core logic being tested: is 0 less than null (which means 0)?
        boolean result = zeroSeconds.isLessThan(null);

        // Assert
        // The expected result is false, because 0 is not less than 0.
        assertFalse("Zero seconds should not be considered less than null (treated as zero).", result);
    }
}