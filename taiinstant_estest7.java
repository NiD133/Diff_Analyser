package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the TaiInstant class, focusing on the minus() method.
 */
public class TaiInstantTest {

    /**
     * Tests that subtracting a negative duration is equivalent to adding the
     * positive counterpart, especially when the operation crosses a second boundary.
     */
    @Test
    public void minus_withNegativeDuration_correctlyAddsAndNormalizesAcrossSecondBoundary() {
        // Arrange: Create a TAI instant that is just one nanosecond away from a full second.
        // This sets up a clear boundary condition for the test.
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(100L, 999_999_999);

        // A negative duration of 1 millisecond. Subtracting this should be
        // equivalent to adding 1 millisecond (1,000,000 nanoseconds).
        Duration negativeDuration = Duration.ofMillis(-1);

        // Expected result: Adding 1ms to an instant at ...999ns should roll over the second.
        // Calculation: 999,999,999ns + 1,000,000ns = 1,000,999,999ns
        // This normalizes to 1 full second and 999,999 nanoseconds.
        // So, the TAI seconds should increment by 1, and the nano-of-second should become 999,999.
        TaiInstant expectedInstant = TaiInstant.ofTaiSeconds(101L, 999_999);

        // Act: Subtract the negative duration from the initial instant.
        TaiInstant resultInstant = initialInstant.minus(negativeDuration);

        // Assert: The resulting instant should match the expected, correctly normalized instant.
        assertEquals(expectedInstant, resultInstant);
    }
}