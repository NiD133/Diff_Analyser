package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    @Test
    public void calculateFirstDayOfYearMillis_shouldReturnCorrectMillisForEarlyADYear() {
        // Arrange
        // Get an instance of the GregorianChronology in UTC.
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        int year = 20;

        // For verification, create a DateTime object representing the start of the given year (0020-01-01T00:00:00Z).
        // This makes the expected value self-documenting and avoids "magic numbers".
        DateTime startOfYear20 = new DateTime(year, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        long expectedMillis = startOfYear20.getMillis();

        // Act
        // Calculate the milliseconds for the first day of the year using the method under test.
        long actualMillis = gregorianChronology.calculateFirstDayOfYearMillis(year);

        // Assert
        // The calculated millis should match the millis from the reference DateTime object.
        assertEquals(expectedMillis, actualMillis);
    }
}