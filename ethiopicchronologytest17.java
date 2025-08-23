package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the duration field for the 'monthOfYear' property in EthiopicChronology.
 * It specifically focuses on behavior around leap years, where the 13th month has a different length.
 */
public class EthiopicChronologyMonthDurationTest {

    private static final int DAYS_IN_ETHIOPIC_MONTH = 30;
    private static final int DAYS_IN_LEAP_YEAR_PAGUME = 6; // Pagume is the 13th month
    private static final long MILLIS_IN_REGULAR_ETHIOPIC_MONTH = (long) DAYS_IN_ETHIOPIC_MONTH * DateTimeConstants.MILLIS_PER_DAY;

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

    private DurationField monthDurationField;

    // A date in the 11th month of Ethiopic leap year 1999.
    private DateTime dateInLeapYearMonth11;
    
    // Expected dates after adding months to the above start date.
    private DateTime dateInLeapYearMonth12;
    private DateTime dateInLeapYearMonth13;
    private DateTime dateInFollowingYearMonth1;

    @Before
    public void setUp() {
        // The test focuses on the duration of months around the end of a leap year.
        // Ethiopic year 1999 is a leap year (1999 % 4 == 3).
        // This means its 13th month (Pagume) has 6 days instead of the usual 5.
        dateInLeapYearMonth11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        monthDurationField = dateInLeapYearMonth11.monthOfYear().getDurationField();

        // Pre-calculate the expected dates when adding months to our start date.
        dateInLeapYearMonth12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        dateInLeapYearMonth13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        dateInFollowingYearMonth1 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
    }

    @Test
    public void testGetDurationField_returnsMonths() {
        assertEquals("The duration field for monthOfYear should be the 'months' field of the chronology",
                ETHIOPIC_UTC.months(), monthDurationField);
    }

    @Test
    public void testAdd_handlesLeapYearCorrectly() {
        // Verifies that adding months correctly crosses the boundary of the 13th month in a leap year.
        assertEquals(dateInLeapYearMonth12.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 1));
        assertEquals(dateInLeapYearMonth13.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 2));
        assertEquals(dateInFollowingYearMonth1.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 3));

        // Test with long parameter overloads
        assertEquals(dateInLeapYearMonth12.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 1L));
        assertEquals(dateInLeapYearMonth13.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 2L));
        assertEquals(dateInFollowingYearMonth1.getMillis(), monthDurationField.add(dateInLeapYearMonth11.getMillis(), 3L));
    }

    @Test
    public void testGetMillisWithContext_handlesLeapYearCorrectly() {
        long startMillis = dateInLeapYearMonth11.getMillis();

        // Duration of 1 month (month 11) = 30 days
        assertEquals(MILLIS_IN_REGULAR_ETHIOPIC_MONTH, monthDurationField.getMillis(1, startMillis));

        // Duration of 2 months (months 11, 12) = 30 + 30 days
        long expectedDuration2Months = 2 * MILLIS_IN_REGULAR_ETHIOPIC_MONTH;
        assertEquals(expectedDuration2Months, monthDurationField.getMillis(2, startMillis));

        // Duration of 3 months (months 11, 12, 13) = 30 + 30 + 6 days (13th month in leap year)
        long millisInLeapPagume = (long) DAYS_IN_LEAP_YEAR_PAGUME * DateTimeConstants.MILLIS_PER_DAY;
        long expectedDuration3Months = expectedDuration2Months + millisInLeapPagume;
        assertEquals(expectedDuration3Months, monthDurationField.getMillis(3, startMillis));
        
        // Duration of 4 months (months 11, 12, 13 of 1999 and month 1 of 2000) = 30 + 30 + 6 + 30 days
        long expectedDuration4Months = expectedDuration3Months + MILLIS_IN_REGULAR_ETHIOPIC_MONTH;
        assertEquals(expectedDuration4Months, monthDurationField.getMillis(4, startMillis));
    }

    @Test
    public void testGetMillisWithoutContext_isBasedOn30DayAverage() {
        // When no context instant is provided, getMillis should use the fixed average length of 30 days.
        assertEquals(1L * MILLIS_IN_REGULAR_ETHIOPIC_MONTH, monthDurationField.getMillis(1));
        assertEquals(2L * MILLIS_IN_REGULAR_ETHIOPIC_MONTH, monthDurationField.getMillis(2));
        assertEquals(13L * MILLIS_IN_REGULAR_ETHIOPIC_MONTH, monthDurationField.getMillis(13));
    }

    @Test
    public void testGetValueWithContext_handlesLeapYearCorrectly() {
        long startMillis = dateInLeapYearMonth11.getMillis();
        long millisInLeapPagume = (long) DAYS_IN_LEAP_YEAR_PAGUME * DateTimeConstants.MILLIS_PER_DAY;

        // --- Test around the 1-month boundary (1 month = 30 days) ---
        long duration1Month = MILLIS_IN_REGULAR_ETHIOPIC_MONTH;
        assertEquals(0, monthDurationField.getValue(duration1Month - 1, startMillis));
        assertEquals(1, monthDurationField.getValue(duration1Month, startMillis));

        // --- Test around the 2-month boundary (2 months = 30 + 30 = 60 days) ---
        long duration2Months = 2 * MILLIS_IN_REGULAR_ETHIOPIC_MONTH;
        assertEquals(1, monthDurationField.getValue(duration2Months - 1, startMillis));
        assertEquals(2, monthDurationField.getValue(duration2Months, startMillis));

        // --- Test around the 3-month boundary (3 months = 30 + 30 + 6 = 66 days) ---
        long duration3Months = duration2Months + millisInLeapPagume;
        assertEquals(2, monthDurationField.getValue(duration3Months - 1, startMillis));
        assertEquals(3, monthDurationField.getValue(duration3Months, startMillis));

        // --- Test around the 4-month boundary (4 months = 30 + 30 + 6 + 30 = 96 days) ---
        long duration4Months = duration3Months + MILLIS_IN_REGULAR_ETHIOPIC_MONTH;
        assertEquals(3, monthDurationField.getValue(duration4Months - 1, startMillis));
        assertEquals(4, monthDurationField.getValue(duration4Months, startMillis));
    }
}