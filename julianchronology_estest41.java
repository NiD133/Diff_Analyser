package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JulianChronology}.
 * This class contains the improved test case.
 */
public class JulianChronologyTest {

    /**
     * Tests that the date() method throws a ClassCastException when an Era
     * from a different chronology is provided.
     */
    @Test
    public void date_withWrongEraType_throwsClassCastException() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        Era nonJulianEra = JapaneseEra.HEISEI; // Use an Era from a different calendar system

        // Act & Assert
        try {
            // Attempt to create a date with the incompatible era
            julianChronology.date(nonJulianEra, 2023, 1, 1);
            fail("Expected a ClassCastException to be thrown due to incompatible Era type.");
        } catch (ClassCastException e) {
            // Verify that the exception is thrown for the correct reason
            assertEquals("Era must be JulianEra", e.getMessage());
        }
    }
}