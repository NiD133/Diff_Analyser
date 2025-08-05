/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class TestCalendarConverter {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");
    private static final long GREGORIAN_CUTOVER_MILLIS = 0L;
    private static final int MIN_DAYS_IN_FIRST_WEEK = 4;

    private Chronology julianChronology;
    private Chronology isoChronology;

    @Before
    public void setUp() {
        julianChronology = JulianChronology.getInstance();
        isoChronology = ISOChronology.getInstance();
    }

    //-----------------------------------------------------------------------
    // Singleton and Class Structure
    //-----------------------------------------------------------------------
    @Test
    public void singleton_isPackagePrivateAndExposedViaInstance() throws Exception {
        Class<?> cls = CalendarConverter.class;
        assertFalse("Class should be package-private", Modifier.isPublic(cls.getModifiers()));
        assertFalse(Modifier.isProtected(cls.getModifiers()));
        assertFalse(Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructor((Class[]) null);
        assertEquals("Should have one constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(constructor.getModifiers()));

        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should be package-private", Modifier.isPublic(instanceField.getModifiers()));
        assertFalse(Modifier.isProtected(instanceField.getModifiers()));
        assertFalse(Modifier.isPrivate(instanceField.getModifiers()));
    }

    @Test
    public void getSupportedType_returnsCalendarClass() {
        assertEquals(Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    //-----------------------------------------------------------------------
    // getInstantMillis
    //-----------------------------------------------------------------------
    @Test
    public void getInstantMillis_returnsMillisFromCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(123L));

        long expectedMillis = 123L;
        long actualMillis = CalendarConverter.INSTANCE.getInstantMillis(calendar, julianChronology);
        assertEquals(expectedMillis, actualMillis);

        // Ensure the original calendar object is not modified
        assertEquals(123L, calendar.getTime().getTime());
    }

    //-----------------------------------------------------------------------
    // getChronology(Object, DateTimeZone)
    //-----------------------------------------------------------------------
    @Test
    public void getChronology_withZone_forGregorianCalendar_returnsGJChronologyWithSpecifiedZone() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology expected = GJChronology.getInstance(MOSCOW);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW));
    }

    @Test
    public void getChronology_withNullZone_forGregorianCalendar_returnsGJChronologyWithCalendarZone() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // When zone is null, the calendar's zone should be used.
        Chronology expected = GJChronology.getInstance(); // Default zone is used if calendar's zone is null
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, (DateTimeZone) null));
    }

    @Test
    public void getChronology_withZone_forGregorianCalendarWithCutover_returnsGJChronologyWithCutover() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(GREGORIAN_CUTOVER_MILLIS));
        Chronology expected = GJChronology.getInstance(MOSCOW, GREGORIAN_CUTOVER_MILLIS, MIN_DAYS_IN_FIRST_WEEK);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW));
    }

    @Test
    public void getChronology_withZone_forGregorianCalendarWithMaxCutover_returnsJulianChronology() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // A cutover at MAX_VALUE means the calendar is effectively always Julian
        calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        Chronology expected = JulianChronology.getInstance(PARIS);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, PARIS));
    }

    @Test
    public void getChronology_withZone_forGregorianCalendarWithMinCutover_returnsGregorianChronology() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // A cutover at MIN_VALUE means the calendar is effectively always Gregorian
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        Chronology expected = GregorianChronology.getInstance(PARIS);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, PARIS));
    }

    @Test
    public void getChronology_withZone_forUnknownCalendar_returnsISOChronology() {
        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology expected = ISOChronology.getInstance(PARIS);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(unknownCalendar, PARIS));
    }

    @Test
    public void getChronology_withZone_forBuddhistCalendar_returnsBuddhistChronology() {
        // This test is specific to Sun/Oracle JDKs. Skip if the class is not found.
        Calendar buddhistCalendar = createBuddhistCalendar();
        assumeTrue("BuddhistCalendar class not found, skipping test", buddhistCalendar != null);

        buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology expected = BuddhistChronology.getInstance(PARIS);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(buddhistCalendar, PARIS));
    }

    //-----------------------------------------------------------------------
    // getChronology(Object, Chronology)
    //-----------------------------------------------------------------------
    @Test
    public void getChronology_withNonNullChronology_returnsPassedInChronology() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(julianChronology, CalendarConverter.INSTANCE.getChronology(calendar, julianChronology));
    }

    @Test
    public void getChronology_withNullChronology_forGregorianCalendar_returnsGJChronologyWithCalendarZone() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology expected = GJChronology.getInstance(PARIS);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));
    }

    @Test
    public void getChronology_withNullChronology_forGregorianCalendarWithCutover_returnsGJChronologyWithCutover() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(GREGORIAN_CUTOVER_MILLIS));
        Chronology expected = GJChronology.getInstance(MOSCOW, GREGORIAN_CUTOVER_MILLIS, MIN_DAYS_IN_FIRST_WEEK);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));
    }

    @Test
    public void getChronology_withNullChronology_forGregorianCalendarWithMaxCutover_returnsJulianChronology() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        Chronology expected = JulianChronology.getInstance(MOSCOW);
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));
    }
    
    @Test
    public void getChronology_withNullChronology_forGregorianCalendarWithUnknownZone_returnsGJChronologyWithDefaultZone() {
        GregorianCalendar calendar = new GregorianCalendar(new MockUnknownTimeZone());
        // Should fall back to the default time zone
        Chronology expected = GJChronology.getInstance();
        assertEquals(expected, CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));
    }

    //-----------------------------------------------------------------------
    // getPartialValues
    //-----------------------------------------------------------------------
    @Test
    public void getPartialValues_extractsValuesFromCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();
        long testMillis = 12345678L;
        calendar.setTime(new Date(testMillis));

        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expected = isoChronology.get(timeOfDay, testMillis);
        int[] actual = CalendarConverter.INSTANCE.getPartialValues(timeOfDay, calendar, isoChronology);

        assertArrayEquals(expected, actual);
    }

    //-----------------------------------------------------------------------
    // toString
    //-----------------------------------------------------------------------
    @Test
    public void toString_returnsConverterName() {
        assertEquals("Converter[java.util.Calendar]", CalendarConverter.INSTANCE.toString());
    }

    //-----------------------------------------------------------------------
    // Helper methods
    //-----------------------------------------------------------------------
    private Calendar createBuddhistCalendar() {
        try {
            return (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // Class not available on this JDK or module path issues
            return null;
        }
    }
}