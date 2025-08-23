package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that isGreaterThan() returns false when comparing zero seconds to a null value.
     * The method's contract specifies that a null input should be treated as a zero-value period.
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingZeroToNull() {
        // Arrange: According to the Javadoc, a null parameter is treated as zero.
        // This test verifies that comparing Seconds.ZERO to null is equivalent to 0 > 0.
        Seconds zeroSeconds = Seconds.ZERO;

        // Act: Call the method under test with a null argument.
        boolean result = zeroSeconds.isGreaterThan(null);

        // Assert: The result must be false, as zero is not greater than zero.
        assertFalse(result);
    }
}