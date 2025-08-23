package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains the refactored test case.
 * Note: The original class name and inheritance (TaiInstant_ESTestTest5 extends TaiInstant_ESTest_scaffolding)
 * are preserved. In a real-world scenario, the class would likely be named TaiInstantTest.
 */
public class TaiInstant_ESTestTest5 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that withTaiSeconds() creates a new TaiInstant with the specified seconds,
     * while preserving the original nanosecond value.
     */
    @Test
    public void withTaiSeconds_shouldUpdateSecondsAndPreserveNanos() {
        // Arrange: Create a base TaiInstant with a known nano-of-second value.
        // The original test used MockInstant.now(), which corresponds to "2014-02-14T10:15:30.320Z".
        // Using an explicit Instant makes the test more readable and less dependent on mock configurations.
        final int originalNanos = 320_000_000;
        Instant baseUtcInstant = Instant.parse("2014-02-14T10:15:30.320Z");
        TaiInstant baseTaiInstant = TaiInstant.of(baseUtcInstant);

        // The new value for the TAI seconds field.
        final long newTaiSeconds = 1_000_000_000L;

        // Act: Call the method under test to create a modified instant.
        TaiInstant modifiedTaiInstant = baseTaiInstant.withTaiSeconds(newTaiSeconds);

        // Assert: Verify that the new instant has the updated seconds and the original nanos.
        assertEquals("The TAI seconds should be updated to the new value.",
                newTaiSeconds, modifiedTaiInstant.getTaiSeconds());
        assertEquals("The nanoseconds should be preserved from the original instant.",
                originalNanos, modifiedTaiInstant.getNano());
    }
}