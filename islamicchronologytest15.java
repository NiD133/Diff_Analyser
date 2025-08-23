package org.joda.time.chrono;

import java.util.Locale;
import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

/**
 * Tests the fields of the IslamicChronology by converting a known ISO date
 * and verifying its properties.
 */
public class TestIslamicChronologyFields extends TestCase {

    // All calculations are done in UTC to avoid time zone complexities.
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // The specific ISO date we are converting from: 1945-11-12.
    private static final DateTime ISO_SAMPLE_DATE = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
    
    // Expected Islamic date components for the above ISO date.
    // ISO 1945-11-12 corresponds to 1364-12-06 AH (Dhu al-Hijjah 6, 1364).
    private static final int EXPECTED_ISLAMIC_YEAR = 1364;
    private static final int EXPECTED_ISLAMIC_MONTH = 12;
    private static final int EXPECTED_ISLAMIC_DAY = 6;

    private DateTime islamicDate;
    private Locale originalLocale;

    @Override
    protected void setUp() throws Exception {
        // Set a fixed locale for consistent test results, as some date logic can be locale-sensitive.
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.UK);

        // Convert the sample ISO date to the Islamic chronology for testing.
        islamicDate = ISO_SAMPLE_DATE.withChronology(ISLAMIC_UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        Locale.setDefault(originalLocale);
    }

    public void testDateConversion() {
        assertEquals(EXPECTED_ISLAMIC_YEAR, islamicDate.getYear());
        assertEquals(EXPECTED_ISLAMIC_MONTH, islamicDate.getMonthOfYear());
        assertEquals(EXPECTED_ISLAMIC_DAY, islamicDate.getDayOfMonth());
        assertEquals(DateTimeConstants.MONDAY, islamicDate.getDayOfWeek());
    }

    public void testYearProperties() {
        assertEquals(IslamicChronology.AH, islamicDate.getEra());
        assertEquals(14, islamicDate.getCenturyOfEra()); // 1301-1400 AH is the 14th century
        assertEquals(64, islamicDate.getYearOfCentury());
        assertEquals(EXPECTED_ISLAMIC_YEAR, islamicDate.getYearOfEra());

        Property year = islamicDate.year();
        assertFalse("Year 1364 should not be a leap year", year.isLeap());
        assertEquals(0, year.getLeapAmount());
        assertEquals(DurationFieldType.days(), year.getLeapDurationField().getType());

        // Adding one year should result in the same date in the following year.
        DateTime expectedNextYear = new DateTime(EXPECTED_ISLAMIC_YEAR + 1, EXPECTED_ISLAMIC_MONTH, EXPECTED_ISLAMIC_DAY, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(expectedNextYear, year.addToCopy(1));
    }

    public void testMonthOfYearProperties() {
        Property month = islamicDate.monthOfYear();
        assertFalse(month.isLeap());
        assertEquals(0, month.getLeapAmount());
        assertEquals(DurationFieldType.days(), month.getLeapDurationField().getType());
        assertEquals(1, month.getMinimumValue());
        assertEquals(12, month.getMaximumValue());

        // Adding one month to the last month of the year should roll over to the next year.
        DateTime expectedNextMonth = new DateTime(EXPECTED_ISLAMIC_YEAR + 1, 1, EXPECTED_ISLAMIC_DAY, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(expectedNextMonth, month.addToCopy(1));

        // Wrapping adds one month but stays within the same year, rolling from 12 to 1.
        DateTime expectedWrappedMonth = new DateTime(EXPECTED_ISLAMIC_YEAR, 1, EXPECTED_ISLAMIC_DAY, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(expectedWrappedMonth, month.addWrapFieldToCopy(1));
    }

    public void testDayOfMonthProperties() {
        Property day = islamicDate.dayOfMonth();
        assertFalse(day.isLeap());
        assertEquals(0, day.getLeapAmount());
        assertNull(day.getLeapDurationField());
        assertEquals(1, day.getMinimumValue());
        assertEquals(29, day.getMaximumValue()); // Month 12 in a non-leap year has 29 days.
        assertEquals(30, day.getMaximumValueOverall()); // Other months can have 30 days.

        DateTime expectedNextDay = new DateTime(EXPECTED_ISLAMIC_YEAR, EXPECTED_ISLAMIC_MONTH, EXPECTED_ISLAMIC_DAY + 1, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(expectedNextDay, day.addToCopy(1));
    }

    public void testDayOfWeekAndYearProperties() {
        // Test Day of Week
        assertEquals(DateTimeConstants.MONDAY, islamicDate.getDayOfWeek());
        Property dayOfWeek = islamicDate.dayOfWeek();
        assertEquals(1, dayOfWeek.getMinimumValue());
        assertEquals(7, dayOfWeek.getMaximumValue());

        // Test Day of Year
        // In a common year, the first 11 months have 6 long (30-day) and 5 short (29-day) months.
        // Day of year = (6 * 30) + (5 * 29) + 6 = 180 + 145 + 6 = 331.
        int expectedDayOfYear = (6 * 30) + (5 * 29) + 6;
        assertEquals(expectedDayOfYear, islamicDate.getDayOfYear());

        Property dayOfYear = islamicDate.dayOfYear();
        assertEquals(1, dayOfYear.getMinimumValue());
        assertEquals(354, dayOfYear.getMaximumValue()); // A common Islamic year has 354 days.
        assertEquals(355, dayOfYear.getMaximumValueOverall()); // A leap year has 355 days.
    }

    public void testTimeFields() {
        assertEquals(0, islamicDate.getHourOfDay());
        assertEquals(0, islamicDate.getMinuteOfHour());
        assertEquals(0, islamicDate.getSecondOfMinute());
        assertEquals(0, islamicDate.getMillisOfSecond());
    }
}