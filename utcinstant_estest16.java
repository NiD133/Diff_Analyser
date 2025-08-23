package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    @Test
    public void plus_withZeroDuration_returnsAnEqualInstance() {
        // Arrange: Create an initial UtcInstant.
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Act: Calculate the duration between the instant and itself (which should be zero)
        // and add it back to the initial instant.
        Duration zeroDuration = initialInstant.durationUntil(initialInstant);
        UtcInstant resultInstant = initialInstant.plus(zeroDuration);

        // Assert: Verify the behavior is correct.
        // 1. The duration calculated should indeed be zero.
        assertEquals("Duration between an instant and itself should be zero", Duration.ZERO, zeroDuration);

        // 2. Adding a zero duration should result in an equal instant.
        assertEquals("Adding a zero duration should not change the value", initialInstant, resultInstant);

        // 3. Since UtcInstant is immutable, the plus() method should return a new instance.
        // Note: Some immutable classes optimize this specific case by returning `this`.
        // This assertion validates that a distinct object is returned.
        assertNotSame("A new instance should be returned to preserve immutability", initialInstant, resultInstant);
    }
}