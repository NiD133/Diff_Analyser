package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that getDaysInMonthMax(long) returns 30 for an instant that falls
     * within a month known to have 30 days.
     */
    @Test
    public void getDaysInMonthMax_forInstantIn30DayMonth_returns30() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // In the Islamic calendar, odd-numbered months (like the 1st month, Muharram) have 30 days.
        // We create a DateTime for the first day of Muharram in the year 50 AH.
        // This specific date corresponds to the magic number -42215385600000L in the original test.
        DateTime dateTimeInMuharram = new DateTime(50, 1, 1, 0, 0, islamicChronology);
        long instant = dateTimeInMuharram.getMillis();

        // Act
        int daysInMonth = islamicChronology.getDaysInMonthMax(instant);

        // Assert
        final int expectedDays = 30;
        assertEquals("The first month (Muharram) should have 30 days", expectedDays, daysInMonth);
    }
}