package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.JapaneseEra;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void zonedDateTime_whenTemporalAccessorHasInsufficientInformation_throwsException() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // A JapaneseEra is a valid TemporalAccessor but lacks the necessary date and time fields
        // to be converted into a complete ZonedDateTime.
        TemporalAccessor temporalWithInsufficientInfo = JapaneseEra.TAISHO;

        // Act & Assert
        try {
            chronology.zonedDateTime(temporalWithInsufficientInfo);
            fail("Expected DateTimeException was not thrown.");
        } catch (DateTimeException e) {
            // The exception is expected. We verify the message to ensure it's thrown for the correct reason.
            // This message comes from the base java.time.chrono.Chronology class.
            String expectedMessage = "Unable to obtain ChronoZonedDateTime from TemporalAccessor: " +
                                     "class java.time.chrono.JapaneseEra";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}