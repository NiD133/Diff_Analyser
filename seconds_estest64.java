package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the duration between two identical instants is zero seconds.
     */
    @Test
    public void secondsBetween_shouldReturnZero_whenInstantsAreTheSame() {
        // Arrange: Create a single point in time.
        Instant now = new Instant();

        // Act: Calculate the seconds between the instant and itself.
        Seconds result = Seconds.secondsBetween(now, now);

        // Assert: The result should be zero seconds.
        assertEquals(Seconds.ZERO, result);
    }
}