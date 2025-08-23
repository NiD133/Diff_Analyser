package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the conversion from {@link Instant} to {@link TaiInstant}.
 *
 * Note: The original class name and inheritance from a scaffolding class are
 * kept to show a direct refactoring of the provided code. In a real-world
 * scenario, a generated class name like "TaiInstant_ESTestTest24" would be
 * renamed to something more descriptive, such as "TaiInstantCreationTest".
 */
public class TaiInstant_ESTestTest24 extends TaiInstant_ESTest_scaffolding {

    /**
     * Verifies that creating a TaiInstant from a standard java.time.Instant
     * correctly accounts for leap seconds and produces the right TAI second count.
     */
    @Test
    public void of_shouldConvertUtcInstantToCorrectTaiInstant() {
        // ARRANGE
        // A standard java.time.Instant, which follows the UTC time-scale.
        // This specific instant ("2014-02-14T20:21:22.320Z") corresponds to the
        // fixed time returned by MockInstant.now() in the original generated test.
        Instant utcInstant = Instant.parse("2014-02-14T20:21:22.320Z");

        // The TAI time-scale is a continuous count of SI seconds since its epoch
        // (1958-01-01T00:00:00 TAI) and does not have leap seconds.
        // The conversion from UTC to TAI requires accounting for all leap seconds
        // that have occurred. As of February 2014, the TAI-UTC offset was +35 seconds.
        // The expected values below are the TAI representation of the UTC instant above.
        long expectedTaiSeconds = 1771100516L;
        int expectedNanos = 320000000;

        // ACT
        // Perform the conversion from a standard Instant to a TaiInstant.
        TaiInstant taiInstant = TaiInstant.of(utcInstant);

        // ASSERT
        // Verify that the resulting TaiInstant has the correct number of seconds
        // and nanoseconds based on the TAI time-scale.
        assertEquals("TAI seconds should be correctly calculated", expectedTaiSeconds, taiInstant.getTaiSeconds());
        assertEquals("Nano-of-second should be preserved", expectedNanos, taiInstant.getNano());
    }
}