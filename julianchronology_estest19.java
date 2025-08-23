package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    @Test
    public void dateYearDay_whenEraIsIncorrectType_throwsClassCastException() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        Era wrongEra = JapaneseEra.HEISEI; // An era that is not a JulianEra
        int anyYear = 1990;                // Arbitrary valid year
        int anyDayOfYear = 100;            // Arbitrary valid day of year

        // Act & Assert
        try {
            chronology.dateYearDay(wrongEra, anyYear, anyDayOfYear);
            fail("Expected a ClassCastException to be thrown.");
        } catch (ClassCastException e) {
            // The method should reject any Era that is not a JulianEra.
            assertEquals("Era must be JulianEra", e.getMessage());
        }
    }
}