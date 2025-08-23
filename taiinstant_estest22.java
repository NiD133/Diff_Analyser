package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This test class is a refactored version of an auto-generated test.
 * In a real-world scenario, this test would be part of a comprehensive
 * TaiInstantTest class.
 */
public class TaiInstant_ESTestTest22 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that subtracting a zero duration from a TaiInstant is a no-op
     * and returns the same object instance, verifying an important optimization.
     */
    @Test
    public void minus_whenDurationIsZero_returnsSameInstance() {
        // Arrange: Create a TaiInstant and a zero duration.
        // The initial values are chosen to match the state of the object under test
        // in the original code, but are now created directly for clarity.
        final long seconds = -2L;
        final int nanos = 320_000_000;
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(seconds, nanos);
        Duration zeroDuration = Duration.ZERO;

        // Act: Subtract the zero duration.
        TaiInstant result = initialInstant.minus(zeroDuration);

        // Assert: The result should be the exact same instance, not just an equal one.
        assertSame("Subtracting Duration.ZERO should be an identity operation returning the same instance.",
                initialInstant, result);

        // For completeness, also verify the state of the returned object is unchanged.
        assertEquals("The TAI seconds should remain unchanged.", seconds, result.getTaiSeconds());
        assertEquals("The nanoseconds should remain unchanged.", nanos, result.getNano());
    }
}