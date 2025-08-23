package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class demonstrates improvements for a test of the
 * {@link EthiopicChronology#isLeapDay(long)} method.
 *
 * The original test was auto-generated and lacked clarity. This version
 * is rewritten to be self-documenting and maintainable.
 */
// The class name has been improved from the generated "EthiopicChronology_ESTestTest15".
public class EthiopicChronologyTest {

    @Test
    public void isLeapDay_returnsFalse_forRegularDay() {
        // Arrange: Set up the test objects and data.
        // The Ethiopic leap day is the 6th day of the 13th month (Pagumē), which is the 366th day of the year.
        // We choose January 15, 1970, a date early in the Gregorian year, which is guaranteed
        // not to be the 366th day of the corresponding Ethiopic year (1962).
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        DateTime aRegularDay = new DateTime(1970, 1, 15, 0, 0, DateTimeZone.UTC);
        long instant = aRegularDay.getMillis();

        // Act: Call the method under test.
        boolean isLeapDay = ethiopicChronology.isLeapDay(instant);

        // Assert: Verify the outcome.
        assertFalse("A regular day like Jan 15, 1970, should not be considered a leap day.", isLeapDay);
    }
}