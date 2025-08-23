package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that {@link JulianChronology#dateYearDay(Era, int, int)}
     * correctly creates a date for a given era, year, and day of the year.
     */
    @Test
    public void dateYearDay_withValidAdEraYearAndDay_createsCorrectDate() {
        // Arrange: Set up the input values for the test.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int yearOfEra = 84;
        int dayOfYear = 84;

        // Act: Call the method under test.
        JulianDate resultDate = julianChronology.dateYearDay(JulianEra.AD, yearOfEra, dayOfYear);

        // Assert: Verify that the created date has the correct properties.
        assertEquals("The era should be AD.", JulianEra.AD, resultDate.getEra());
        assertEquals("The year of era should match the input.", yearOfEra, resultDate.get(ChronoField.YEAR_OF_ERA));
        assertEquals("The day of year should match the input.", dayOfYear, resultDate.get(ChronoField.DAY_OF_YEAR));
    }
}