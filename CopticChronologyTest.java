/*
 *  Copyright 2001-2014 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * This class is a JUnit unit test for CopticChronology.
 * It tests various aspects of the CopticChronology class.
 * 
 * Author: Stephen Colebourne
 */
public class TestCopticChronology extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long DAYS_IN_YEAR = 365L;
    private static final long DAYS_IN_LEAP_YEAR = 366L;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    private static final long SKIP = 1 * MILLIS_PER_DAY;

    // Pre-calculated number of days from 1970 to 2002
    private long daysFrom1970To2002 = calculateDaysFrom1970To2002();

    // Test time set to 2002-06-09
    private long TEST_TIME_NOW = (daysFrom1970To2002 + 31 + 28 + 31 + 30 + 31 + 9 - 1) * MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCopticChronology.class);
    }

    public TestCopticChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    private long calculateDaysFrom1970To2002() {
        return 31 * DAYS_IN_YEAR + 8 * DAYS_IN_LEAP_YEAR;
    }

    // Test cases for factory methods
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, CopticChronology.getInstanceUTC().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, CopticChronology.getInstance().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, CopticChronology.getInstance(null).getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    // Test cases for equality and zone conversion
    public void testEquality() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(PARIS));
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
        assertSame(CopticChronology.getInstance(), CopticChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(LONDON).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(TOKYO).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC().withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(null));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance().withZone(PARIS));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("CopticChronology[Asia/Tokyo]", CopticChronology.getInstance(TOKYO).toString());
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance().toString());
        assertEquals("CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    // Test cases for duration fields
    public void testDurationFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertDurationField(coptic.eras(), "eras", false, false);
        assertDurationField(coptic.centuries(), "centuries", true, false);
        assertDurationField(coptic.years(), "years", true, false);
        assertDurationField(coptic.weekyears(), "weekyears", true, false);
        assertDurationField(coptic.months(), "months", true, false);
        assertDurationField(coptic.weeks(), "weeks", true, false);
        assertDurationField(coptic.days(), "days", true, false);
        assertDurationField(coptic.halfdays(), "halfdays", true, false);
        assertDurationField(coptic.hours(), "hours", true, true);
        assertDurationField(coptic.minutes(), "minutes", true, true);
        assertDurationField(coptic.seconds(), "seconds", true, true);
        assertDurationField(coptic.millis(), "millis", true, true);

        final CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        assertDurationField(copticUTC.centuries(), "centuries", true, false);
        assertDurationField(copticUTC.years(), "years", true, false);
        assertDurationField(copticUTC.weekyears(), "weekyears", true, false);
        assertDurationField(copticUTC.months(), "months", true, false);
        assertDurationField(copticUTC.weeks(), "weeks", true, true);
        assertDurationField(copticUTC.days(), "days", true, true);
        assertDurationField(copticUTC.halfdays(), "halfdays", true, true);
        assertDurationField(copticUTC.hours(), "hours", true, true);
        assertDurationField(copticUTC.minutes(), "minutes", true, true);
        assertDurationField(copticUTC.seconds(), "seconds", true, true);
        assertDurationField(copticUTC.millis(), "millis", true, true);

        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final CopticChronology copticGMT = CopticChronology.getInstance(gmt);
        assertDurationField(copticGMT.centuries(), "centuries", true, false);
        assertDurationField(copticGMT.years(), "years", true, false);
        assertDurationField(copticGMT.weekyears(), "weekyears", true, false);
        assertDurationField(copticGMT.months(), "months", true, false);
        assertDurationField(copticGMT.weeks(), "weeks", true, true);
        assertDurationField(copticGMT.days(), "days", true, true);
        assertDurationField(copticGMT.halfdays(), "halfdays", true, true);
        assertDurationField(copticGMT.hours(), "hours", true, true);
        assertDurationField(copticGMT.minutes(), "minutes", true, true);
        assertDurationField(copticGMT.seconds(), "seconds", true, true);
        assertDurationField(copticGMT.millis(), "millis", true, true);
    }

    private void assertDurationField(DurationField field, String name, boolean isSupported, boolean isPrecise) {
        assertEquals(name, field.getName());
        assertEquals(isSupported, field.isSupported());
        assertEquals(isPrecise, field.isPrecise());
    }

    // Test cases for date fields
    public void testDateFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertDateField(coptic.era(), "era", true, null);
        assertDateField(coptic.centuryOfEra(), "centuryOfEra", true, coptic.eras());
        assertDateField(coptic.yearOfCentury(), "yearOfCentury", true, coptic.centuries());
        assertDateField(coptic.yearOfEra(), "yearOfEra", true, coptic.eras());
        assertDateField(coptic.year(), "year", true, null);
        assertDateField(coptic.monthOfYear(), "monthOfYear", true, coptic.years());
        assertDateField(coptic.weekyearOfCentury(), "weekyearOfCentury", true, coptic.centuries());
        assertDateField(coptic.weekyear(), "weekyear", true, null);
        assertDateField(coptic.weekOfWeekyear(), "weekOfWeekyear", true, coptic.weekyears());
        assertDateField(coptic.dayOfYear(), "dayOfYear", true, coptic.years());
        assertDateField(coptic.dayOfMonth(), "dayOfMonth", true, coptic.months());
        assertDateField(coptic.dayOfWeek(), "dayOfWeek", true, coptic.weeks());
    }

    private void assertDateField(DateTimeField field, String name, boolean isSupported, DurationField rangeDuration) {
        assertEquals(name, field.getName());
        assertEquals(isSupported, field.isSupported());
        assertEquals(rangeDuration, field.getRangeDurationField());
    }

    // Test cases for time fields
    public void testTimeFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertTimeField(coptic.halfdayOfDay(), "halfdayOfDay", true);
        assertTimeField(coptic.clockhourOfHalfday(), "clockhourOfHalfday", true);
        assertTimeField(coptic.hourOfHalfday(), "hourOfHalfday", true);
        assertTimeField(coptic.clockhourOfDay(), "clockhourOfDay", true);
        assertTimeField(coptic.hourOfDay(), "hourOfDay", true);
        assertTimeField(coptic.minuteOfDay(), "minuteOfDay", true);
        assertTimeField(coptic.minuteOfHour(), "minuteOfHour", true);
        assertTimeField(coptic.secondOfDay(), "secondOfDay", true);
        assertTimeField(coptic.secondOfMinute(), "secondOfMinute", true);
        assertTimeField(coptic.millisOfDay(), "millisOfDay", true);
        assertTimeField(coptic.millisOfSecond(), "millisOfSecond", true);
    }

    private void assertTimeField(DateTimeField field, String name, boolean isSupported) {
        assertEquals(name, field.getName());
        assertEquals(isSupported, field.isSupported());
    }

    // Test cases for specific dates and durations
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals(new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

    public void testEra() {
        assertEquals(1, CopticChronology.AM);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, COPTIC_UTC);
            fail("Expected IllegalArgumentException for invalid date");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    public void testSampleDate() {
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        assertSampleDate(dt);
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(COPTIC_UTC);
        assertSampleDate(dt);
        assertEquals(10, dt.getHourOfDay());  // PARIS is UTC+2 in summer (12-2=10)
    }

    private void assertSampleDate(DateTime dt) {
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(1720, dt.getYear());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testDurationYear() {
        // Leap year 1723
        DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt21 = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt22 = new DateTime(1722, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt23 = new DateTime(1723, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt24 = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);

        DurationField yearField = dt20.year().getDurationField();
        assertEquals(COPTIC_UTC.years(), yearField);
        assertYearDuration(yearField, dt20, dt21, dt22, dt23, dt24);
    }

    private void assertYearDuration(DurationField yearField, DateTime dt20, DateTime dt21, DateTime dt22, DateTime dt23, DateTime dt24) {
        assertEquals(1L * DAYS_IN_YEAR * MILLIS_PER_DAY, yearField.getMillis(1, dt20.getMillis()));
        assertEquals(2L * DAYS_IN_YEAR * MILLIS_PER_DAY, yearField.getMillis(2, dt20.getMillis()));
        assertEquals(3L * DAYS_IN_YEAR * MILLIS_PER_DAY, yearField.getMillis(3, dt20.getMillis()));
        assertEquals((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY, yearField.getMillis(4, dt20.getMillis()));

        assertEquals(((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY) / 4, yearField.getMillis(1));
        assertEquals(((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY) / 2, yearField.getMillis(2));

        assertEquals(0, yearField.getValue(1L * DAYS_IN_YEAR * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(1, yearField.getValue(1L * DAYS_IN_YEAR * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(1, yearField.getValue(1L * DAYS_IN_YEAR * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(1, yearField.getValue(2L * DAYS_IN_YEAR * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(2, yearField.getValue(2L * DAYS_IN_YEAR * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(2, yearField.getValue(2L * DAYS_IN_YEAR * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(2, yearField.getValue(3L * DAYS_IN_YEAR * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(3, yearField.getValue(3L * DAYS_IN_YEAR * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(3, yearField.getValue(3L * DAYS_IN_YEAR * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(3, yearField.getValue((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(4, yearField.getValue((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(4, yearField.getValue((4L * DAYS_IN_YEAR + 1L) * MILLIS_PER_DAY + 1L, dt20.getMillis()));

        assertEquals(dt21.getMillis(), yearField.add(dt20.getMillis(), 1));
        assertEquals(dt22.getMillis(), yearField.add(dt20.getMillis(), 2));
        assertEquals(dt23.getMillis(), yearField.add(dt20.getMillis(), 3));
        assertEquals(dt24.getMillis(), yearField.add(dt20.getMillis(), 4));
    }

    public void testDurationMonth() {
        // Leap year 1723
        DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt12 = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt13 = new DateTime(1723, 13, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt01 = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);

        DurationField monthField = dt11.monthOfYear().getDurationField();
        assertEquals(COPTIC_UTC.months(), monthField);
        assertMonthDuration(monthField, dt11, dt12, dt13, dt01);
    }

    private void assertMonthDuration(DurationField monthField, DateTime dt11, DateTime dt12, DateTime dt13, DateTime dt01) {
        assertEquals(1L * 30L * MILLIS_PER_DAY, monthField.getMillis(1, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, monthField.getMillis(2, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, monthField.getMillis(3, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, monthField.getMillis(4, dt11.getMillis()));

        assertEquals(0, monthField.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(1, monthField.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(1, monthField.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(1, monthField.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(2, monthField.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, monthField.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(2, monthField.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(3, monthField.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, monthField.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(3, monthField.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(4, monthField.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(4, monthField.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));

        assertEquals(dt12.getMillis(), monthField.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), monthField.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), monthField.add(dt11.getMillis(), 3));
    }

    public void testLeap_5_13() {
        Chronology chrono = CopticChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    public void testLeap_6_13() {
        Chronology chrono = CopticChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}