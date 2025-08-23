package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on the behavior of the withNanoOfDay() method.
 */
public class UtcInstantTest {

    /**
     * Tests that calling withNanoOfDay() with the same value that the object
     * already has results in an equal instance, demonstrating idempotency.
     */
    @Test
    public void withNanoOfDay_whenValueIsUnchanged_returnsEqualInstance() {
        // Arrange: Create a UtcInstant at the epoch, which has a nano-of-day of 0.
        UtcInstant originalInstant = UtcInstant.of(Instant.EPOCH);
        assertEquals("Precondition failed: The original instant should have 0 nano-of-day",
                0L, originalInstant.getNanoOfDay());

        // Act: Call withNanoOfDay() with the same nano-of-day value.
        UtcInstant updatedInstant = originalInstant.withNanoOfDay(0L);

        // Assert: The returned instance should be equal to the original.
        assertEquals(originalInstant, updatedInstant);
    }
}