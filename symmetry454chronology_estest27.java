package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test class contains tests for the Symmetry454Chronology class.
 * The original test class name is kept to match the input, but in a real-world scenario,
 * it would be renamed to something more descriptive, like Symmetry454ChronologyTest.
 */
public class Symmetry454Chronology_ESTestTest27 extends Symmetry454Chronology_ESTest_scaffolding {

    /**
     * Tests that attempting to create a Symmetry454Date from a TemporalAccessor
     * that only represents an Era throws an exception. An Era alone does not provide
     * enough information (specifically, the EPOCH_DAY field) to construct a complete date.
     */
    @Test
    public void date_fromTemporalAccessorContainingOnlyEra_throwsException() {
        // Arrange: Get the chronology instance and create a TemporalAccessor that only holds Era info.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        TemporalAccessor eraOnlyAccessor = IsoEra.CE;

        // Act & Assert: Verify that calling date() with this accessor throws the expected exception.
        UnsupportedTemporalTypeException exception = assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> chronology.date(eraOnlyAccessor)
        );

        // Further Assert: Check the exception message to confirm it failed for the correct reason.
        assertEquals("Unsupported field: EpochDay", exception.getMessage());
    }
}