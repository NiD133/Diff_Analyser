package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that zonedDateTime() throws an exception when the provided temporal
     * accessor lacks the necessary time and zone information.
     */
    @Test
    public void zonedDateTime_whenTemporalAccessorLacksTimeAndZone_throwsException() {
        // Arrange
        // The BritishCutoverChronology.CUTOVER is a LocalDate, which is a TemporalAccessor.
        // A LocalDate does not contain time or time-zone information.
        TemporalAccessor partialTemporal = BritishCutoverChronology.CUTOVER;
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act & Assert
        // Attempting to create a ZonedDateTime from a LocalDate should fail because
        // crucial information (time, zone) is missing.
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> chronology.zonedDateTime(partialTemporal)
        );

        // Verify the exception message for more precise feedback.
        assertTrue(
            "The exception message should indicate the conversion failure.",
            thrown.getMessage().contains("Unable to obtain ChronoZonedDateTime from TemporalAccessor")
        );
    }
}