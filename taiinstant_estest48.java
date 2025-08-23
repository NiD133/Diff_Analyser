package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that isAfter() returns true when comparing an instant to an earlier one
     * where both have the same TAI seconds but the former has a greater nanosecond value.
     */
    @Test
    public void isAfter_shouldReturnTrue_whenNanosecondsAreGreater() {
        // Arrange: Create two instants at the same second, but with one having more nanoseconds.
        TaiInstant earlierInstant = TaiInstant.ofTaiSeconds(37L, 100L);
        TaiInstant laterInstant = TaiInstant.ofTaiSeconds(37L, 200L);

        // Act: Check if the later instant is correctly identified as being "after" the earlier one.
        boolean result = laterInstant.isAfter(earlierInstant);

        // Assert: The result must be true.
        assertTrue("An instant should be considered 'after' another if its nanosecond part is greater", result);
    }
}