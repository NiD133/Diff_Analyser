package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import static org.junit.Assert.assertEquals;

/**
 * Test class for JulianChronology.
 * Note: The original test class name 'JulianChronology_ESTestTest11' was renamed for clarity.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a JulianDate from a TemporalAccessor of a different
     * chronology (JapaneseDate) results in the correct date conversion.
     */
    @Test
    public void dateFromTemporal_whenInputIsJapaneseDate_shouldReturnCorrectJulianDate() {
        // --- Arrange ---
        // The Japanese date for Heisei 24, June 18th, corresponds to the ISO date 2012-06-18.
        JapaneseDate japaneseDate = JapaneseDate.of(JapaneseEra.HEISEI, 24, 6, 18);

        // In the 21st century, the Julian calendar is 13 days behind the Gregorian (ISO) calendar.
        // Therefore, the ISO date 2012-06-18 is equivalent to the Julian date 2012-06-05.
        JulianDate expectedJulianDate = JulianDate.of(2012, 6, 5);
        
        // --- Act ---
        // The method under test: convert the JapaneseDate to a JulianDate.
        JulianDate actualJulianDate = JulianChronology.INSTANCE.date(japaneseDate);

        // --- Assert ---
        assertEquals("Conversion from JapaneseDate should produce the correct JulianDate",
                expectedJulianDate, actualJulianDate);
    }
}