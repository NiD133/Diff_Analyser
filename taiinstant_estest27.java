package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * A more understandable test for the TaiInstant class.
 * This focuses on verifying the behavior of the getNano() method.
 */
public class TaiInstantTest {

    /**
     * Verifies that when a TaiInstant is created from a java.time.Instant,
     * the nanosecond-of-second component is correctly preserved.
     */
    @Test
    public void getNano_whenCreatedFromInstant_shouldPreserveNanoseconds() {
        // Arrange: Create a standard Instant with a specific, non-zero nanosecond value.
        // The conversion from an Instant (which is based on UTC) to a TaiInstant
        // involves complex adjustments for leap seconds. However, these adjustments
        // should only affect the whole-second part of the timestamp, not the
        // fractional nanosecond part.
        final int expectedNanos = 123_456_789;
        Instant sourceInstant = Instant.ofEpochSecond(1_000_000_000L, expectedNanos);

        // Act: Convert the source Instant to a TaiInstant and then retrieve its
        // nanosecond component.
        TaiInstant taiInstant = TaiInstant.of(sourceInstant);
        int actualNanos = taiInstant.getNano();

        // Assert: The nanosecond component from the TaiInstant should be identical
        // to the one from the original source Instant.
        assertEquals(expectedNanos, actualNanos);
    }
}