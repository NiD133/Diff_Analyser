package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link TaiInstant}.
 * This focuses on the isBefore() method's logic.
 */
public class TaiInstantTest {

    @Test
    public void isBefore_shouldReturnFalse_whenComparingToAnEarlierInstant() {
        // Arrange: Create a base instant and a second instant that is later in time.
        // Subtracting a negative duration is equivalent to adding a positive one,
        // thus creating an instant that is later on the time-line.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(1_000_000L, 0);
        Duration negativeDuration = Duration.ofHours(-1);
        TaiInstant laterInstant = baseInstant.minus(negativeDuration);

        // Act: Check if the later instant is "before" the base instant.
        boolean isBefore = laterInstant.isBefore(baseInstant);

        // Assert: The result should be false, as a later time is not before an earlier time.
        assertFalse("A later instant should not be considered 'before' an earlier one.", isBefore);
    }
}