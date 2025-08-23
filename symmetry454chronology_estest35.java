package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.TemporalAccessor;

/**
 * This test class contains tests for the Symmetry454Chronology class.
 * This specific test case focuses on the behavior of the localDateTime() method.
 */
// The original test class name is kept, but a more descriptive name like
// Symmetry454ChronologyTest would be preferable in a real-world scenario.
public class Symmetry454Chronology_ESTestTest35 extends Symmetry454Chronology_ESTest_scaffolding {

    /**
     * Verifies that calling localDateTime() with a null TemporalAccessor
     * throws a NullPointerException.
     *
     * The method is expected to delegate to other internal methods that perform
     * a null check on the input.
     */
    @Test(expected = NullPointerException.class)
    public void localDateTime_withNullTemporal_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        // The constructor is deprecated, so using the INSTANCE field is the correct approach.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act & Assert: Call localDateTime with a null input.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        chronology.localDateTime((TemporalAccessor) null);
    }
}