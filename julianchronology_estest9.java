package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a date with a positive proleptic year results in the AD era.
     * The Julian calendar considers years from 1 onwards as 'Anno Domini' (AD).
     */
    @Test
    public void dateYearDay_withPositiveYear_returnsDateInAdEra() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int prolepticYear = 327;
        int dayOfYear = 327; // Corresponds to November 23rd in a non-leap year.

        // Act
        JulianDate julianDate = julianChronology.dateYearDay(prolepticYear, dayOfYear);

        // Assert
        assertEquals("The era for a positive year should be AD", JulianEra.AD, julianDate.getEra());
    }
}