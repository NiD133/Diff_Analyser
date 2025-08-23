package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import org.junit.Test;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the date(Era, ...) method throws an exception when the provided
     * Era is not an IsoEra, which is the only type supported by this chronology.
     */
    @Test
    public void date_whenEraIsOfIncorrectType_throwsClassCastException() {
        // Arrange: Set up the test conditions.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        
        // The date(Era, ...) method in Symmetry010Chronology expects an IsoEra.
        // We use a JapaneseEra to test the invalid type handling.
        Era invalidEra = JapaneseEra.HEISEI;

        // Act & Assert: Perform the action and verify the outcome.
        try {
            // The actual date values (year, month, day) are not important
            // as the exception is thrown due to the era type check.
            chronology.date(invalidEra, 2000, 1, 1);
            fail("Symmetry010Chronology.date() should have thrown a ClassCastException for a non-IsoEra.");
        } catch (ClassCastException e) {
            // The exception is expected.
            // We verify that the exception message correctly identifies the invalid era.
            assertEquals("Invalid era: " + invalidEra, e.getMessage());
        }
    }
}