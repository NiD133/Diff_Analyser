package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link TaiInstant}.
 * This class contains the refactored test case.
 */
public class TaiInstant_ESTestTest42 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that compareTo() correctly evaluates a modern instant as being after an
     * instant from the distant past.
     *
     * <p>The test creates a past instant by taking a modern reference time and setting its
     * date to the Modified Julian Day epoch (1858-11-17), while preserving the time of day.
     * It then verifies that the comparison returns a positive value as expected.
     */
    @Test
    public void compareTo_returnsPositive_whenComparingModernInstantToDistantPastInstant() {
        // --- ARRANGE ---
        // 1. Define a clear, modern reference point in time.
        Instant modernInstant = Instant.parse("2023-10-27T12:00:00Z");
        TaiInstant modernTaiInstant = TaiInstant.of(modernInstant);

        // 2. To create an instant in the distant past, we first convert the modern
        // instant to the UtcInstant representation.
        UtcInstant modernUtcInstant = UtcInstant.of(modernTaiInstant);

        // 3. Then, create a new UtcInstant by changing the day to the epoch of the
        // Modified Julian Day system (1858-11-17), while keeping the same time of day.
        long modifiedJulianDayEpoch = 0L;
        UtcInstant pastUtcInstant = modernUtcInstant.withModifiedJulianDay(modifiedJulianDayEpoch);

        // 4. Finally, convert this past UtcInstant back to a TaiInstant for comparison.
        TaiInstant pastTaiInstant = TaiInstant.of(pastUtcInstant);

        // --- ACT ---
        // Compare the modern instant to the one from the distant past.
        int comparisonResult = modernTaiInstant.compareTo(pastTaiInstant);

        // --- ASSERT ---
        // First, verify that our setup is correct: the time-of-day should have been
        // preserved in the past instant. 12:00:00Z is 43,200 seconds into the day.
        long expectedNanosOfDay = 43_200L * 1_000_000_000L;
        assertEquals(
            "Nano-of-day should be preserved from the original instant",
            expectedNanosOfDay,
            pastUtcInstant.getNanoOfDay()
        );

        // Second, verify the core logic: the modern instant is correctly identified as
        // being after the past one. A return value of 1 indicates that
        // modernTaiInstant > pastTaiInstant.
        assertEquals(
            "A modern instant should be greater than a past instant",
            1,
            comparisonResult
        );
    }
}