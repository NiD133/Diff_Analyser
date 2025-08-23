package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the maximum number of days in any month is correctly reported as 30.
     *
     * According to the Islamic calendar's tabular form, months have either 29 or 30 days.
     * Therefore, the maximum number of days possible in a month is 30.
     */
    @Test
    public void getDaysInMonthMax_shouldReturn30() {
        // Arrange: Get an instance of the IslamicChronology.
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int expectedMaxDays = 30;

        // Act: Call the method under test.
        int actualMaxDays = islamicChronology.getDaysInMonthMax();

        // Assert: Verify that the result is 30.
        assertEquals("The maximum days in any Islamic month should be 30.", expectedMaxDays, actualMaxDays);
    }
}