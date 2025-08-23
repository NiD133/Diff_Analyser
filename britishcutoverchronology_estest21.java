package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that dateNow() throws a DateTimeException if the provided clock
     * returns an instant that is outside the representable range.
     */
    @Test
    public void dateNow_whenClockInstantExceedsMax_throwsException() {
        // Arrange: Create a BritishCutoverChronology instance.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Arrange: Create a clock that produces an Instant beyond the maximum storable value.
        // We do this by starting at the absolute maximum Instant and adding one second.
        // The call to dateNow() will fail internally when it tries to create this invalid Instant.
        Clock clockAtMax = Clock.fixed(Instant.MAX, ZoneOffset.UTC);
        Clock clockBeyondMax = Clock.offset(clockAtMax, Duration.ofSeconds(1));

        // Act & Assert
        try {
            chronology.dateNow(clockBeyondMax);
            fail("Expected a DateTimeException to be thrown for an out-of-range instant.");
        } catch (DateTimeException e) {
            // Assert that the correct exception was thrown with the expected message.
            // The exception originates from java.time when the overflow is detected.
            assertEquals("Instant exceeds minimum or maximum instant", e.getMessage());
        }
    }
}