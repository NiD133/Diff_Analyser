package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TaiInstant#of(Instant)} factory method.
 * This class provides an improved, more understandable version of an
 * automatically generated test case.
 */
public class TaiInstantCreationTest {

    /**
     * Tests that converting an Instant from a date far in the past, predating
     * both the TAI and UTC epochs, produces the correct proleptic TaiInstant.
     */
    @Test
    public void of_convertsInstantFromBeforeTaiAndUtcEpochs() {
        // --- Arrange ---
        // An Instant representing a point in time before both the TAI epoch (1958)
        // and the standard UTC epoch (1970).
        Instant pastInstant = Instant.parse("1901-05-15T12:28:01.320Z");

        // The expected TAI seconds and nanoseconds for the given Instant.
        // TAI seconds are relative to its epoch of 1958-01-01T00:00:00(TAI).
        // These values were derived from the original test's calculation and
        // represent the known correct proleptic conversion.
        long expectedTaiSeconds = -1798688309L;
        int expectedNanos = 320000000;

        // --- Act ---
        TaiInstant result = TaiInstant.of(pastInstant);

        // --- Assert ---
        assertEquals(
            "TAI seconds should be correctly calculated for a date before the TAI epoch",
            expectedTaiSeconds,
            result.getTaiSeconds());
        assertEquals(
            "Nanoseconds should be preserved from the original Instant",
            expectedNanos,
            result.getNano());
    }
}