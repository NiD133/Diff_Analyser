package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTime.Property;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests various properties of date fields in the IslamicChronology.
 * This test focuses on a specific leap year date to verify field behaviors.
 */
public class IslamicChronologyDateFieldsTest {

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();

    // Test date is 1426-12-24 in the Islamic calendar.
    // The year 1426 is a leap year in the default (16-based) pattern,
    // as 1426 % 30 = 16, and 16 is a leap year in that pattern.
    private static final int TEST_YEAR = 1426;
    private static final int TEST_MONTH = 12;
    private static final int TEST_DAY = 24;

    private DateTime testDate;

    @Before
    public void setUp() {
        testDate = new DateTime(TEST_YEAR, TEST_MONTH, TEST_DAY, 0, 0, 0, 0, ISLAMIC_UTC);
    }

    @Test
    public void testDateCreationAndBasicComponents() {
        assertEquals(IslamicChronology.AH, testDate.getEra());
        assertEquals(TEST_YEAR, testDate.getYear());
        assertEquals(TEST_MONTH, testDate.getMonthOfYear());
        assertEquals(TEST_DAY, testDate.getDayOfMonth());
        assertEquals(DateTimeConstants.TUESDAY, testDate.getDayOfWeek());
    }

    @Test
    public void testTimeComponents() {
        assertEquals(0, testDate.getHourOfDay());
        assertEquals(0, testDate.getMinuteOfHour());
        assertEquals(0, testDate.getSecondOfMinute());
        assertEquals(0, testDate.getMillisOfSecond());
    }

    @Test
    public void testYearField_inLeapYear() {
        Property yearProperty = testDate.year();
        assertTrue("Year 1426 should be a leap year", yearProperty.isLeap());
        assertEquals(1, yearProperty.getLeapAmount());
        assertEquals(DurationFieldType.days(), yearProperty.getLeapDurationField().getType());
    }

    @Test
    public void testMonthOfYearField_inLeapYear() {
        Property monthProperty = testDate.monthOfYear();
        // The last month of a leap year is also considered "leap" as it has an extra day.
        assertTrue("Month 12 in a leap year should be considered leap", monthProperty.isLeap());
        assertEquals(1, monthProperty.getLeapAmount());
        assertEquals(DurationFieldType.days(), monthProperty.getLeapDurationField().getType());
        assertEquals(1, monthProperty.getMinimumValue());
        assertEquals(12, monthProperty.getMaximumValue());
    }

    @Test
    public void testDayOfMonthField_inLeapYear() {
        Property dayOfMonthProperty = testDate.dayOfMonth();
        assertFalse(dayOfMonthProperty.isLeap());
        assertNull(dayOfMonthProperty.getLeapDurationField());
        assertEquals(1, dayOfMonthProperty.getMinimumValue());
        // In a leap year, the 12th month (Dhu al-Hijjah) has 30 days instead of 29.
        assertEquals(30, dayOfMonthProperty.getMaximumValue());
        assertEquals(30, dayOfMonthProperty.getMaximumValueOverall());
    }

    @Test
    public void testDayOfWeekField() {
        Property dayOfWeekProperty = testDate.dayOfWeek();
        assertFalse(dayOfWeekProperty.isLeap());
        assertNull(dayOfWeekProperty.getLeapDurationField());
        assertEquals(1, dayOfWeekProperty.getMinimumValue());
        assertEquals(7, dayOfWeekProperty.getMaximumValue());
    }

    @Test
    public void testDayOfYearField_inLeapYear() {
        // In the Islamic calendar, odd months have 30 days and even months have 29.
        // For the first 11 months, there are 6 odd (30 days) and 5 even (29 days).
        // Day of year = (6 * 30) + (5 * 29) + day_of_12th_month
        int expectedDayOfYear = (6 * 30) + (5 * 29) + TEST_DAY;
        assertEquals(expectedDayOfYear, testDate.getDayOfYear());

        Property dayOfYearProperty = testDate.dayOfYear();
        assertFalse(dayOfYearProperty.isLeap());
        // A leap year in the Islamic calendar has 355 days.
        assertEquals(355, dayOfYearProperty.getMaximumValue());
        assertEquals(355, dayOfYearProperty.getMaximumValueOverall());
    }
}