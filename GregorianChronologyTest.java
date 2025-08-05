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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is a Junit unit test for GregorianChronology.
 *
 * @author Stephen Colebourne
 */
public class TestGregorianChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // 2002-06-09 in UTC
    private static final long TEST_TIME_NOW = 
        new DateTime(2002, 6, 9, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, GregorianChronology.getInstanceUTC().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstanceUTC().getClass());
    }

    @Test
    public void testFactory() {
        assertEquals(LONDON, GregorianChronology.getInstance().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance().getClass());
    }

    @Test
    public void testFactoryWithZone() {
        assertEquals(TOKYO, GregorianChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, GregorianChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, GregorianChronology.getInstance(null).getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void testFactoryWithZoneAndMinimumDays() {
        GregorianChronology chrono = GregorianChronology.getInstance(TOKYO, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        
        try {
            GregorianChronology.getInstance(TOKYO, 0);
            fail("Expected IllegalArgumentException for invalid minimum days (0)");
        } catch (IllegalArgumentException ex) {}
        
        try {
            GregorianChronology.getInstance(TOKYO, 8);
            fail("Expected IllegalArgumentException for invalid minimum days (8)");
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    @Test
    public void testEquality() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(PARIS));
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
        assertSame(GregorianChronology.getInstance(), GregorianChronology.getInstance(LONDON));
    }

    @Test
    public void testWithUTC() {
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(LONDON).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(TOKYO).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC().withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance().withUTC());
    }

    @Test
    public void testWithZone() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(null));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance().withZone(PARIS));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void testToString() {
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance(LONDON).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", GregorianChronology.getInstance(TOKYO).toString());
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]", GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testDurationFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("eras", greg.eras().getName());
        assertEquals("centuries", greg.centuries().getName());
        assertEquals("years", greg.years().getName());
        assertEquals("weekyears", greg.weekyears().getName());
        assertEquals("months", greg.months().getName());
        assertEquals("weeks", greg.weeks().getName());
        assertEquals("days", greg.days().getName());
        assertEquals("halfdays", greg.halfdays().getName());
        assertEquals("hours", greg.hours().getName());
        assertEquals("minutes", greg.minutes().getName());
        assertEquals("seconds", greg.seconds().getName());
        assertEquals("millis", greg.millis().getName());
        
        // Test field support
        assertEquals(false, greg.eras().isSupported());
        assertEquals(true, greg.centuries().isSupported());
        assertEquals(true, greg.years().isSupported());
        assertEquals(true, greg.weekyears().isSupported());
        assertEquals(true, greg.months().isSupported());
        assertEquals(true, greg.weeks().isSupported());
        assertEquals(true, greg.days().isSupported());
        assertEquals(true, greg.halfdays().isSupported());
        assertEquals(true, greg.hours().isSupported());
        assertEquals(true, greg.minutes().isSupported());
        assertEquals(true, greg.seconds().isSupported());
        assertEquals(true, greg.millis().isSupported());
        
        // Test precision in default zone (London)
        assertEquals(false, greg.centuries().isPrecise());
        assertEquals(false, greg.years().isPrecise());
        assertEquals(false, greg.weekyears().isPrecise());
        assertEquals(false, greg.months().isPrecise());
        assertEquals(false, greg.weeks().isPrecise());
        assertEquals(false, greg.days().isPrecise());
        assertEquals(false, greg.halfdays().isPrecise());
        assertEquals(true, greg.hours().isPrecise());
        assertEquals(true, greg.minutes().isPrecise());
        assertEquals(true, greg.seconds().isPrecise());
        assertEquals(true, greg.millis().isPrecise());
        
        // Test precision in UTC
        final GregorianChronology gregUTC = GregorianChronology.getInstanceUTC();
        assertEquals(false, gregUTC.centuries().isPrecise());
        assertEquals(false, gregUTC.years().isPrecise());
        assertEquals(false, gregUTC.weekyears().isPrecise());
        assertEquals(false, gregUTC.months().isPrecise());
        assertEquals(true, gregUTC.weeks().isPrecise());
        assertEquals(true, gregUTC.days().isPrecise());
        assertEquals(true, gregUTC.halfdays().isPrecise());
        assertEquals(true, gregUTC.hours().isPrecise());
        assertEquals(true, gregUTC.minutes().isPrecise());
        assertEquals(true, gregUTC.seconds().isPrecise());
        assertEquals(true, gregUTC.millis().isPrecise());
        
        // Test precision in GMT
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregGMT = GregorianChronology.getInstance(gmt);
        assertEquals(false, gregGMT.centuries().isPrecise());
        assertEquals(false, gregGMT.years().isPrecise());
        assertEquals(false, gregGMT.weekyears().isPrecise());
        assertEquals(false, gregGMT.months().isPrecise());
        assertEquals(true, gregGMT.weeks().isPrecise());
        assertEquals(true, gregGMT.days().isPrecise());
        assertEquals(true, gregGMT.halfdays().isPrecise());
        assertEquals(true, gregGMT.hours().isPrecise());
        assertEquals(true, gregGMT.minutes().isPrecise());
        assertEquals(true, gregGMT.seconds().isPrecise());
        assertEquals(true, gregGMT.millis().isPrecise());
    }

    @Test
    public void testDateFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("era", greg.era().getName());
        assertEquals("centuryOfEra", greg.centuryOfEra().getName());
        assertEquals("yearOfCentury", greg.yearOfCentury().getName());
        assertEquals("yearOfEra", greg.yearOfEra().getName());
        assertEquals("year", greg.year().getName());
        assertEquals("monthOfYear", greg.monthOfYear().getName());
        assertEquals("weekyearOfCentury", greg.weekyearOfCentury().getName());
        assertEquals("weekyear", greg.weekyear().getName());
        assertEquals("weekOfWeekyear", greg.weekOfWeekyear().getName());
        assertEquals("dayOfYear", greg.dayOfYear().getName());
        assertEquals("dayOfMonth", greg.dayOfMonth().getName());
        assertEquals("dayOfWeek", greg.dayOfWeek().getName());
        
        // Test field support
        assertEquals(true, greg.era().isSupported());
        assertEquals(true, greg.centuryOfEra().isSupported());
        assertEquals(true, greg.yearOfCentury().isSupported());
        assertEquals(true, greg.yearOfEra().isSupported());
        assertEquals(true, greg.year().isSupported());
        assertEquals(true, greg.monthOfYear().isSupported());
        assertEquals(true, greg.weekyearOfCentury().isSupported());
        assertEquals(true, greg.weekyear().isSupported());
        assertEquals(true, greg.weekOfWeekyear().isSupported());
        assertEquals(true, greg.dayOfYear().isSupported());
        assertEquals(true, greg.dayOfMonth().isSupported());
        assertEquals(true, greg.dayOfWeek().isSupported());
        
        // Test duration fields
        assertEquals(greg.eras(), greg.era().getDurationField());
        assertEquals(greg.centuries(), greg.centuryOfEra().getDurationField());
        assertEquals(greg.years(), greg.yearOfCentury().getDurationField());
        assertEquals(greg.years(), greg.yearOfEra().getDurationField());
        assertEquals(greg.years(), greg.year().getDurationField());
        assertEquals(greg.months(), greg.monthOfYear().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyearOfCentury().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyear().getDurationField());
        assertEquals(greg.weeks(), greg.weekOfWeekyear().getDurationField());
        assertEquals(greg.days(), greg.dayOfYear().getDurationField());
        assertEquals(greg.days(), greg.dayOfMonth().getDurationField());
        assertEquals(greg.days(), greg.dayOfWeek().getDurationField());
        
        // Test range duration fields
        assertEquals(null, greg.era().getRangeDurationField());
        assertEquals(greg.eras(), greg.centuryOfEra().getRangeDurationField());
        assertEquals(greg.centuries(), greg.yearOfCentury().getRangeDurationField());
        assertEquals(greg.eras(), greg.yearOfEra().getRangeDurationField());
        assertEquals(null, greg.year().getRangeDurationField());
        assertEquals(greg.years(), greg.monthOfYear().getRangeDurationField());
        assertEquals(greg.centuries(), greg.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, greg.weekyear().getRangeDurationField());
        assertEquals(greg.weekyears(), greg.weekOfWeekyear().getRangeDurationField());
        assertEquals(greg.years(), greg.dayOfYear().getRangeDurationField());
        assertEquals(greg.months(), greg.dayOfMonth().getRangeDurationField());
        assertEquals(greg.weeks(), greg.dayOfWeek().getRangeDurationField());
    }

    @Test
    public void testTimeFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("halfdayOfDay", greg.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", greg.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", greg.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", greg.clockhourOfDay().getName());
        assertEquals("hourOfDay", greg.hourOfDay().getName());
        assertEquals("minuteOfDay", greg.minuteOfDay().getName());
        assertEquals("minuteOfHour", greg.minuteOfHour().getName());
        assertEquals("secondOfDay", greg.secondOfDay().getName());
        assertEquals("secondOfMinute", greg.secondOfMinute().getName());
        assertEquals("millisOfDay", greg.millisOfDay().getName());
        assertEquals("millisOfSecond", greg.millisOfSecond().getName());
        
        // Test field support
        assertEquals(true, greg.halfdayOfDay().isSupported());
        assertEquals(true, greg.clockhourOfHalfday().isSupported());
        assertEquals(true, greg.hourOfHalfday().isSupported());
        assertEquals(true, greg.clockhourOfDay().isSupported());
        assertEquals(true, greg.hourOfDay().isSupported());
        assertEquals(true, greg.minuteOfDay().isSupported());
        assertEquals(true, greg.minuteOfHour().isSupported());
        assertEquals(true, greg.secondOfDay().isSupported());
        assertEquals(true, greg.secondOfMinute().isSupported());
        assertEquals(true, greg.millisOfDay().isSupported());
        assertEquals(true, greg.millisOfSecond().isSupported());
    }

    @Test
    public void testMaximumValueForFebruary() {
        Chronology chrono = GregorianChronology.getInstance();
        LocalDate febDate = new LocalDate(1999, DateTimeConstants.FEBRUARY, 1, chrono);
        long febInstant = febDate.toDateTimeAtStartOfDay(chrono.getZone()).getMillis();
        
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(febDate));
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(febInstant));
    }

    @Test
    public void testLeapProperties_28February() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);
        
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(false, dt.dayOfMonth().isLeap());
        assertEquals(false, dt.dayOfYear().isLeap());
    }

    @Test
    public void testLeapProperties_29February() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);
        
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(true, dt.dayOfMonth().isLeap());
        assertEquals(true, dt.dayOfYear().isLeap());
    }
}