package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * A more understandable test for the {@link EthiopicChronology} class.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that isLeapDay() correctly identifies the 6th day of the 13th month
     * in an Ethiopic leap year.
     */
    @Test
    public void isLeapDay_shouldReturnTrue_forLeapDayInEthiopicLeapYear() {
        // Arrange:
        // In the Ethiopic calendar, a leap year occurs when (year % 4) == 3.
        // The year 2071 is a leap year because 2071 % 4 = 3.
        // The leap day is the 6th day of the 13th month (Pagume).
        final EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        
        // Construct the specific date for the leap day instead of using a magic number.
        final DateTime ethiopicLeapDay = new DateTime(2071, 13, 6, 0, 0, ethiopicChronology);
        final long leapDayInstant = ethiopicLeapDay.getMillis();

        // Act:
        // Check if the calculated instant is considered a leap day.
        boolean isLeapDay = ethiopicChronology.isLeapDay(leapDayInstant);

        // Assert:
        // The result should be true for this specific day.
        assertTrue("The 6th day of the 13th month in Ethiopic year 2071 should be a leap day.", isLeapDay);
    }
}