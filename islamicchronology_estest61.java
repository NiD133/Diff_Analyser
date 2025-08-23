package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This class contains unit tests for the {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the maximum number of days in a year for the Islamic calendar is correctly reported.
     * <p>
     * The Islamic calendar has 354 days in a common year and 355 days in a leap year.
     * Therefore, the getDaysInYearMax() method should return 355.
     */
    @Test
    public void getDaysInYearMax_shouldReturn355() {
        // Arrange: Get a standard instance of the IslamicChronology.
        // The specific time zone (UTC) does not affect this calculation.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        
        // Act: Retrieve the maximum number of days in a year.
        int maxDaysInYear = islamicChronology.getDaysInYearMax();
        
        // Assert: The maximum number of days should be 355, corresponding to a leap year.
        assertEquals(355, maxDaysInYear);
    }
}