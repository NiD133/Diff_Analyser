package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.Duration;

/**
 * Unit tests for the TaiInstant class, focusing on the plus() method.
 */
public class TaiInstantTest {

    /**
     * Tests that adding a Duration to a TaiInstant correctly sums both the
     * seconds and the nanosecond components.
     */
    @Test
    public void plus_addsDurationWithSecondsAndNanos_correctly() {
        // Arrange: Define an initial instant and a duration to add.
        // Both have non-zero seconds and nanoseconds to ensure both parts are tested.
        TaiInstant startInstant = TaiInstant.ofTaiSeconds(1000L, 1000L);
        Duration durationToAdd = Duration.ofSeconds(1000L, 1000L);

        // Act: Add the duration to the starting instant.
        TaiInstant resultInstant = startInstant.plus(durationToAdd);

        // Assert: The resulting instant should equal an expected instant where
        // both seconds and nanoseconds have been summed.
        TaiInstant expectedInstant = TaiInstant.ofTaiSeconds(2000L, 2000L);
        assertEquals(expectedInstant, resultInstant);
    }
}