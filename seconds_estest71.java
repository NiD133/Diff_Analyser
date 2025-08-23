package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Verifies that calling standardSecondsIn with a zero-length period
     * returns a Seconds object representing zero.
     */
    @Test
    public void standardSecondsIn_withZeroPeriod_returnsZeroSeconds() {
        // Arrange: Create a period representing zero seconds.
        ReadablePeriod zeroPeriod = Seconds.ZERO;

        // Act: Convert the period to a Seconds instance.
        Seconds result = Seconds.standardSecondsIn(zeroPeriod);

        // Assert: The result should be the constant for zero seconds.
        assertEquals(Seconds.ZERO, result);
    }
}