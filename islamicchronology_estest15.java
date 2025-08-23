package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    @Test
    public void getYearDifference_returnsCorrectlyWhenMinuendIsEarlierInItsYear() {
        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // Define two dates for the test.
        // The minuend (the date being subtracted from) is Feb 1, 1999.
        // In the Islamic calendar, this corresponds to year 1419.
        DateTime minuendDate = new DateTime(1999, 2, 1, 0, 0, DateTimeZone.UTC);

        // The subtrahend (the date being subtracted) is Jan 8, 1970.
        // In the Islamic calendar, this corresponds to year 1389.
        DateTime subtrahendDate = new DateTime(1970, 1, 8, 0, 0, DateTimeZone.UTC);

        // The simple difference in year numbers is 1419 - 1389 = 30.
        // However, getYearDifference() calculates the number of *full* years that have passed.
        // It does this by comparing the progress of each date within its respective Islamic year.
        //
        // In this specific case, Feb 1, 1999 occurs earlier in its Islamic year (1419)
        // than Jan 8, 1970 does in its Islamic year (1389).
        // Therefore, a full 30 years have not yet elapsed between the two instants.
        long expectedDifference = 29L;

        // Act
        long actualDifference = chronology.getYearDifference(minuendDate.getMillis(), subtrahendDate.getMillis());

        // Assert
        assertEquals("The number of full Islamic years should be 29", expectedDifference, actualDifference);
    }
}