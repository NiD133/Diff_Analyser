package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Verifies that prolepticYear() throws a ClassCastException when an era
     * from an incorrect chronology (e.g., Japanese) is provided.
     * The method is specified to only accept a JulianEra.
     */
    @Test
    public void prolepticYear_throwsClassCastException_forNonJulianEra() {
        // Arrange: Set up the test objects. We need the chronology under test
        // and an era from a different, incompatible chronology.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        Era invalidEra = JapaneseEra.HEISEI;
        int yearOfEra = 1; // An arbitrary valid year for the era.

        // Act & Assert: We expect the method call to throw a ClassCastException.
        try {
            julianChronology.prolepticYear(invalidEra, yearOfEra);
            fail("Expected a ClassCastException, but it was not thrown.");
        } catch (ClassCastException ex) {
            // Verify that the exception has the expected message, confirming
            // the reason for the failure is correct.
            assertEquals("Era must be JulianEra", ex.getMessage());
        }
    }
}