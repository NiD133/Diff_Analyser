package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.evosuite.runtime.mock.java.time.MockClock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This class contains a specific test case focusing on edge-case behavior.
 */
public class Symmetry010ChronologyTest {

    /**
     * Verifies that dateNow() throws an ArithmeticException when the provided Clock
     * represents a time so far in the future that it causes a long overflow during
     * internal calculations.
     */
    @Test
    public void dateNow_withClockAtFutureInfinity_throwsArithmeticException() {
        // Arrange: Create a clock that points to a time that will cause an overflow.
        // ChronoUnit.FOREVER.getDuration() represents a duration so large it is
        // effectively infinite for practical calculations.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Clock baseClock = MockClock.systemDefaultZone();
        Duration infiniteDuration = ChronoUnit.FOREVER.getDuration();
        Clock clockAtFutureInfinity = MockClock.offset(baseClock, infiniteDuration);

        // Act & Assert: Call dateNow and verify that it throws the expected exception.
        try {
            chronology.dateNow(clockAtFutureInfinity);
            fail("Expected an ArithmeticException to be thrown for a clock value that causes overflow.");
        } catch (ArithmeticException e) {
            // The underlying implementation uses operations that throw this specific
            // exception on overflow. We verify the message to be certain of the cause.
            assertEquals("long overflow", e.getMessage());
        }
    }
}