package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Month;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This is a refactored version of a single test case.
 */
public class InternationalFixedChronology_ESTestTest36 {

    /**
     * Tests that creating a date from a TemporalAccessor with insufficient information
     * throws an UnsupportedTemporalTypeException. A Month object by itself does not
     * provide enough information (like year or epoch day) to create a full date.
     */
    @Test
    public void date_fromPartialTemporalAccessor_throwsException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // A Month is a valid TemporalAccessor but lacks the necessary fields to form a complete date.
        TemporalAccessor partialTemporal = Month.MAY;

        // Act & Assert
        try {
            chronology.date(partialTemporal);
            fail("Expected UnsupportedTemporalTypeException to be thrown due to insufficient date information.");
        } catch (UnsupportedTemporalTypeException e) {
            // The exception is expected because Month cannot be resolved to an EpochDay.
            assertEquals("Unsupported field: EpochDay", e.getMessage());
        }
    }
}