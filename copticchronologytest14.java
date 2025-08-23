package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;

/**
 * Tests the CopticChronology by converting a sample ISO date and verifying its fields.
 *
 * <p>The sample date is ISO 2004-06-09, which corresponds to 1720-10-02 in the Coptic calendar.
 */
public class TestCopticChronologyWithSampleDate extends TestCase {

    // The CopticChronology instance used for testing, fixed to UTC for consistency.
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // The specific date used for testing, created once in setUp().
    private DateTime copticDate;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    @Override
    protected void setUp() throws Exception {
        // Store original default values
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set defaults to a known state to ensure consistent test results
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);

        // The test subject is an ISO date (2004-06-09) converted to the Coptic calendar.
        DateTime isoDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC);
        copticDate = isoDate.withChronology(COPTIC_UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original default values
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    public void testIsoToCopticDateConversion() {
        // Assert that ISO 2004-06-09 correctly converts to Coptic 1720-10-02
        assertEquals("Era should be AM", CopticChronology.AM, copticDate.getEra());
        assertEquals("Year", 1720, copticDate.getYear());
        assertEquals("Month of Year", 10, copticDate.getMonthOfYear());
        assertEquals("Day of Month", 2, copticDate.getDayOfMonth());
    }

    public void testEraBasedDateFields() {
        // For Coptic year 1720:
        // Century of Era = floor((1720 - 1) / 100) + 1 = 18
        // Year of Century = 1720 % 100 = 20 (or 100 if year is multiple of 100)
        assertEquals("Century of Era", 18, copticDate.getCenturyOfEra());
        assertEquals("Year of Century", 20, copticDate.getYearOfCentury());
        assertEquals("Year of Era", 1720, copticDate.getYearOfEra());
    }

    public void testYearFieldProperties() {
        Property yearProperty = copticDate.year();

        assertFalse("Coptic year 1720 should not be a leap year", yearProperty.isLeap());
        assertEquals("Leap amount for a non-leap year should be 0", 0, yearProperty.getLeapAmount());
        assertEquals(DurationFieldType.days(), yearProperty.getLeapDurationField().getType());

        DateTime nextYear = yearProperty.addToCopy(1);
        assertEquals("Adding one year should result in 1721-10-02",
                new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC), nextYear);
    }

    public void testMonthOfYearFieldProperties() {
        Property monthProperty = copticDate.monthOfYear();

        assertFalse("Month property should not be leap", monthProperty.isLeap());
        assertEquals(0, monthProperty.getLeapAmount());
        assertEquals(DurationFieldType.days(), monthProperty.getLeapDurationField().getType());
        assertEquals("Minimum month is 1", 1, monthProperty.getMinimumValue());
        assertEquals("Maximum month is 13", 13, monthProperty.getMaximumValue());

        // Adding 4 months to month 10 (Paoni) should result in month 1 (Tout) of the next year.
        DateTime futureMonth = monthProperty.addToCopy(4);
        assertEquals("Adding 4 months should roll over to the next year",
                new DateTime(1721, 1, 2, 0, 0, 0, 0, COPTIC_UTC), futureMonth);

        // Wrapping 4 months should result in month 1 of the same year.
        DateTime wrappedMonth = monthProperty.addWrapFieldToCopy(4);
        assertEquals("Wrapping 4 months should stay in the same year",
                new DateTime(1720, 1, 2, 0, 0, 0, 0, COPTIC_UTC), wrappedMonth);
    }

    public void testDayOfMonthFieldProperties() {
        Property dayOfMonthProperty = copticDate.dayOfMonth();

        assertFalse("Day of month property should not be leap", dayOfMonthProperty.isLeap());
        assertEquals(0, dayOfMonthProperty.getLeapAmount());
        assertNull(dayOfMonthProperty.getLeapDurationField());
        assertEquals("Minimum day is 1", 1, dayOfMonthProperty.getMinimumValue());
        assertEquals("Maximum day is 30 for this month", 30, dayOfMonthProperty.getMaximumValue());

        DateTime nextDay = dayOfMonthProperty.addToCopy(1);
        assertEquals("Adding one day should result in 1720-10-03",
                new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), nextDay);
    }

    public void testDayOfWeekFieldProperties() {
        assertEquals(DateTimeConstants.WEDNESDAY, copticDate.getDayOfWeek());

        Property dayOfWeekProperty = copticDate.dayOfWeek();
        assertFalse("Day of week property should not be leap", dayOfWeekProperty.isLeap());
        assertEquals(1, dayOfWeekProperty.getMinimumValue());
        assertEquals(7, dayOfWeekProperty.getMaximumValue());

        DateTime nextDay = dayOfWeekProperty.addToCopy(1);
        assertEquals("Adding one day via dayOfWeek should result in Thursday",
                new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), nextDay);
    }

    public void testDayOfYearFieldProperties() {
        // Day of year for 1720-10-02 is (9 full months * 30 days/month) + 2 days = 272
        assertEquals(272, copticDate.getDayOfYear());

        Property dayOfYearProperty = copticDate.dayOfYear();
        assertFalse("Day of year property should not be leap", dayOfYearProperty.isLeap());
        assertEquals(1, dayOfYearProperty.getMinimumValue());
        assertEquals("Max days in a non-leap Coptic year is 365", 365, dayOfYearProperty.getMaximumValue());
        assertEquals("Max days overall includes leap years", 366, dayOfYearProperty.getMaximumValueOverall());

        DateTime nextDay = dayOfYearProperty.addToCopy(1);
        assertEquals("Adding one day via dayOfYear should result in day 273",
                new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), nextDay);
    }

    public void testTimeFieldsAreUnchanged() {
        assertEquals(0, copticDate.getHourOfDay());
        assertEquals(0, copticDate.getMinuteOfHour());
        assertEquals(0, copticDate.getSecondOfMinute());
        assertEquals(0, copticDate.getMillisOfSecond());
    }
}