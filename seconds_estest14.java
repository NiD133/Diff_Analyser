package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that standardSecondsIn() returns the ZERO constant when the input period is null.
     * The Javadoc for the method specifies that a null input should result in zero seconds.
     */
    @Test
    public void standardSecondsIn_whenPeriodIsNull_returnsZero() {
        // Act: Call the factory method with a null input.
        Seconds result = Seconds.standardSecondsIn(null);

        // Assert: The result should be the singleton instance of ZERO seconds.
        assertSame("standardSecondsIn(null) should return the singleton Seconds.ZERO", Seconds.ZERO, result);
    }
}