package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that converting an NTFS time to an Instant and back results in the original NTFS time value.
     * This is also known as a round-trip or inversion test.
     */
    @Test
    public void testNtfsTimeInstantRoundTripConversion() {
        // Arrange: Define an original NTFS time value.
        // Using -1L as a non-trivial test case.
        final long originalNtfsTime = -1L;

        // Act: Convert the NTFS time to an Instant, and then convert it back.
        final Instant intermediateInstant = FileTimes.ntfsTimeToInstant(originalNtfsTime);
        final long roundTripNtfsTime = FileTimes.toNtfsTime(intermediateInstant);

        // Assert: The final NTFS time should be identical to the original value.
        assertEquals("The round-trip conversion should yield the original value", originalNtfsTime, roundTripNtfsTime);
    }
}