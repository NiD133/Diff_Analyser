package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.Year;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Verifies that creating a local date-time from a TemporalAccessor that lacks
     * sufficient date and time information throws a DateTimeException.
     * A {@link java.time.Year} instance only provides the year, which is not enough
     * to construct a full {@code ChronoLocalDateTime}.
     */
    @Test
    public void localDateTime_fromIncompleteTemporal_throwsException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        TemporalAccessor incompleteTemporal = Year.of(37);
        String expectedErrorMessage = "Unable to obtain ChronoLocalDateTime from TemporalAccessor: class java.time.Year";

        // Act & Assert
        try {
            chronology.localDateTime(incompleteTemporal);
            fail("Expected DateTimeException was not thrown.");
        } catch (DateTimeException ex) {
            assertEquals(expectedErrorMessage, ex.getMessage());
        }
    }
}