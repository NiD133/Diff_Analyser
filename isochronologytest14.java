package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.YearMonthDay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests the behavior of adding months to a date using the ISOChronology.
 * This test focuses on ensuring that adding months to both DateTime and YearMonthDay
 * objects produces correct and consistent results, especially across leap years.
 */
@RunWith(Parameterized.class)
public class ISOChronologyMonthAdditionTest {

    private static final Chronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();
    private static final DurationFieldType MONTHS_TYPE = DurationFieldType.months();

    private final String startYMD;
    private final int monthsToAdd;
    private final String expectedYMD;

    public ISOChronologyMonthAdditionTest(String startYMD, int monthsToAdd, String expectedYMD) {
        this.startYMD = startYMD;
        this.monthsToAdd = monthsToAdd;
        this.expectedYMD = expectedYMD;
    }

    @Parameters(name = "{0} plus {1} months is {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"1582-01-01", 1, "1582-02-01"},
            {"1582-01-01", 6, "1582-07-01"},
            {"1582-01-01", 12, "1583-01-01"},
            {"1582-11-15", 1, "1582-12-15"},
            {"1582-09-04", 2, "1582-11-04"},
            // Test adding a large number of months, crossing over a leap year
            {"1580-01-01", 48, "1584-01-01"},
            // Test adding months to a leap day, resulting in a leap day
            {"1580-02-29", 48, "1584-02-29"},
            {"1580-10-01", 48, "1584-10-01"},
            // Test adding months from the end of a year
            {"1580-12-31", 48, "1584-12-31"},
        });
    }

    @Test
    public void testMonthAddition() {
        // Test with DateTime
        DateTime dtStart = new DateTime(startYMD, UTC_CHRONOLOGY);
        DateTime dtEnd = new DateTime(expectedYMD, UTC_CHRONOLOGY);

        assertEquals("Forward addition failed for DateTime", dtEnd, dtStart.withFieldAdded(MONTHS_TYPE, monthsToAdd));
        assertEquals("Backward addition failed for DateTime", dtStart, dtEnd.withFieldAdded(MONTHS_TYPE, -monthsToAdd));

        DurationField monthField = MONTHS_TYPE.getField(UTC_CHRONOLOGY);
        assertEquals("Difference calculation failed for DateTime", monthsToAdd, monthField.getDifference(dtEnd.getMillis(), dtStart.getMillis()));

        // Test with YearMonthDay for consistency
        YearMonthDay ymdStart = new YearMonthDay(startYMD, UTC_CHRONOLOGY);
        YearMonthDay ymdEnd = new YearMonthDay(expectedYMD, UTC_CHRONOLOGY);

        assertEquals("Forward addition failed for YearMonthDay", ymdEnd, ymdStart.withFieldAdded(MONTHS_TYPE, monthsToAdd));
        assertEquals("Backward addition failed for YearMonthDay", ymdStart, ymdEnd.withFieldAdded(MONTHS_TYPE, -monthsToAdd));
    }
}