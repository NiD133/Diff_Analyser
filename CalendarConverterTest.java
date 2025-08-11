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
 * This class is a Junit unit test for CalendarConverter.
 *
 * @author Stephen Colebourne
 */
public class TestCalendarConverter extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");
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

    //-----------------------------------------------------------------------
    // Test singleton pattern
    //-----------------------------------------------------------------------
    public void testSingleton() throws Exception {
        Class cls = CalendarConverter.class;
        assertEquals(false, Modifier.isPublic(cls.getModifiers()));
        assertEquals(false, Modifier.isProtected(cls.getModifiers()));
        assertEquals(false, Modifier.isPrivate(cls.getModifiers()));
        
        Constructor con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertEquals(true, Modifier.isProtected(con.getModifiers()));
        
        Field fld = cls.getDeclaredField("INSTANCE");
        assertEquals(false, Modifier.isPublic(fld.getModifiers()));
        assertEquals(false, Modifier.isProtected(fld.getModifiers()));
        assertEquals(false, Modifier.isPrivate(fld.getModifiers()));
    }

    //-----------------------------------------------------------------------
    // Test supported type
    //-----------------------------------------------------------------------
    public void testSupportedType() throws Exception {
        assertEquals(Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    //-----------------------------------------------------------------------
    // Test getInstantMillis
    //-----------------------------------------------------------------------
    public void testGetInstantMillis() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(123L));
        assertEquals(123L, CalendarConverter.INSTANCE.getInstantMillis(cal, JULIAN));
        assertEquals(123L, cal.getTime().getTime());
    }

    //-----------------------------------------------------------------------
    // Test getChronology with DateTimeZone parameter
    //-----------------------------------------------------------------------
    public void testGetChronology_WithDateTimeZone_ForGregorianCalendarWithDifferentZone() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, MOSCOW);
        assertEquals(GJChronology.getInstance(MOSCOW), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForGregorianCalendarWithNullZone() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (DateTimeZone) null);
        assertEquals(GJChronology.getInstance(), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForGregorianCalendarWithGregorianChangeAtZero() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(0L));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, MOSCOW);
        assertEquals(GJChronology.getInstance(MOSCOW, 0L, 4), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForGregorianCalendarWithGregorianChangeAtMaxValue() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, PARIS);
        assertEquals(JulianChronology.getInstance(PARIS), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForGregorianCalendarWithGregorianChangeAtMinValue() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, PARIS);
        assertEquals(GregorianChronology.getInstance(PARIS), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForMockUnknownCalendar() {
        Calendar uc = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(uc, PARIS);
        assertEquals(ISOChronology.getInstance(PARIS), chrono);
    }
    
    public void testGetChronology_WithDateTimeZone_ForBuddhistCalendar() {
        Calendar bc = createBuddhistCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        if (bc == null) return; // Skip if not available
        
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(bc, PARIS);
        assertEquals(BuddhistChronology.getInstance(PARIS), chrono);
    }

    //-----------------------------------------------------------------------
    // Test getChronology with null Chronology parameter
    //-----------------------------------------------------------------------
    public void testGetChronology_WithNullChronology_ForGregorianCalendarWithParisTimeZone() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null);
        assertEquals(GJChronology.getInstance(PARIS), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForGregorianCalendarWithGregorianChangeAtZero() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(0L));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null);
        assertEquals(GJChronology.getInstance(MOSCOW, 0L, 4), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForGregorianCalendarWithGregorianChangeAtMaxValue() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null);
        assertEquals(JulianChronology.getInstance(MOSCOW), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForGregorianCalendarWithGregorianChangeAtMinValue() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null);
        assertEquals(GregorianChronology.getInstance(MOSCOW), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForGregorianCalendarWithMockTimeZone() {
        GregorianCalendar cal = new GregorianCalendar(new MockUnknownTimeZone());
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null);
        assertEquals(GJChronology.getInstance(), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForMockUnknownCalendar() {
        Calendar uc = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(uc, (Chronology) null);
        assertEquals(ISOChronology.getInstance(MOSCOW), chrono);
    }
    
    public void testGetChronology_WithNullChronology_ForBuddhistCalendar() {
        Calendar bc = createBuddhistCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        if (bc == null) return; // Skip if not available
        
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(bc, (Chronology) null);
        assertEquals(BuddhistChronology.getInstance(MOSCOW), chrono);
    }

    //-----------------------------------------------------------------------
    // Test getChronology with specific Chronology parameter
    //-----------------------------------------------------------------------
    public void testGetChronology_WithChronologyParameter() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology chrono = CalendarConverter.INSTANCE.getChronology(cal, JULIAN);
        assertEquals(JULIAN, chrono);
    }

    //-----------------------------------------------------------------------
    // Test getPartialValues
    //-----------------------------------------------------------------------
    public void testGetPartialValues() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(12345678L));
        TimeOfDay tod = new TimeOfDay();
        int[] expected = ISO.get(tod, 12345678L);
        int[] actual = CalendarConverter.INSTANCE.getPartialValues(tod, cal, ISO);
        assertTrue(Arrays.equals(expected, actual));
    }

    //-----------------------------------------------------------------------
    // Test toString
    //-----------------------------------------------------------------------
    public void testToString() {
        assertEquals("Converter[java.util.Calendar]", CalendarConverter.INSTANCE.toString());
    }

    //-----------------------------------------------------------------------
    // Helper methods
    //-----------------------------------------------------------------------
    private Calendar createBuddhistCalendar(TimeZone tz) {
        try {
            Calendar bc = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            bc.setTimeZone(tz);
            return bc;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            // Ignore if BuddhistCalendar not available (non-Sun JDK) or inaccessible (JDK 9+ modules)
            return null;
        }
    }

    //-----------------------------------------------------------------------
    // Mock classes for testing
    //-----------------------------------------------------------------------
    static class MockUnknownCalendar extends GregorianCalendar {
        public MockUnknownCalendar(TimeZone zone) {
            super(zone);
        }
    }

    static class MockUnknownTimeZone extends TimeZone {
        @Override
        public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
            return 0;
        }

        @Override
        public void setRawOffset(int offsetMillis) {}

        @Override
        public int getRawOffset() {
            return 0;
        }

        @Override
        public boolean useDaylightTime() {
            return false;
        }

        @Override
        public boolean inDaylightTime(Date date) {
            return false;
        }
    }
}