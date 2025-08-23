package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TaiInstant}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class TaiInstantTest {

    /**
     * Tests that adding a large negative duration to a TaiInstant produces the correct result.
     * This test also verifies that the factory method {@code ofTaiSeconds} correctly normalizes
     * inputs where the nano-of-second adjustment is negative.
     */
    @Test
    public void plus_shouldCorrectlyAddNegativeDuration() {
        // Arrange: Set up the initial state and inputs.

        // 1. Create an initial TaiInstant using a negative nano adjustment.
        // The factory method should normalize 10 seconds and -1 nanosecond
        // into 9 seconds and 999,999,999 nanoseconds.
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(10L, -1L);

        // Verify that the initial instant was normalized correctly.
        assertEquals("Initial seconds should be normalized", 9L, initialInstant.getTaiSeconds());
        assertEquals("Initial nanos should be normalized", 999_999_999, initialInstant.getNano());

        // 2. Define a simple, large negative duration to add.
        Duration twoDaysNegative = Duration.ofDays(-2);


        // Act: Call the method under test.
        TaiInstant resultInstant = initialInstant.plus(twoDaysNegative);


        // Assert: Verify the outcome.

        // Calculate the expected result.
        // The duration of -2 days is -172,800 seconds (2 * 86400).
        long secondsInTwoDays = 2 * 86400L;
        long expectedSeconds = 9L - secondsInTwoDays; // 9 - 172,800 = -172,791
        int expectedNanos = 999_999_999; // The nano part should remain unchanged.

        assertEquals("Resulting seconds should be correct after adding negative duration",
                expectedSeconds, resultInstant.getTaiSeconds());
        assertEquals("Resulting nanos should be unchanged",
                expectedNanos, resultInstant.getNano());
    }
}