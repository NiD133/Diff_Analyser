package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;

/**
 * This test class contains an improved version of a generated test case
 * for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Verifies that creating a date with an unsupported era type throws a ClassCastException.
     *
     * The Symmetry454 calendar system is aligned with the ISO calendar and uses IsoEra (CE/BCE).
     * Providing an era from a different system, like the Hijrah calendar's 'AH' era,
     * is invalid and should be rejected with a type-cast exception.
     */
    @Test(expected = ClassCastException.class)
    public void date_whenEraIsFromUnsupportedChronology_throwsClassCastException() {
        // Arrange: Get the singleton instance of the chronology and an era from an
        // incompatible calendar system (Hijrah).
        Symmetry454Chronology symmetry454Chronology = Symmetry454Chronology.INSTANCE;
        Era unsupportedEra = HijrahEra.AH;

        // Act & Assert: Attempt to create a date using the unsupported era.
        // The @Test(expected) annotation asserts that a ClassCastException will be thrown.
        // The year, month, and day values are arbitrary, as the type check on the era happens first.
        symmetry454Chronology.date(unsupportedEra, 2024, 1, 1);
    }
}