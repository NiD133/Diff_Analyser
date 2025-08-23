package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the IslamicChronology class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the maximum number of days for the 12th month (Dhú'l-Hijja) is 30.
     * <p>
     * In the Islamic calendar, the 12th month has 29 days in a common year
     * and is extended to 30 days in a leap year. Therefore, its maximum possible
     * number of days is 30.
     */
    @Test
    public void maxDaysInLastMonth_shouldBe30() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int lastMonthOfYear = 12;

        // Act
        int maxDays = islamicChronology.getDaysInMonthMax(lastMonthOfYear);

        // Assert
        assertEquals(30, maxDays);
    }
}