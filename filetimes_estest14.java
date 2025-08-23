package org.apache.commons.io.file.attribute;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#ntfsTimeToInstant(BigDecimal)} correctly converts a small
     * positive NTFS time value to the corresponding Instant.
     */
    @Test
    public void ntfsTimeToInstant_shouldConvertSmallPositiveValueCorrectly() {
        // Arrange
        // NTFS time is the number of 100-nanosecond intervals since the NTFS epoch (1601-01-01 00:00:00 UTC).
        // An input of 10 represents 10 * 100 = 1000 nanoseconds (or 1 microsecond) after the epoch.
        BigDecimal ntfsTime = BigDecimal.TEN;
        Instant expectedInstant = Instant.parse("1601-01-01T00:00:00.000001Z");

        // Act
        Instant actualInstant = FileTimes.ntfsTimeToInstant(ntfsTime);

        // Assert
        assertEquals(expectedInstant, actualInstant);
    }
}