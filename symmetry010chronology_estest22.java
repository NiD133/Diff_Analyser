package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.ThaiBuddhistEra;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This focuses on validating method contracts and handling of invalid inputs.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that dateYearDay() throws a ClassCastException when an era
     * other than IsoEra is provided. The Symmetry010Chronology is documented
     * to only support IsoEra.
     */
    @Test(expected = ClassCastException.class)
    public void dateYearDay_withNonIsoEra_throwsClassCastException() {
        // Arrange: Get the chronology instance and an era from a different calendar system.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era invalidEra = ThaiBuddhistEra.BEFORE_BE;
        int yearOfEra = 2023;
        int dayOfYear = 100;

        // Act: Attempt to create a date with the unsupported era.
        // This call is expected to fail with a ClassCastException.
        chronology.dateYearDay(invalidEra, yearOfEra, dayOfYear);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}