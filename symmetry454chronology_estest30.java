package org.threeten.extra.chrono;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This class focuses on exception handling for the prolepticYear method.
 */
public class Symmetry454Chronology_ESTestTest30 {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void prolepticYear_throwsClassCastException_whenEraIsNotIsoEra() {
        // Arrange: The Symmetry454Chronology only supports IsoEra.
        // We use a different era type to trigger the exception.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era invalidEra = JapaneseEra.SHOWA;
        int yearOfEra = 29;

        // Assert: Configure expectations for the exception
        thrown.expect(ClassCastException.class);
        thrown.expectMessage("Invalid era: " + invalidEra);

        // Act: Call the method under test with the invalid era
        chronology.prolepticYear(invalidEra, yearOfEra);
    }
}