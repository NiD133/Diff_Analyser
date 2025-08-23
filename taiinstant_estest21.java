package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * A more understandable test for the TaiInstant class.
 * This example focuses on the behavior of the minus() method.
 */
public class TaiInstantTest {

    @Test
    public void minus_withZeroDuration_returnsSameInstance() {
        // Arrange: Create an instant at the TAI epoch and a zero-length duration.
        final TaiInstant initialInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        final Duration zeroDuration = Duration.ZERO;

        // Act: Subtract the zero duration from the instant.
        final TaiInstant result = initialInstant.minus(zeroDuration);

        // Assert: The result should be the exact same instance, not just an equal one.
        // This confirms an important optimization for immutable objects where a no-op
        // operation should not create a new object.
        assertSame("Subtracting Duration.ZERO should be a no-op and return the same instance.", initialInstant, result);
        
        // A secondary check to ensure the value is unchanged, though implied by assertSame.
        assertEquals(0, result.getNano());
    }
}