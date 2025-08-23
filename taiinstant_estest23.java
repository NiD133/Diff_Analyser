package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that creating a TaiInstant with zero seconds and zero nanoseconds
     * correctly represents the TAI epoch.
     */
    @Test
    public void ofTaiSeconds_withZeroValues_createsEpochInstant() {
        // Arrange: Define the TAI epoch values.
        long taiSeconds = 0L;
        long nanoAdjustment = 0L;

        // Act: Create the TaiInstant instance using the factory method.
        TaiInstant epochInstant = TaiInstant.ofTaiSeconds(taiSeconds, nanoAdjustment);

        // Assert: Verify that both the seconds and nanoseconds match the epoch values.
        assertEquals("The TAI seconds should be zero for the epoch.", 0L, epochInstant.getTaiSeconds());
        assertEquals("The nanoseconds should be zero for the epoch.", 0, epochInstant.getNano());
    }
}