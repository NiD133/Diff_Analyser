package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;

/**
 * Tests for {@link InternationalFixedChronology#prolepticYear(Era, int)}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that prolepticYear() throws a ClassCastException when an era from a different
     * chronology is provided. The InternationalFixedChronology only supports its own era type.
     */
    @Test(expected = ClassCastException.class)
    public void prolepticYear_throwsExceptionForInvalidEraType() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // IsoEra is not a valid era for InternationalFixedChronology, which expects InternationalFixedEra
        Era invalidEra = IsoEra.BCE;
        int yearOfEra = 2023; // The specific year value does not affect this test's outcome

        // Act
        chronology.prolepticYear(invalidEra, yearOfEra);

        // Assert: ClassCastException is expected (handled by the @Test annotation)
    }
}