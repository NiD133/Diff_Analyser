package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that converting the NTFS epoch time (0L) to an Instant and back
     * results in the original value. This verifies the round-trip conversion is
     * lossless for this fundamental boundary value.
     */
    @Test
    public void ntfsEpochTimeShouldRoundTripViaInstant() {
        // Arrange: The NTFS epoch is 1601-01-01T00:00:00Z, represented by the value 0.
        final long ntfsEpochTime = 0L;

        // Act: Convert the NTFS time to an Instant and then back to a long.
        final Instant instantRepresentation = FileTimes.ntfsTimeToInstant(ntfsEpochTime);
        final long roundTripNtfsTime = FileTimes.toNtfsTime(instantRepresentation);

        // Assert: The value after the round-trip conversion should be identical to the original.
        assertEquals(ntfsEpochTime, roundTripNtfsTime);
    }
}