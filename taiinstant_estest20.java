package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests the conversion from a standard {@code java.time.Instant} to a {@code TaiInstant}.
     *
     * <p>This test case verifies that the conversion logic correctly handles a large
     * timestamp value. The source {@code Instant} is created from a {@code UtcInstant}
     * defined by a large Modified Julian Day. The test then asserts that the resulting
     * {@code TaiInstant} has the correct, pre-calculated number of TAI seconds and nanoseconds.
     */
    @Test
    public void of_convertsInstantFromLargeUtcValue_toCorrectTaiInstant() {
        // ARRANGE: Create a source Instant from a UtcInstant with a large Modified Julian Day.
        // The specific values are chosen to test the conversion logic with large numbers.
        long modifiedJulianDay = 1_000_000_000L;
        long nanoOfJulianDay = 1_000_000_000L; // This is exactly 1 full second.
        UtcInstant sourceUtcInstant = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfJulianDay);
        Instant sourceInstant = sourceUtcInstant.toInstant();

        // These are the pre-calculated, expected results for the given input.
        // The conversion from UTC to TAI is complex and involves leap second adjustments.
        long expectedTaiSeconds = 86_396_871_974_438L;
        int expectedNanos = 0;

        // ACT: Convert the Instant to a TaiInstant using the factory method under test.
        TaiInstant actualTaiInstant = TaiInstant.of(sourceInstant);

        // ASSERT: Verify that the TAI seconds and nanoseconds are correct.
        assertEquals(
            "TAI seconds should match the pre-calculated value",
            expectedTaiSeconds,
            actualTaiInstant.getTaiSeconds()
        );
        assertEquals(
            "Nanoseconds should be zero for this conversion",
            expectedNanos,
            actualTaiInstant.getNano()
        );
    }
}