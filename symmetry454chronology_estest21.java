package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.DateTimeException;
import java.time.temporal.TemporalAccessor;
import org.junit.Test;

/**
 * This test class contains tests for the {@link Symmetry454Chronology} class.
 * The original class name, Symmetry454Chronology_ESTestTest21, is kept as per the instructions.
 * In a real-world scenario, it would be renamed to something more descriptive, like Symmetry454ChronologyTest.
 */
public class Symmetry454Chronology_ESTestTest21 {

    /**
     * Tests that {@link Symmetry454Chronology#localDateTime(TemporalAccessor)} throws a
     * DateTimeException when passed a TemporalAccessor from an incompatible chronology.
     */
    @Test
    public void localDateTime_throwsExceptionForIncompatibleTemporalAccessor() {
        // Arrange: Create an instance of the chronology under test and a date
        // from a different, incompatible chronology (EthiopicDate).
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        TemporalAccessor incompatibleDate = EthiopicDate.ofEpochDay(719162L);

        // Act & Assert
        try {
            chronology.localDateTime(incompatibleDate);
            fail("Expected a DateTimeException to be thrown due to incompatible chronology.");
        } catch (DateTimeException e) {
            // Assert: Verify the exception message indicates the failure to convert
            // from the incompatible TemporalAccessor type.
            String expectedMessage = "Unable to obtain ChronoLocalDateTime from TemporalAccessor: " +
                                     "class org.threeten.extra.chrono.EthiopicDate";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}