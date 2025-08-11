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
 * Unit tests for CalendarConverter.
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

    /**
     * Test that CalendarConverter is a singleton.
     */
    public void testSingletonPattern() throws Exception {
        Class<?> cls = CalendarConverter.class;
        assertFalse("Class should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructor();
        assertEquals("Should have one constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(constructor.getModifiers()));

        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should not be public", Modifier.isPublic(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be protected", Modifier.isProtected(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be private", Modifier.isPrivate(instanceField.getModifiers()));
    }

    /**
     * Test the supported type of CalendarConverter.
     */
    public void testSupportedType() {
        assertEquals("Supported type should be Calendar", Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    /**
     * Test getting instant milliseconds from a Calendar object.
     */
    public void testGetInstantMillisFromCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(123L));
        assertEquals("Instant millis should match", 123L, CalendarConverter.INSTANCE.getInstantMillis(calendar, JULIAN));
        assertEquals("Calendar time should remain unchanged", 123L, calendar.getTime().getTime());
    }

    /**
     * Test getting the correct Chronology based on Calendar and DateTimeZone.
     */
    public void testGetChronologyWithZone() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Chronology should match GJChronology with Moscow zone",
                GJChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW));

        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Chronology should match default GJChronology",
                GJChronology.getInstance(), CalendarConverter.INSTANCE.getChronology(calendar, (DateTimeZone) null));

        calendar.setGregorianChange(new Date(0L));
        assertEquals("Chronology should match GJChronology with specific cutover",
                GJChronology.getInstance(MOSCOW, 0L, 4), CalendarConverter.INSTANCE.getChronology(calendar, MOSCOW));

        calendar.setGregorianChange(new Date(Long.MAX_VALUE));
        assertEquals("Chronology should match JulianChronology with Paris zone",
                JulianChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(calendar, PARIS));

        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        assertEquals("Chronology should match GregorianChronology with Paris zone",
                GregorianChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(calendar, PARIS));

        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Chronology should match ISOChronology with Paris zone",
                ISOChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(unknownCalendar, PARIS));

        testBuddhistCalendarChronology();
    }

    /**
     * Test getting the correct Chronology based on Calendar and Chronology.
     */
    public void testGetChronologyWithChronology() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Chronology should match JULIAN", JULIAN, CalendarConverter.INSTANCE.getChronology(calendar, JULIAN));
    }

    /**
     * Test getting partial values from a Calendar object.
     */
    public void testGetPartialValues() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(12345678L));
        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expectedValues = ISO.get(timeOfDay, 12345678L);
        int[] actualValues = CalendarConverter.INSTANCE.getPartialValues(timeOfDay, calendar, ISO);
        assertTrue("Partial values should match", Arrays.equals(expectedValues, actualValues));
    }

    /**
     * Test the toString method of CalendarConverter.
     */
    public void testToString() {
        assertEquals("ToString should match", "Converter[java.util.Calendar]", CalendarConverter.INSTANCE.toString());
    }

    /**
     * Helper method to test BuddhistCalendar Chronology.
     */
    private void testBuddhistCalendarChronology() {
        try {
            Calendar buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertEquals("Chronology should match BuddhistChronology with Moscow zone",
                    BuddhistChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(buddhistCalendar, (Chronology) null));
        } catch (ClassNotFoundException | IllegalAccessException ex) {
            // Ignore if not Sun JDK or due to JDK 9 modules
        }
    }
}