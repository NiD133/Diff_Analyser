package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class is part of a test suite for {@link InternationalFixedChronology}.
 * This specific test file was automatically generated and has been refactored for clarity.
 */
public class InternationalFixedChronology_ESTestTest32 {

    /**
     * Tests that {@link InternationalFixedChronology#dateNow(Clock)} throws a DateTimeException
     * when the provided clock returns an instant that is outside the supported range of {@link java.time.Instant}.
     */
    @Test(timeout = 4000)
    public void dateNow_whenClockInstantIsTooLarge_throwsException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Create a clock that points to a time far in the future,
        // beyond the range supported by java.time.Instant.
        // The duration of an ERA is immense, making it suitable for this purpose.
        Clock baseClock = Clock.system(ZoneOffset.UTC);
        Duration immenseDuration = ChronoUnit.ERAS.getDuration();
        Clock farFutureClock = Clock.offset(baseClock, immenseDuration);

        // Act & Assert
        try {
            chronology.dateNow(farFutureClock);
            fail("Expected DateTimeException was not thrown for an out-of-bounds instant.");
        } catch (DateTimeException e) {
            // This is the expected behavior.
            // The underlying java.time.Instant throws this exception when its value exceeds the supported range.
            String expectedMessage = "Instant exceeds minimum or maximum instant";
            assertTrue(
                "Exception message should indicate the instant is out of bounds. Got: " + e.getMessage(),
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}