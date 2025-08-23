package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

// Note: The original test was auto-generated. This version has been refactored
// for improved understandability and maintainability.
public class TaiInstant_ESTestTest12 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that withNano() creates a new TaiInstant with the specified nano-of-second,
     * while preserving the TAI seconds count. It also verifies the immutability of the
     * original TaiInstant object.
     */
    @Test
    public void withNano_returnsNewInstanceWithUpdatedNanosAndPreservesOriginal() {
        // Arrange: Create an initial TaiInstant from a standard java.time.Instant.
        // The original test used Instant.ofEpochSecond(36204L, -1L), which normalizes to
        // 36203 seconds and 999,999,999 nanoseconds. We use the normalized values directly for clarity.
        long initialEpochSecond = 36203L;
        int initialNano = 999_999_999;
        Instant utcInstant = Instant.ofEpochSecond(initialEpochSecond, initialNano);

        // This is the expected TAI seconds value after converting the UTC Instant.
        // The conversion logic is complex and accounts for leap seconds.
        long expectedTaiSeconds = 378727413L;
        TaiInstant originalTaiInstant = TaiInstant.of(utcInstant);

        // Act: Call the withNano() method to create a new instant with a different nano value.
        int newNanoValue = 0;
        TaiInstant modifiedTaiInstant = originalTaiInstant.withNano(newNanoValue);

        // Assert: Verify the state of the new, modified TaiInstant.
        assertEquals("The new instance should have the updated nano-of-second value.",
                newNanoValue, modifiedTaiInstant.getNano());
        assertEquals("The new instance should have the same TAI seconds as the original.",
                expectedTaiSeconds, modifiedTaiInstant.getTaiSeconds());

        // Assert: Verify that the original TaiInstant remains unchanged (immutability).
        assertEquals("Original instance's TAI seconds should not be changed.",
                expectedTaiSeconds, originalTaiInstant.getTaiSeconds());
        assertEquals("Original instance's nanos should not be changed.",
                initialNano, originalTaiInstant.getNano());
    }
}