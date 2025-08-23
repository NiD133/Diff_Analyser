/*
 * Copyright 2001-2014 Stephen Colebourne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for GregorianChronology with improved readability.
 */
@SuppressWarnings("deprecation")
public class TestGregorianChronology {

    // Common zones used across tests
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Freeze "now" at 2002-06-09T00:00:00.000Z to make tests deterministic
    private static final long FIXED_NOW_UTC =
            new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    // Original JVM-wide settings to be restored after each test
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(FIXED_NOW_UTC);

        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Use London as the default time zone and UK locale unless explicitly overridden
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);

        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    // ---------------------------------------------------------------------
    // Factory methods
    // ---------------------------------------------------------------------

    @Test
    public void factoryUTC_returnsUTCInstance() {
        assertEquals(DateTimeZone.UTC, GregorianChronology.getInstanceUTC().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstanceUTC().getClass());
    }

    @Test
    public void factory_defaultZone_isLondon() {
        assertEquals(LONDON, GregorianChronology.getInstance().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance().getClass());
    }

    @Test
    public void factory_withZone_returnsCorrectZone() {
        assertEquals(TOKYO, GregorianChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, GregorianChronology.getInstance(PARIS).getZone());
        // null means "use default"
        assertEquals(LONDON, GregorianChronology.getInstance(null).getZone());

        assertSame(GregorianChronology.class, GregorianChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void factory_withZone_andMinDaysInFirstWeek() {
        GregorianChronology chrono = GregorianChronology.getInstance(TOKYO, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());

        assertThrows(IllegalArgumentException.class, () -> GregorianChronology.getInstance(TOKYO, 0));
        assertThrows(IllegalArgumentException.class, () -> GregorianChronology.getInstance(TOKYO, 8));
    }

    // ---------------------------------------------------------------------
    // Identity and zone adjustments
    // ---------------------------------------------------------------------

    @Test
    public void singletonPerZone_andDefault() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(PARIS));
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
        assertSame(GregorianChronology.getInstance(), GregorianChronology.getInstance(LONDON));
    }

    @Test
    public void withUTC_returnsUTCInstanceRegardlessOfSourceZone() {
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(LONDON).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(TOKYO).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC().withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance().withUTC());
    }

    @Test
    public void withZone_changesOrKeepsZoneAsExpected() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(TOKYO).withZone(PARIS));

        // null -> default zone
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(null));

        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance().withZone(PARIS));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void toString_includesZoneAndMinDaysInFirstWeekWhenNonDefault() {
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance(LONDON).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", GregorianChronology.getInstance(TOKYO).toString());
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]",
                GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    // ---------------------------------------------------------------------
    // Duration fields
    // ---------------------------------------------------------------------

    @Test
    public void durationFields_names_support_and_precision() {
        final GregorianChronology greg = GregorianChronology.getInstance();

        // Names
        assertEquals("eras",       greg.eras().getName());
        assertEquals("centuries",  greg.centuries().getName());
        assertEquals("years",      greg.years().getName());
        assertEquals("weekyears",  greg.weekyears().getName());
        assertEquals("months",     greg.months().getName());
        assertEquals("weeks",      greg.weeks().getName());
        assertEquals("days",       greg.days().getName());
        assertEquals("halfdays",   greg.halfdays().getName());
        assertEquals("hours",      greg.hours().getName());
        assertEquals("minutes",    greg.minutes().getName());
        assertEquals("seconds",    greg.seconds().getName());
        assertEquals("millis",     greg.millis().getName());

        // Support
        assertFalse(greg.eras().isSupported()); // not a duration in this chronology
        assertTrue(greg.centuries().isSupported());
        assertTrue(greg.years().isSupported());
        assertTrue(greg.weekyears().isSupported());
        assertTrue(greg.months().isSupported());
        assertTrue(greg.weeks().isSupported());
        assertTrue(greg.days().isSupported());
        assertTrue(greg.halfdays().isSupported());
        assertTrue(greg.hours().isSupported());
        assertTrue(greg.minutes().isSupported());
        assertTrue(greg.seconds().isSupported());
        assertTrue(greg.millis().isSupported());

        // Precision depends on time zone for fields affected by DST cutovers
        assertFalse(greg.centuries().isPrecise());
        assertFalse(greg.years().isPrecise());
        assertFalse(greg.weekyears().isPrecise());
        assertFalse(greg.months().isPrecise());
        assertFalse(greg.weeks().isPrecise());
        assertFalse(greg.days().isPrecise());
        assertFalse(greg.halfdays().isPrecise());
        assertTrue(greg.hours().isPrecise());
        assertTrue(greg.minutes().isPrecise());
        assertTrue(greg.seconds().isPrecise());
        assertTrue(greg.millis().isPrecise());

        // In UTC (no DST), day-based fields become precise
        final GregorianChronology gregUTC = GregorianChronology.getInstanceUTC();
        assertFalse(gregUTC.centuries().isPrecise());
        assertFalse(gregUTC.years().isPrecise());
        assertFalse(gregUTC.weekyears().isPrecise());
        assertFalse(gregUTC.months().isPrecise());
        assertTrue(gregUTC.weeks().isPrecise());
        assertTrue(gregUTC.days().isPrecise());
        assertTrue(gregUTC.halfdays().isPrecise());
        assertTrue(gregUTC.hours().isPrecise());
        assertTrue(gregUTC.minutes().isPrecise());
        assertTrue(gregUTC.seconds().isPrecise());
        assertTrue(gregUTC.millis().isPrecise());

        // A fixed-offset zone like Etc/GMT behaves like UTC for precision
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregGMT = GregorianChronology.getInstance(gmt);
        assertFalse(gregGMT.centuries().isPrecise());
        assertFalse(gregGMT.years().isPrecise());
        assertFalse(gregGMT.weekyears().isPrecise());
        assertFalse(gregGMT.months().isPrecise());
        assertTrue(gregGMT.weeks().isPrecise());
        assertTrue(gregGMT.days().isPrecise());
        assertTrue(gregGMT.halfdays().isPrecise());
        assertTrue(gregGMT.hours().isPrecise());
        assertTrue(gregGMT.minutes().isPrecise());
        assertTrue(gregGMT.seconds().isPrecise());
        assertTrue(gregGMT.millis().isPrecise());
    }

    // ---------------------------------------------------------------------
    // Date fields
    // ---------------------------------------------------------------------

    @Test
    public void dateFields_names_support_and_durations() {
        final GregorianChronology greg = GregorianChronology.getInstance();

        // Names
        assertEquals("era",               greg.era().getName());
        assertEquals("centuryOfEra",      greg.centuryOfEra().getName());
        assertEquals("yearOfCentury",     greg.yearOfCentury().getName());
        assertEquals("yearOfEra",         greg.yearOfEra().getName());
        assertEquals("year",              greg.year().getName());
        assertEquals("monthOfYear",       greg.monthOfYear().getName());
        assertEquals("weekyearOfCentury", greg.weekyearOfCentury().getName());
        assertEquals("weekyear",          greg.weekyear().getName());
        assertEquals("weekOfWeekyear",    greg.weekOfWeekyear().getName());
        assertEquals("dayOfYear",         greg.dayOfYear().getName());
        assertEquals("dayOfMonth",        greg.dayOfMonth().getName());
        assertEquals("dayOfWeek",         greg.dayOfWeek().getName());

        // Support
        assertTrue(greg.era().isSupported());
        assertTrue(greg.centuryOfEra().isSupported());
        assertTrue(greg.yearOfCentury().isSupported());
        assertTrue(greg.yearOfEra().isSupported());
        assertTrue(greg.year().isSupported());
        assertTrue(greg.monthOfYear().isSupported());
        assertTrue(greg.weekyearOfCentury().isSupported());
        assertTrue(greg.weekyear().isSupported());
        assertTrue(greg.weekOfWeekyear().isSupported());
        assertTrue(greg.dayOfYear().isSupported());
        assertTrue(greg.dayOfMonth().isSupported());
        assertTrue(greg.dayOfWeek().isSupported());

        // Duration fields
        assertEquals(greg.eras(),      greg.era().getDurationField());
        assertEquals(greg.centuries(), greg.centuryOfEra().getDurationField());
        assertEquals(greg.years(),     greg.yearOfCentury().getDurationField());
        assertEquals(greg.years(),     greg.yearOfEra().getDurationField());
        assertEquals(greg.years(),     greg.year().getDurationField());
        assertEquals(greg.months(),    greg.monthOfYear().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyearOfCentury().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyear().getDurationField());
        assertEquals(greg.weeks(),     greg.weekOfWeekyear().getDurationField());
        assertEquals(greg.days(),      greg.dayOfYear().getDurationField());
        assertEquals(greg.days(),      greg.dayOfMonth().getDurationField());
        assertEquals(greg.days(),      greg.dayOfWeek().getDurationField());

        // Range duration fields
        assertEquals(null,              greg.era().getRangeDurationField());
        assertEquals(greg.eras(),       greg.centuryOfEra().getRangeDurationField());
        assertEquals(greg.centuries(),  greg.yearOfCentury().getRangeDurationField());
        assertEquals(greg.eras(),       greg.yearOfEra().getRangeDurationField());
        assertEquals(null,              greg.year().getRangeDurationField());
        assertEquals(greg.years(),      greg.monthOfYear().getRangeDurationField());
        assertEquals(greg.centuries(),  greg.weekyearOfCentury().getRangeDurationField());
        assertEquals(null,              greg.weekyear().getRangeDurationField());
        assertEquals(greg.weekyears(),  greg.weekOfWeekyear().getRangeDurationField());
        assertEquals(greg.years(),      greg.dayOfYear().getRangeDurationField());
        assertEquals(greg.months(),     greg.dayOfMonth().getRangeDurationField());
        assertEquals(greg.weeks(),      greg.dayOfWeek().getRangeDurationField());
    }

    // ---------------------------------------------------------------------
    // Time fields
    // ---------------------------------------------------------------------

    @Test
    public void timeFields_names_and_support() {
        final GregorianChronology greg = GregorianChronology.getInstance();

        // Names
        assertEquals("halfdayOfDay",     greg.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", greg.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday",    greg.hourOfHalfday().getName());
        assertEquals("clockhourOfDay",   greg.clockhourOfDay().getName());
        assertEquals("hourOfDay",        greg.hourOfDay().getName());
        assertEquals("minuteOfDay",      greg.minuteOfDay().getName());
        assertEquals("minuteOfHour",     greg.minuteOfHour().getName());
        assertEquals("secondOfDay",      greg.secondOfDay().getName());
        assertEquals("secondOfMinute",   greg.secondOfMinute().getName());
        assertEquals("millisOfDay",      greg.millisOfDay().getName());
        assertEquals("millisOfSecond",   greg.millisOfSecond().getName());

        // Support
        assertTrue(greg.halfdayOfDay().isSupported());
        assertTrue(greg.clockhourOfHalfday().isSupported());
        assertTrue(greg.hourOfHalfday().isSupported());
        assertTrue(greg.clockhourOfDay().isSupported());
        assertTrue(greg.hourOfDay().isSupported());
        assertTrue(greg.minuteOfDay().isSupported());
        assertTrue(greg.minuteOfHour().isSupported());
        assertTrue(greg.secondOfDay().isSupported());
        assertTrue(greg.secondOfMinute().isSupported());
        assertTrue(greg.millisOfDay().isSupported());
        assertTrue(greg.millisOfSecond().isSupported());
    }

    // ---------------------------------------------------------------------
    // Specific behavior
    // ---------------------------------------------------------------------

    @Test
    public void maximumDayOfMonth_inFebruary1999_is28() {
        YearMonthDay feb1999 = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1);
        DateMidnight feb1999Midnight = new DateMidnight(1999, DateTimeConstants.FEBRUARY, 1);

        Chronology chrono = GregorianChronology.getInstance();
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(feb1999));
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(feb1999Midnight.getMillis()));
    }

    @Test
    public void leapFlags_onFeb28_inLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    @Test
    public void leapFlags_onFeb29_inLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}