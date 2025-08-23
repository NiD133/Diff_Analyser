package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 * This focuses on the conversion to {@link TaiInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests the conversion of a UtcInstant from before the modern UTC era (1972)
     * to a TaiInstant.
     *
     * <p>The conversion for historical dates involves a proleptic application of the
     * TAI-UTC offset. This test verifies that calculation.
     */
    @Test
    public void toTaiInstant_convertsPre1972DateCorrectly() {
        // Arrange: Define a UTC instant for a date before the TAI epoch.
        // The date 1859-09-09T00:00:00Z corresponds to Modified Julian Day 296.
        long mjd = 296L;
        long nanoOfDay = 0L;
        UtcInstant pre1972UtcInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

        // The expected TAI seconds are calculated based on the difference from the TAI epoch (1958-01-01)
        // and the initial 10-second TAI-UTC offset established in 1972.
        final long MJD_OF_TAI_EPOCH = 36204L; // MJD for 1958-01-01
        final long SECONDS_PER_DAY = 86400L;
        final long INITIAL_TAI_UTC_OFFSET_SECONDS = 10L;

        long daysSinceTaiEpoch = mjd - MJD_OF_TAI_EPOCH;
        long expectedTaiSeconds = (daysSinceTaiEpoch * SECONDS_PER_DAY) + INITIAL_TAI_UTC_OFFSET_SECONDS;
        // Calculation: (296 - 36204) * 86400 + 10 = -3102451190

        // Act: Convert the UtcInstant to a TaiInstant.
        TaiInstant actualTaiInstant = pre1972UtcInstant.toTaiInstant();

        // Assert: Verify the components of the resulting TaiInstant.
        assertEquals("TAI seconds should be correctly calculated for a pre-1972 date",
                expectedTaiSeconds, actualTaiInstant.getTaiSeconds());
        assertEquals("Nano-of-second should be preserved as zero",
                0, actualTaiInstant.getNano());
    }
}