package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.evosuite.runtime.mock.java.time.MockInstant;

// Note: Unused imports from the original test have been removed for clarity.

public class TaiInstant_ESTestTest25 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that converting a UtcInstant at Modified Julian Day 0 (1858-11-17)
     * to a TaiInstant produces the correct number of TAI seconds.
     */
    @Test
    public void getTaiSeconds_forDateCorrespondingToModifiedJulianDayZero() {
        // ARRANGE
        // The goal is to create a UtcInstant for the date 1858-11-17 (MJD 0)
        // with a specific, consistent time-of-day for this test. The following
        // steps derive this time from a mocked Instant.

        // 1. Start with a mock Instant to get a fixed time-of-day.
        Instant sourceInstant = MockInstant.now();

        // 2. Convert to UtcInstant to access its time-of-day.
        UtcInstant intermediateUtcInstant = UtcInstant.of(TaiInstant.of(sourceInstant));

        // 3. Create the target UtcInstant for Modified Julian Day 0,
        //    preserving the time-of-day from the intermediate instant.
        long modifiedJulianDayZero = 0L;
        UtcInstant utcInstantAtMjdZero = intermediateUtcInstant.withModifiedJulianDay(modifiedJulianDayZero);

        // This assertion acts as a guard to ensure the test setup is correct.
        // It verifies that the nano-of-day from the source instant was preserved
        // and matches the value this test depends on.
        long expectedNanoOfDay = 73281320000000L;
        assertEquals(
            "Precondition failed: The nano-of-day for the test date is incorrect.",
            expectedNanoOfDay,
            utcInstantAtMjdZero.getNanoOfDay());

        // ACT
        // Convert the UtcInstant representing a moment on 1858-11-17 to a TaiInstant.
        TaiInstant resultTaiInstant = TaiInstant.of(utcInstantAtMjdZero);
        long actualTaiSeconds = resultTaiInstant.getTaiSeconds();

        // ASSERT
        // Verify the TAI seconds. The value is negative because the date is
        // approximately 99 years before the TAI epoch (1958-01-01).
        long expectedTaiSeconds = -3127952309L;
        assertEquals(expectedTaiSeconds, actualTaiSeconds);
    }
}