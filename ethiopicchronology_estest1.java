package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link EthiopicChronology}.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that isLeapDay() returns false for a date that is not the
     * extra leap day in the Ethiopic calendar.
     *
     * The original test used the magic number -56623968000000L, which
     * corresponds to the Gregorian date 180-02-28T00:00:00Z. This date
     * is not a leap day in the Ethiopic calendar.
     */
    @Test
    public void isLeapDay_whenDateIsNotEthiopicLeapDay_returnsFalse() {
        // Arrange: Create an instance of the EthiopicChronology in a fixed timezone (UTC)
        // to ensure the test is deterministic.
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(DateTimeZone.UTC);
        
        // The instant corresponds to the Gregorian date February 28, 180 AD.
        // In the Ethiopic calendar, a leap day is the 6th epagomenal day at the end of a leap year.
        // This specific date is not one of those days.
        DateTime nonLeapDayInstant = new DateTime(180, 2, 28, 0, 0, DateTimeZone.UTC);

        // Act: Check if this instant is considered a leap day.
        boolean isLeapDay = ethiopicChronology.isLeapDay(nonLeapDayInstant.getMillis());

        // Assert: The result should be false.
        assertFalse("The date should not be identified as a leap day in the Ethiopic calendar.", isLeapDay);
    }
}