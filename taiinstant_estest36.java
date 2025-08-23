package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Tests for the {@link TaiInstant#minus(Duration)} method.
 */
public class TaiInstantMinusTest {

    @Test(expected = ArithmeticException.class)
    public void minus_whenDurationIsForever_throwsArithmeticException() {
        // Arrange
        // A duration of "forever" is the largest possible duration and will cause an overflow
        // when used in subtraction, as documented by the minus() method.
        Duration foreverDuration = ChronoUnit.FOREVER.getDuration();
        TaiInstant anyInstant = TaiInstant.ofTaiSeconds(0, 0);

        // Act
        // Attempt to subtract this massive duration from an instant.
        anyInstant.minus(foreverDuration);

        // Assert
        // The @Test(expected) annotation verifies that an ArithmeticException is thrown.
    }
}