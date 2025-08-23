package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that creating a UtcInstant from a java.time.Instant that represents
     * the Modified Julian Day (MJD) epoch (1858-11-17T00:00:00Z) correctly
     * results in a nano-of-day value of 0.
     */
    @Test
    public void of_instantAtMjdEpoch_hasZeroNanoOfDay() {
        // Arrange: The MJD epoch as a standard java.time.Instant.
        // This is the reference point for the UtcInstant's internal calendar system.
        Instant mjdEpochStartInstant = Instant.parse("1858-11-17T00:00:00Z");

        // Act: Create a UtcInstant from the epoch Instant.
        UtcInstant utcInstant = UtcInstant.of(mjdEpochStartInstant);

        // Assert: The nano-of-day should be 0 at the very start of the epoch day.
        assertEquals("Nano-of-day at MJD epoch start should be 0", 0L, utcInstant.getNanoOfDay());
    }
}