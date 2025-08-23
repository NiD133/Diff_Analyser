package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.Month;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.Test;

/**
 * Tests for the {@link JulianChronology} class, focusing on date creation.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a JulianDate from a TemporalAccessor fails if the accessor
     * does not provide enough information to define a date.
     *
     * The {@link JulianChronology#date(TemporalAccessor)} method relies on the
     * {@code EPOCH_DAY} field, which is not available in a simple {@code Month} object.
     */
    @Test
    public void dateFromTemporalAccessor_whenAccessorIsInsufficient_throwsException() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        // A Month instance is a TemporalAccessor but lacks the necessary EPOCH_DAY field
        // to be converted into a complete date.
        TemporalAccessor insufficientTemporalAccessor = Month.APRIL;

        // Act & Assert
        // We expect an exception because the Month object cannot be resolved to an epoch day.
        // The assertThrows method is a modern, clear way to test for expected exceptions.
        UnsupportedTemporalTypeException thrown = assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> julianChronology.date(insufficientTemporalAccessor)
        );

        // Verify that the exception message confirms the missing field, ensuring the
        // operation failed for the correct reason.
        assertEquals("Unsupported field: EpochDay", thrown.getMessage());
    }
}