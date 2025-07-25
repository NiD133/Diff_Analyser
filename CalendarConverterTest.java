package org.joda.time.convert;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Test suite for {@link CalendarConverter}.
 * <p>
 * This class tests the functionality of the {@link CalendarConverter} class,
 * ensuring it correctly converts {@link Calendar} objects to Joda-Time objects.
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
     * Tests that {@link CalendarConverter} is a singleton and has the expected access modifiers.
     */
    public void testSingleton() throws Exception {
        Class<?> cls = CalendarConverter.class;

        // Verify class-level modifiers (should not be public, protected, or private)
        assertFalse("CalendarConverter should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("CalendarConverter should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("CalendarConverter should not be private", Modifier.isPrivate(cls.getModifiers()));

        // Verify constructor modifiers (should be protected)
        Constructor<?> con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals("Should only have one constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(con.getModifiers()));

        // Verify INSTANCE field modifiers (should not be public, protected, or private)
        Field fld = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should not be public", Modifier.isPublic(fld.getModifiers()));
        assertFalse("INSTANCE field should not be protected", Modifier.isProtected(fld.getModifiers()));
        assertFalse("INSTANCE field should not be private", Modifier.isPrivate(fld.getModifiers()));
    }

    /**
     * Tests that the {@link CalendarConverter#getSupportedType()} method returns the correct type (Calendar.class).
     */
    public void testSupportedType() throws Exception {
        assertEquals("Supported type should be Calendar.class", Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    /**
     * Tests the {@link CalendarConverter#getInstantMillis(Object, Chronology)} method.
     * It checks if the method correctly retrieves the instant milliseconds from a Calendar object.
     */
    public void testGetInstantMillis_Object_Chronology() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(123L));

        long expectedMillis = 123L;
        long actualMillis = CalendarConverter.INSTANCE.getInstantMillis(cal, JULIAN);

        assertEquals("Instant millis should match", expectedMillis, actualMillis);
        assertEquals("Calendar time should not be modified", expectedMillis, cal.getTime().getTime());
    }

    /**
     * Tests the {@link CalendarConverter#getChronology(Object, DateTimeZone)} method with different Calendar configurations.
     * It verifies that the method returns the correct Chronology based on the Calendar's TimeZone and Gregorian change date.
     */
    public void testGetChronology_Object_Zone() throws Exception {
        GregorianCalendar cal;

        // Test with different time zones and Gregorian change dates
        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(GJChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(cal, MOSCOW));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals(GJChronology.getInstance(), CalendarConverter.INSTANCE.getChronology(cal, (DateTimeZone) null));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(0L));
        assertEquals(GJChronology.getInstance(MOSCOW, 0L, 4), CalendarConverter.INSTANCE.getChronology(cal, MOSCOW));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        assertEquals(JulianChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(cal, PARIS));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        assertEquals(GregorianChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(cal, PARIS));

        // Test with a MockUnknownCalendar
        Calendar uc = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals(ISOChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(uc, PARIS));

        // Test with BuddhistCalendar (if available)
        try {
            Calendar bc = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            bc.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertEquals(BuddhistChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(bc, PARIS));
        } catch (ClassNotFoundException ex) {
            // Ignore if not Sun JDK
        } catch (IllegalAccessException ex) {
            // Ignore JDK 9 modules access restrictions
        }
    }

    /**
     * Tests the {@link CalendarConverter#getChronology(Object, Chronology)} method when a null Chronology is passed.
     * It ensures that the correct Chronology is derived based on the Calendar's configuration.
     */
    public void testGetChronology_Object_nullChronology() throws Exception {
        GregorianCalendar cal;

        // Test with different time zones and Gregorian change dates
        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(GJChronology.getInstance(PARIS), CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(0L));
        assertEquals(GJChronology.getInstance(MOSCOW, 0L, 4), CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MAX_VALUE));
        assertEquals(JulianChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null));

        cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        assertEquals(GregorianChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null));

        cal = new GregorianCalendar(new MockUnknownTimeZone());
        assertEquals(GJChronology.getInstance(), CalendarConverter.INSTANCE.getChronology(cal, (Chronology) null));

        // Test with a MockUnknownCalendar
        Calendar uc = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals(ISOChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(uc, (Chronology) null));

        // Test with BuddhistCalendar (if available)
        try {
            Calendar bc = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            bc.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            assertEquals(BuddhistChronology.getInstance(MOSCOW), CalendarConverter.INSTANCE.getChronology(bc, (Chronology) null));
        } catch (ClassNotFoundException ex) {
            // Ignore if not Sun JDK
        } catch (IllegalAccessException ex) {
            // Ignore JDK 9 modules access restrictions
        }
    }

    /**
     * Tests the {@link CalendarConverter#getChronology(Object, Chronology)} method when a specific Chronology is provided.
     * It verifies that the provided Chronology is returned without modification.
     */
    public void testGetChronology_Object_Chronology() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("Should return the provided chronology", JULIAN, CalendarConverter.INSTANCE.getChronology(cal, JULIAN));
    }

    /**
     * Tests the {@link CalendarConverter#getPartialValues(org.joda.time.ReadablePartial, Object, Chronology)} method.
     * It checks if the method correctly extracts partial values from a Calendar object based on the provided Chronology.
     */
    public void testGetPartialValues() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(12345678L));
        TimeOfDay tod = new TimeOfDay();

        int[] expected = ISO.get(tod, 12345678L);
        int[] actual = CalendarConverter.INSTANCE.getPartialValues(tod, cal, ISO);

        assertTrue("Partial values should match", Arrays.equals(expected, actual));
    }

    /**
     * Tests the {@link CalendarConverter#toString()} method.
     * It verifies that the method returns the expected string representation of the converter.
     */
    public void testToString() {
        assertEquals("toString() should return the correct string", "Converter[java.util.Calendar]", CalendarConverter.INSTANCE.toString());
    }

}