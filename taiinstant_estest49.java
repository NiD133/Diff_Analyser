package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that isAfter() correctly returns false when comparing an earlier instant
     * to a later one.
     *
     * This test also implicitly verifies the normalization logic of the
     * ofTaiSeconds() factory method, where a nano-of-second adjustment equal
     * to a full second is correctly carried over to the seconds part.
     */
    @Test
    public void isAfter_shouldReturnFalse_whenComparingEarlierInstantToLaterOne() {
        // Arrange
        // An instant representing a point 50 seconds and 50 nanoseconds after the TAI epoch.
        TaiInstant laterInstant = TaiInstant.ofTaiSeconds(50L, 50L);

        // An instant that should normalize to the TAI epoch (0 seconds, 0 nanoseconds).
        // The factory method handles the nano adjustment overflowing into the seconds part:
        // -1 second + 1,000,000,000 nanoseconds results in 0 seconds.
        TaiInstant earlierInstant = TaiInstant.ofTaiSeconds(-1L, 1_000_000_000L);

        // To ensure the comparison is meaningful, first verify the state of our objects.
        assertEquals("Normalized seconds of earlierInstant should be 0", 0L, earlierInstant.getTaiSeconds());
        assertEquals("Normalized nanos of earlierInstant should be 0", 0, earlierInstant.getNano());
        assertEquals("Seconds of laterInstant should be 50", 50L, laterInstant.getTaiSeconds());
        assertEquals("Nanos of laterInstant should be 50", 50, laterInstant.getNano());

        // Act
        boolean isAfter = earlierInstant.isAfter(laterInstant);

        // Assert
        // The earlier instant (0s) should not be "after" the later instant (50.000000050s).
        assertFalse("An earlier instant should not be considered after a later one.", isAfter);
    }
}