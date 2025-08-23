package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.Year;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.Test;

/**
 * Tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Verifies that date(TemporalAccessor) throws an exception when the provided
     * accessor does not support the required EPOCH_DAY field.
     */
    @Test
    public void date_fromTemporalAccessorWithoutEpochDay_throwsException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // A java.time.Year is a good example of a TemporalAccessor that lacks epoch day information.
        TemporalAccessor temporalWithoutEpochDay = Year.of(2023);

        // Act & Assert
        UnsupportedTemporalTypeException exception = assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> chronology.date(temporalWithoutEpochDay)
        );

        assertEquals("Unsupported field: EpochDay", exception.getMessage());
    }
}