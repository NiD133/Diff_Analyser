package org.threeten.extra.scale;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class TaiInstant_ESTestTest60 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests the round-trip conversion from a historical TaiInstant to UtcInstant and back.
     * <p>
     * This test verifies that converting a {@code TaiInstant} from before the UTC epoch
     * to a {@code UtcInstant} produces the correct intermediate value, and that converting
     * it back to a {@code TaiInstant} results in the original value (a lossless conversion).
     */
    @Test
    public void conversionToUtcInstantAndBackIsLosslessForHistoricalDate() {
        // ARRANGE
        // An instant one second after the TAI epoch of 1958-01-01T00:00:00(TAI).
        TaiInstant originalTaiInstant = TaiInstant.ofTaiSeconds(1L, 0L);

        // The threeten-extra library's model extrapolates a 10-second difference
        // between TAI and UTC for dates before 1972.
        // Therefore, 1958-01-01T00:00:01(TAI) should correspond to 1957-12-31T23:59:51(UTC).
        long expectedUtcEpochDay = LocalDate.of(1957, 12, 31).toEpochDay();
        long expectedUtcNanoOfDay = LocalTime.of(23, 59, 51).toNanoOfDay();

        // ACT
        // Convert from TAI to UTC, and then back to TAI.
        UtcInstant intermediateUtcInstant = originalTaiInstant.toUtcInstant();
        TaiInstant roundTripTaiInstant = TaiInstant.of(intermediateUtcInstant);

        // ASSERT
        // 1. Verify the intermediate UtcInstant value is correct.
        assertEquals("Epoch day of converted UtcInstant",
                expectedUtcEpochDay, intermediateUtcInstant.getEpochDay());
        assertEquals("Nano of day of converted UtcInstant",
                expectedUtcNanoOfDay, intermediateUtcInstant.getNanoOfDay());

        // 2. Verify the round-trip conversion is lossless.
        assertEquals("Round-trip conversion should yield the original TaiInstant",
                originalTaiInstant, roundTripTaiInstant);
    }
}