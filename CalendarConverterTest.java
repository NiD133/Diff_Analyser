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
 * Unit tests for the CalendarConverter class.
 */
public class TestCalendarConverter extends TestCase {

    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW_TIMEZONE = DateTimeZone.forID("Europe/Moscow");
    private static Chronology julianChronology;
    private static Chronology isoChronology;

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
        julianChronology = JulianChronology.getInstance();
        isoChronology = ISOChronology.getInstance();
    }

    /**
     * Test that CalendarConverter is a singleton with a protected constructor.
     */
    public void testSingleton() throws Exception {
        Class<?> cls = CalendarConverter.class;
        assertFalse("Class should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructor();
        assertEquals("There should be only one constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(constructor.getModifiers()));

        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should not be public", Modifier.isPublic(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be protected", Modifier.isProtected(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be private", Modifier.isPrivate(instanceField.getModifiers()));
    }

    /**
     * Test that the supported type is Calendar.class.
     */
    public void testSupportedType() throws Exception {
        assertEquals("Supported type should be Calendar.class", Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    /**
     * Test getting instant milliseconds from a Calendar object.
     */
    public void testGetInstantMillis_Object_Chronology() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(123L));
        assertEquals("Instant millis should match", 123L, CalendarConverter.INSTANCE.getInstantMillis(calendar, julianChronology));
        assertEquals("Calendar time should remain unchanged", 123L, calendar.getTime().getTime());
    }

    /**
     * Test getting the appropriate Chronology for a Calendar object and a specified DateTimeZone.
     */
    public void testGetChronology_Object_Zone() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Chronology should match GJChronology with Moscow timezone", 
                     GJChronology.getInstance(MOSCOW_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW_TIMEZONE));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Chronology should match GJChronology with default timezone", 
                     GJChronology.getInstance(), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (DateTimeZone) null));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(0L));
        assertEquals("Chronology should match GJChronology with specific change date", 
                     GJChronology.getInstance(MOSCOW_TIMEZONE, 0L, 4), 
                     CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW_TIMEZONE));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        assertEquals("Chronology should match JulianChronology with Paris timezone", 
                     JulianChronology.getInstance(PARIS_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, PARIS_TIMEZONE));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        assertEquals("Chronology should match GregorianChronology with Paris timezone", 
                     GregorianChronology.getInstance(PARIS_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, PARIS_TIMEZONE));

        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Chronology should match ISOChronology with Paris timezone", 
                     ISOChronology.getInstance(PARIS_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(unknownCalendar, PARIS_TIMEZONE));

        try {
            Calendar buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertEquals("Chronology should match BuddhistChronology with Paris timezone", 
                         BuddhistChronology.getInstance(PARIS_TIMEZONE), 
                         CalendarConverter.INSTANCE.getChronology(buddhistCalendar, PARIS_TIMEZONE));
        } catch (ClassNotFoundException | IllegalAccessException ex) {
            // Ignore if not Sun JDK or due to JDK 9 modules
        }
    }

    /**
     * Test getting the appropriate Chronology for a Calendar object with a null Chronology.
     */
    public void testGetChronology_Object_nullChronology() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Chronology should match GJChronology with Paris timezone", 
                     GJChronology.getInstance(PARIS_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(0L));
        assertEquals("Chronology should match GJChronology with specific change date", 
                     GJChronology.getInstance(MOSCOW_TIMEZONE, 0L, 4), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        assertEquals("Chronology should match JulianChronology with Moscow timezone", 
                     JulianChronology.getInstance(MOSCOW_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        assertEquals("Chronology should match GregorianChronology with Moscow timezone", 
                     GregorianChronology.getInstance(MOSCOW_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));

        calendar = new GregorianCalendar(new MockUnknownTimeZone());
        assertEquals("Chronology should match GJChronology with default timezone", 
                     GJChronology.getInstance(), 
                     CalendarConverter.INSTANCE.getChronology(calendar, (Chronology) null));

        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Chronology should match ISOChronology with Moscow timezone", 
                     ISOChronology.getInstance(MOSCOW_TIMEZONE), 
                     CalendarConverter.INSTANCE.getChronology(unknownCalendar, (Chronology) null));

        try {
            Calendar buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertEquals("Chronology should match BuddhistChronology with Moscow timezone", 
                         BuddhistChronology.getInstance(MOSCOW_TIMEZONE), 
                         CalendarConverter.INSTANCE.getChronology(buddhistCalendar, (Chronology) null));
        } catch (ClassNotFoundException | IllegalAccessException ex) {
            // Ignore if not Sun JDK or due to JDK 9 modules
        }
    }

    /**
     * Test getting the appropriate Chronology for a Calendar object and a specified Chronology.
     */
    public void testGetChronology_Object_Chronology() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Chronology should match the specified JulianChronology", 
                     julianChronology, 
                     CalendarConverter.INSTANCE.getChronology(calendar, julianChronology));
    }

    /**
     * Test getting partial values from a Calendar object.
     */
    public void testGetPartialValues() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(12345678L));
        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expectedValues = isoChronology.get(timeOfDay, 12345678L);
        int[] actualValues = CalendarConverter.INSTANCE.getPartialValues(timeOfDay, calendar, isoChronology);
        assertTrue("Partial values should match", Arrays.equals(expectedValues, actualValues));
    }

    /**
     * Test the toString method of CalendarConverter.
     */
    public void testToString() {
        assertEquals("String representation should match", 
                     "Converter[java.util.Calendar]", 
                     CalendarConverter.INSTANCE.toString());
    }
}