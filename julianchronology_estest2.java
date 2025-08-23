package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

/**
 * Unit tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a date with a day-of-year value outside the valid range
     * of 1-366 throws a DateTimeException.
     */
    @Test(expected = DateTimeException.class)
    public void dateYearDay_throwsExceptionForInvalidDayOfYear() {
        // Arrange: Set up inputs for the method call.
        // The day of year 1461 is intentionally chosen as it's far outside the valid range.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JulianEra era = JulianEra.BC;
        int year = 2493;
        int invalidDayOfYear = 1461;

        // Act: Call the method under test.
        // This call is expected to throw a DateTimeException due to the invalid dayOfYear.
        julianChronology.dateYearDay(era, year, invalidDayOfYear);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}