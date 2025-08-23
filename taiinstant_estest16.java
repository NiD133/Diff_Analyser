package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link TaiInstant} class.
 */
public class TaiInstantTest {

    /**
     * Tests that adding a zero duration to a TaiInstant returns the same instance.
     * This verifies an important optimization for immutable objects.
     */
    @Test
    public void plus_whenAddingZeroDuration_returnsSameInstance() {
        // Arrange
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        Duration zeroDuration = Duration.ZERO;

        // Act
        TaiInstant resultInstant = initialInstant.plus(zeroDuration);

        // Assert
        // For immutable objects, adding zero should be a no-op and return the original instance.
        assertSame("Adding a zero duration should not create a new object", initialInstant, resultInstant);
    }
}