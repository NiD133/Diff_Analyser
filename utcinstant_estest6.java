package org.threeten.extra.scale;

import org.junit.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test class is a refactored version of an auto-generated test
 * to improve its understandability.
 */
public class UtcInstantTest {

    /**
     * Tests that isAfter() correctly returns false when comparing an earlier instant
     * to a later one. It also verifies the internal state of a UtcInstant created
     * from a TaiInstant at the TAI epoch.
     */
    @Test
    public void isAfter_shouldReturnFalse_whenComparingAnEarlierInstantToALaterOne() {
        // --- Arrange ---
        // Create a later instant, based on a non-zero TAI seconds value.
        UtcInstant laterInstant = TaiInstant.ofTaiSeconds(3217L, 1000L).toUtcInstant();

        // Create an earlier instant, based on the TAI epoch (0 seconds).
        UtcInstant earlierInstant = UtcInstant.of(TaiInstant.ofTaiSeconds(0L, 1000L));

        // --- Act ---
        // Check if the earlier instant is considered to be after the later one.
        boolean isAfter = earlierInstant.isAfter(laterInstant);

        // --- Assert ---
        // 1. Verify the comparison logic.
        assertFalse("An earlier instant should not be reported as 'after' a later instant.", isAfter);

        // 2. Verify the state of the 'earlierInstant'. This assertion was in the original
        //    test and confirms the correctness of the TAI-to-UTC conversion at the TAI epoch.
        // The expected value reflects the historical TAI-UTC offset.
        // 86_390_000_001_000 nanoseconds corresponds to 23:59:50.000001000 on the previous UTC day.
        long expectedNanoOfDayAtTaiEpoch = 86_390_000_001_000L;
        assertEquals("Nano-of-day should reflect the TAI-UTC offset at the TAI epoch",
                     expectedNanoOfDayAtTaiEpoch, earlierInstant.getNanoOfDay());
    }
}