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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Tests for CalendarConverter.
 * 
 * The goal of these tests is to clearly describe behavior with:
 * - visibility and singleton construction
 * - supported type reporting
 * - instant millis extraction
 * - chronology selection across zones and calendar variants
 * - partial value extraction
 * - string representation
 */
public class TestCalendarConverter extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");

    private static final long SAMPLE_MILLIS = 123L;
    private static final long SAMPLE_MILLIS_FOR_PARTIAL = 12_345_678L;

    private static Chronology JULIAN;
    private static Chronology ISO;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCalendarConverter.class);
    }

    public TestCalendarConverter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        JULIAN = JulianChronology.getInstance();
        ISO = ISOChronology.getInstance();
    }

    // ---------------------------------------------------------------------
    // Singleton and visibility
    // ---------------------------------------------------------------------

    public void testSingleton_visibilityAndConstructor() throws Exception {
        Class<?> cls = CalendarConverter.class;

        // Class visibility should be package-private
        assertFalse("Class should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(cls.getModifiers()));

        // Exactly one constructor and it must be protected
        Constructor<?> con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals("Expected exactly one declared constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(con.getModifiers()));

        // INSTANCE field should be package-private (no explicit modifier)
        Field fld = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE should not be public", Modifier.isPublic(fld.getModifiers()));
        assertFalse("INSTANCE should not be protected", Modifier.isProtected(fld.getModifiers()));
        assertFalse("INSTANCE should not be private", Modifier.isPrivate(fld.getModifiers()));
    }

    // ---------------------------------------------------------------------
    // Supported type
    // ---------------------------------------------------------------------

    public void testSupportedType_isCalendar() {
        assertEquals(Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    // ---------------------------------------------------------------------
    // Instant millis extraction
    // ---------------------------------------------------------------------

    public void testGetInstantMillis_returnsCalendarTimeAndDoesNotMutate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(SAMPLE_MILLIS));

        assertEquals("Instant millis should match Calendar time",
                SAMPLE_MILLIS, CalendarConverter.INSTANCE.getInstantMillis(calendar, JULIAN));
        assertEquals("Calendar time should be unchanged after conversion",
                SAMPLE_MILLIS, calendar.getTime().getTime());
    }

    // ---------------------------------------------------------------------
    // Chronology selection with DateTimeZone preference
    // ---------------------------------------------------------------------

    public void testGetChronology_withZone_prefersSpecifiedZoneAndCalendarType() throws Exception {
        // GJChronology when using GregorianCalendar with specified zone
        GregorianCalendar cal = newGregorianCalendar("Europe/Paris");
        assertChronologyFromZone(cal, MOSCOW, GJChronology.getInstance(MOSCOW));

        // When zone is null, use calendar's zone
        cal = newGregorianCalendar("Europe/Moscow");
        assertChronologyFromZone(cal, null, GJChronology.getInstance());

        // GJ with supplied cutover (changeover date at epoch)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(0L));
        assertChronologyFromZone(cal, MOSCOW, GJChronology.getInstance(MOSCOW, 0L, 4));

        // Pure Julian when changeover is effectively never (Long.MAX_VALUE)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        assertChronologyFromZone(cal, PARIS, JulianChronology.getInstance(PARIS));

        // Pure Gregorian when changeover is effectively from beginning (Long.MIN_VALUE)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        assertChronologyFromZone(cal, PARIS, GregorianChronology.getInstance(PARIS));

        // Unknown Calendar -> ISO with given zone
        Calendar unknownCal = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertChronologyFromZone(unknownCal, PARIS, ISOChronology.getInstance(PARIS));

        // BuddhistCalendar (if available) -> BuddhistChronology
        Calendar buddhist = tryCreateBuddhistCalendar();
        if (buddhist != null) {
            buddhist.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertChronologyFromZone(buddhist, PARIS, BuddhistChronology.getInstance(PARIS));
        }
    }

    // ---------------------------------------------------------------------
    // Chronology selection with provided Chronology taking precedence
    // ---------------------------------------------------------------------

    public void testGetChronology_withExplicitChronology_passesThrough() {
        GregorianCalendar cal = newGregorianCalendar("Europe/Paris");
        assertSame("Explicit chronology should be returned as-is",
                JULIAN, CalendarConverter.INSTANCE.getChronology(cal, JULIAN));
    }

    // ---------------------------------------------------------------------
    // Chronology selection when no Chronology is provided (null)
    // ---------------------------------------------------------------------

    public void testGetChronology_withNullChronology_usesCalendarAndZone() throws Exception {
        // GJChronology when using GregorianCalendar with its own zone
        GregorianCalendar cal = newGregorianCalendar("Europe/Paris");
        assertChronologyFromChronology(cal, null, GJChronology.getInstance(PARIS));

        // GJ with supplied cutover (changeover date at epoch)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(0L));
        assertChronologyFromChronology(cal, null, GJChronology.getInstance(MOSCOW, 0L, 4));

        // Pure Julian when changeover is effectively never (Long.MAX_VALUE)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        assertChronologyFromChronology(cal, null, JulianChronology.getInstance(MOSCOW));

        // Pure Gregorian when changeover is effectively from beginning (Long.MIN_VALUE)
        cal = newGregorianCalendar("Europe/Moscow");
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        assertChronologyFromChronology(cal, null, GregorianChronology.getInstance(MOSCOW));

        // Unknown TimeZone on GregorianCalendar -> default zone GJ (no zone override)
        cal = new GregorianCalendar(new MockUnknownTimeZone());
        assertChronologyFromChronology(cal, null, GJChronology.getInstance());

        // Unknown Calendar -> ISO with calendar zone
        Calendar unknownCal = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertChronologyFromChronology(unknownCal, null, ISOChronology.getInstance(MOSCOW));

        // BuddhistCalendar (if available) -> BuddhistChronology with calendar zone
        Calendar buddhist = tryCreateBuddhistCalendar();
        if (buddhist != null) {
            buddhist.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertChronologyFromChronology(buddhist, null, BuddhistChronology.getInstance(MOSCOW));
        }
    }

    // ---------------------------------------------------------------------
    // Partial values extraction
    // ---------------------------------------------------------------------

    public void testGetPartialValues_matchesISOFieldValues() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(SAMPLE_MILLIS_FOR_PARTIAL));

        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expected = ISO.get(timeOfDay, SAMPLE_MILLIS_FOR_PARTIAL);
        int[] actual = CalendarConverter.INSTANCE.getPartialValues(timeOfDay, cal, ISO);

        assertTrue("Partial field values must match ISO chronology extraction",
                Arrays.equals(expected, actual));
    }

    // ---------------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------------

    public void testToString() {
        assertEquals("Converter[java.util.Calendar]", CalendarConverter.INSTANCE.toString());
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static GregorianCalendar newGregorianCalendar(String tzId) {
        return new GregorianCalendar(TimeZone.getTimeZone(tzId));
    }

    private static void assertChronologyFromZone(Calendar calendar, DateTimeZone zone, Chronology expected) {
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendar, zone);
        assertEquals("Chronology selected (with zone preference) did not match", expected, actual);
    }

    private static void assertChronologyFromChronology(Calendar calendar, Chronology requested, Chronology expected) {
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendar, requested);
        assertEquals("Chronology selected (with null/explicit chronology) did not match", expected, actual);
    }

    /**
     * Attempts to create a JDK BuddhistCalendar if present and accessible.
     * Returns null otherwise, allowing tests to skip gracefully on non-Sun JDKs or module restrictions.
     */
    private static Calendar tryCreateBuddhistCalendar() {
        try {
            return (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
        } catch (ClassNotFoundException ex) {
            // Not available on this JDK
            return null;
        } catch (IllegalAccessException ex) {
            // Not accessible due to modules (JDK 9+)
            return null;
        } catch (InstantiationException ex) {
            return null;
        }
    }
}