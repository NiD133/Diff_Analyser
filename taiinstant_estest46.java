package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link TaiInstant} class, focusing on comparison methods.
 */
public class TaiInstantComparisonTest {

    /**
     * Tests that isBefore() returns true when comparing an instant to a later
     * instant that has the same second value but a greater nanosecond value.
     */
    @Test
    public void isBefore_returnsTrue_whenOtherInstantHasGreaterNanos() {
        // Arrange: Create two instants with the same second value.
        // The 'laterInstant' has a larger nanosecond component.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(37L, 100L);
        TaiInstant laterInstant = TaiInstant.ofTaiSeconds(37L, 500L);

        // Act: Check if the base instant is before the later one.
        boolean isBefore = baseInstant.isBefore(laterInstant);

        // Assert: The result should be true.
        assertTrue(
            "An instant should be 'before' another with the same seconds but more nanos.",
            isBefore
        );
    }
}