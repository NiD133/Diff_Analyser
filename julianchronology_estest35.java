package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.fail;

/**
 * Unit tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that zonedDateTime() throws a NullPointerException when the provided Instant is null.
     */
    @Test
    public void zonedDateTime_withNullInstant_throwsNullPointerException() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        ZoneId systemZone = ZoneId.systemDefault();
        Instant nullInstant = null;

        // Act & Assert
        try {
            chronology.zonedDateTime(nullInstant, systemZone);
            fail("Expected a NullPointerException to be thrown, but no exception was thrown.");
        } catch (NullPointerException expected) {
            // Success: The expected exception was thrown.
        }
    }
}