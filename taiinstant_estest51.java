package org.threeten.extra.scale;

import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests that adding a duration that would cause the TAI seconds to overflow
     * throws an ArithmeticException.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Create a standard instant and a duration large enough to cause an overflow.
        // ChronoUnit.FOREVER.getDuration() returns a duration of Long.MAX_VALUE seconds.
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 0L);
        Duration nearInfiniteDuration = ChronoUnit.FOREVER.getDuration();

        // Act: Adding the near-infinite duration to any positive instant will cause a
        // long overflow. The assertion is handled by the 'expected' attribute of the
        // @Test annotation, which verifies that an ArithmeticException is thrown.
        instant.plus(nearInfiniteDuration);
    }
}