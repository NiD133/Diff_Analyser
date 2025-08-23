package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.YearMonthDay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests year additions in ISOChronology, especially around the historical
 * Gregorian cutover period to ensure the proleptic calendar behaves correctly.
 */
@RunWith(Parameterized.class)
public class ISOChronologyYearAdditionTest {

    private static final Chronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();
    private static final DurationFieldType YEARS_FIELD = DurationFieldType.years();

    private final String startDateTimeStr;
    private final String endDateTimeStr;
    private final int yearsToAdd;

    public ISOChronologyYearAdditionTest(String startDateTimeStr, String endDateTimeStr, int yearsToAdd) {
        this.startDateTimeStr = startDateTimeStr;
        this.endDateTimeStr = endDateTimeStr;
        this.yearsToAdd = yearsToAdd;
    }

    @Parameters(name = "Test {index}: {0} + {2} year(s) = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            // Test cases around 1582, the year of the historical Gregorian calendar adoption.
            // ISOChronology is proleptic, so it should handle these additions linearly
            // without any "cutover" gap.

            // --- Adding one year ---
            {"1582-01-01", "1583-01-01", 1},
            {"1582-02-15", "1583-02-15", 1},
            {"1582-02-28", "1583-02-28", 1}, // End of month in a non-leap year
            {"1582-03-01", "1583-03-01", 1},
            {"1582-09-30", "1583-09-30", 1},
            {"1582-10-01", "1583-10-01", 1},
            {"1582-10-04", "1583-10-04", 1}, // Date just before the historical cutover
            {"1582-10-15", "1583-10-15", 1}, // Date just after the historical cutover
            {"1582-10-16", "1583-10-16", 1},

            // --- Adding four years to cross a leap year (1584) ---
            {"1580-01-01", "1584-01-01", 4},
            {"1580-02-29", "1584-02-29", 4}, // Leap day to leap day
            {"1580-10-01", "1584-10-01", 4},
            {"1580-10-10", "1584-10-10", 4},
            {"1580-10-15", "1584-10-15", 4},
            {"1580-12-31", "1584-12-31", 4},
        });
    }

    @Test
    public void testYearAdditionIsCorrect() {
        DateTime dtStart = new DateTime(startDateTimeStr, UTC_CHRONOLOGY);
        DateTime dtEnd = new DateTime(endDateTimeStr, UTC_CHRONOLOGY);

        // 1. Test forward addition
        assertEquals("Forward addition of years failed.",
            dtEnd, dtStart.withFieldAdded(YEARS_FIELD, yearsToAdd));

        // 2. Test backward addition (reversibility)
        assertEquals("Backward addition of years failed.",
            dtStart, dtEnd.withFieldAdded(YEARS_FIELD, -yearsToAdd));

        // 3. Test difference calculation
        DurationField yearDurationField = YEARS_FIELD.getField(UTC_CHRONOLOGY);
        int differenceInYears = yearDurationField.getDifference(dtEnd.getMillis(), dtStart.getMillis());
        assertEquals("Calculating the difference in years failed.", yearsToAdd, differenceInYears);

        // 4. Test addition on a date-only partial (YearMonthDay)
        YearMonthDay ymdStart = new YearMonthDay(startDateTimeStr, UTC_CHRONOLOGY);
        YearMonthDay ymdEnd = new YearMonthDay(endDateTimeStr, UTC_CHRONOLOGY);
        assertEquals("Forward addition for YearMonthDay failed.",
            ymdEnd, ymdStart.withFieldAdded(YEARS_FIELD, yearsToAdd));
        assertEquals("Backward addition for YearMonthDay failed.",
            ymdStart, ymdEnd.withFieldAdded(YEARS_FIELD, -yearsToAdd));
    }
}