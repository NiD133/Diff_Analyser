package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    private static final int DAYS_IN_ISLAMIC_COMMON_YEAR = 354;

    @Test
    public void getDaysInYear_shouldReturn354ForCommonYear() {
        // Arrange: The Islamic calendar has common years (354 days) and leap years (355 days).
        // Year 1 AH is a common year according to the standard leap year patterns.
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int commonYear = 1;

        // Act: Calculate the number of days in year 1.
        int actualDays = islamicChronology.getDaysInYear(commonYear);

        // Assert: Verify that the result is correct for a common year.
        assertEquals("A common Islamic year should have 354 days",
                     DAYS_IN_ISLAMIC_COMMON_YEAR, actualDays);
    }
}