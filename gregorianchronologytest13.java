package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests the behavior of the isLeap() methods for various date-time fields
 * within the GregorianChronology.
 */
public class GregorianChronologyLeapLogicTest {

    private static final Chronology GREGORIAN_UTC = GregorianChronology.getInstance(DateTimeZone.UTC);

    /**
     * Verifies the isLeap() status for different fields of a date in a leap year.
     * The test uses February 28, 2012, which is a leap year.
     */
    @Test
    public void isLeap_forDateInLeapYear_returnsCorrectStatusForEachField() {
        // Arrange: A date on February 28th in a known leap year (2012).
        // 2012 is divisible by 4 but not by 100, so it is a leap year.
        DateTime dateInLeapYear = new DateTime(2012, 2, 28, 0, 0, GREGORIAN_UTC);

        // Act & Assert

        // 1. The year 2012 is a leap year.
        assertTrue("Year 2012 should be considered a leap year.", dateInLeapYear.year().isLeap());

        // 2. The month of February is considered "leap" if it's in a leap year,
        //    because it contains the extra leap day (Feb 29th).
        assertTrue("February in a leap year should be considered a leap month.", dateInLeapYear.monthOfYear().isLeap());

        // 3. The day itself (the 28th) is not the leap day. Only Feb 29th would be.
        assertFalse("The 28th day of the month should not be a leap day.", dateInLeapYear.dayOfMonth().isLeap());

        // 4. The day of the year is not the leap day. The 60th day (Feb 29th) would be.
        assertFalse("The 59th day of a leap year (Feb 28th) should not be a leap day.", dateInLeapYear.dayOfYear().isLeap());
    }
}