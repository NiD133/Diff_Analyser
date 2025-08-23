package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests that converting a TaiInstant to a java.time.Instant and back
     * results in the original TaiInstant. This verifies the round-trip
     * conversion for a non-leap-second instant.
     */
    @Test
    public void testRoundTripConversionToInstantAndBack() {
        // Arrange: Create a TaiInstant at its epoch (0 seconds, 0 nanoseconds).
        TaiInstant originalTaiInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Convert to a standard Instant and then back to a TaiInstant.
        Instant convertedInstant = originalTaiInstant.toInstant();
        TaiInstant resultTaiInstant = TaiInstant.of(convertedInstant);

        // Assert: The TaiInstant after the round-trip conversion should be
        // equal to the original.
        assertEquals(originalTaiInstant, resultTaiInstant);
    }
}