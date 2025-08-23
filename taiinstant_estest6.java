package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link TaiInstant}.
 * This improved test focuses on clarity and correctness.
 */
public class TaiInstantTest {

    /**
     * Tests that calculating the duration from an instant to itself results in a zero duration.
     */
    @Test
    public void durationUntil_whenCalledWithSameInstant_returnsZeroDuration() {
        // Arrange: Create an instance of TaiInstant.
        // The TAI epoch (0 seconds, 0 nanoseconds) is a simple, clear choice.
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Calculate the duration from the instant to itself.
        Duration duration = instant.durationUntil(instant);

        // Assert: The duration between an instant and itself must be zero.
        assertEquals(Duration.ZERO, duration);
    }
}