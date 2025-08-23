package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoUnit;
import static org.junit.Assert.assertEquals;

/**
 * Tests for date manipulation within the Julian calendar system.
 */
public class JulianDateManipulationTest {

    /**
     * Tests that subtracting a small number of days from an early AD date
     * results in a correct date that remains within the AD era.
     */
    @Test
    public void testMinusDaysFromEarlyAdDateRemainsInAdEra() {
        // Arrange: Define an initial date of March 3rd, 3 AD in the Julian calendar.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JulianDate initialDate = julianChronology.date(3, 3, 3);
        JulianDate expectedDate = julianChronology.date(3, 2, 28); // Expected: Feb 28th, 3 AD

        // Act: Subtract 3 days from the initial date.
        JulianDate resultDate = initialDate.minus(3, ChronoUnit.DAYS);

        // Assert: Verify the resulting date is correct and the era has not changed.
        assertEquals("The resulting date should be Feb 28th, 3 AD", expectedDate, resultDate);
        assertEquals("The era should remain AD", JulianEra.AD, resultDate.getEra());
    }
}