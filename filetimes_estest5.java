package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that converting an NTFS time to a Date and back again yields the original value.
     * This confirms the round-trip integrity of the conversion methods.
     */
    @Test
    public void testNtfsTimeConversionRoundTrip() {
        // Arrange: Define the starting NTFS time.
        // 0L represents the start of the NTFS epoch: 1601-01-01T00:00:00Z.
        final long originalNtfsTime = 0L;

        // Act: Convert the NTFS time to a Date and then back to an NTFS time.
        final Date intermediateDate = FileTimes.ntfsTimeToDate(originalNtfsTime);
        final long resultNtfsTime = FileTimes.toNtfsTime(intermediateDate);

        // Assert: The final NTFS time should be identical to the original.
        assertEquals(originalNtfsTime, resultNtfsTime);
    }
}