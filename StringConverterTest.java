package org.joda.time.convert;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.*;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

/**
 * Unit tests for the StringConverter class.
 * Tests the conversion of strings to various Joda-Time objects.
 */
public class TestStringConverter extends TestCase {

    // Constants for time zones
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone EIGHT = DateTimeZone.forOffsetHours(8);

    // Constants for chronologies
    private static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT);
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);
    private static Chronology ISO;
    private static Chronology JULIAN;

    // Variables to store the default zone and locale
    private DateTimeZone originalZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestStringConverter.class);
    }

    public TestStringConverter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        originalZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);

        JULIAN = JulianChronology.getInstance();
        ISO = ISOChronology.getInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalZone);
        Locale.setDefault(originalLocale);
        originalZone = null;
    }

    // Test for singleton pattern in StringConverter
    public void testSingleton() throws Exception {
        Class<?> cls = StringConverter.class;
        assertFalse(Modifier.isPublic(cls.getModifiers()));
        assertFalse(Modifier.isProtected(cls.getModifiers()));
        assertFalse(Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructor();
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(constructor.getModifiers()));

        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse(Modifier.isPublic(instanceField.getModifiers()));
        assertFalse(Modifier.isProtected(instanceField.getModifiers()));
        assertFalse(Modifier.isPrivate(instanceField.getModifiers()));
    }

    // Test for supported type
    public void testSupportedType() {
        assertEquals(String.class, StringConverter.INSTANCE.getSupportedType());
    }

    // Test for converting string to instant millis
    public void testGetInstantMillis_Object() {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, EIGHT);
        assertEquals(expectedDateTime.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+08:00", ISO_EIGHT));
    }

    // Test for converting string to instant millis with time zone
    public void testGetInstantMillis_Object_Zone() {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        assertEquals(expectedDateTime.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+02:00", ISO_PARIS));
    }

    // Test for converting string to instant millis with chronology
    public void testGetInstantMillis_Object_Chronology() {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, JulianChronology.getInstance(LONDON));
        assertEquals(expectedDateTime.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", JULIAN));
    }

    // Test for invalid instant millis conversion
    public void testGetInstantMillisInvalid() {
        try {
            StringConverter.INSTANCE.getInstantMillis("", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            StringConverter.INSTANCE.getInstantMillis("X", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    // Test for getting chronology from string and zone
    public void testGetChronology_Object_Zone() {
        assertEquals(ISOChronology.getInstance(PARIS), StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", PARIS));
    }

    // Test for getting chronology from string and chronology
    public void testGetChronology_Object_Chronology() {
        assertEquals(JulianChronology.getInstance(LONDON), StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", JULIAN));
    }

    // Test for getting partial values from string
    public void testGetPartialValues() {
        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expectedValues = {3, 4, 5, 6};
        int[] actualValues = StringConverter.INSTANCE.getPartialValues(timeOfDay, "T03:04:05.006", ISOChronology.getInstance());
        assertTrue(Arrays.equals(expectedValues, actualValues));
    }

    // Test for converting string to DateTime
    public void testGetDateTime() {
        DateTime baseDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        DateTime testDateTime = new DateTime(baseDateTime.toString(), PARIS);
        assertEquals(baseDateTime, testDateTime);
    }

    // Test for converting string to duration millis
    public void testGetDurationMillis_Object1() {
        long expectedMillis = 12345;
        assertEquals(expectedMillis, StringConverter.INSTANCE.getDurationMillis("PT12.345S"));
    }

    // Test for invalid duration millis conversion
    public void testGetDurationMillis_Object2() {
        try {
            StringConverter.INSTANCE.getDurationMillis("P2Y6M9DXYZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    // Test for getting period type from string
    public void testGetPeriodType_Object() {
        assertEquals(PeriodType.standard(), StringConverter.INSTANCE.getPeriodType("P2Y6M9D"));
    }

    // Test for setting period values from string
    public void testSetIntoPeriod_Object1() {
        MutablePeriod period = new MutablePeriod(PeriodType.yearMonthDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y6M9DT12H24M48S", null);
        assertEquals(2, period.getYears());
        assertEquals(6, period.getMonths());
        assertEquals(9, period.getDays());
        assertEquals(12, period.getHours());
        assertEquals(24, period.getMinutes());
        assertEquals(48, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    // Test for invalid period conversion
    public void testSetIntoPeriod_Object8() {
        MutablePeriod period = new MutablePeriod();
        try {
            StringConverter.INSTANCE.setInto(period, "", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    // Test for checking readable interval
    public void testIsReadableInterval_Object_Chronology() {
        assertFalse(StringConverter.INSTANCE.isReadableInterval("", null));
    }

    // Test for setting interval values from string
    public void testSetIntoInterval_Object_Chronology1() {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2004-06-09/P1Y2M", null);
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), interval.getStart());
        assertEquals(new DateTime(2005, 8, 9, 0, 0, 0, 0), interval.getEnd());
        assertEquals(ISOChronology.getInstance(), interval.getChronology());
    }

    // Test for invalid interval conversion
    public void testSetIntoIntervalEx_Object_Chronology1() {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        try {
            StringConverter.INSTANCE.setInto(interval, "", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    // Test for StringConverter toString method
    public void testToString() {
        assertEquals("Converter[java.lang.String]", StringConverter.INSTANCE.toString());
    }
}