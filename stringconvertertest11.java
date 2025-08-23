package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

public class StringConverterTestTest11 extends TestCase {

    private static final DateTimeZone ONE_HOUR = DateTimeZone.forOffsetHours(1);

    private static final DateTimeZone SIX = DateTimeZone.forOffsetHours(6);

    private static final DateTimeZone SEVEN = DateTimeZone.forOffsetHours(7);

    private static final DateTimeZone EIGHT = DateTimeZone.forOffsetHours(8);

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT);

    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);

    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);

    private static Chronology ISO;

    private static Chronology JULIAN;

    private DateTimeZone zone = null;

    private Locale locale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestStringConverter.class);
    }

    @Override
    protected void setUp() throws Exception {
        zone = DateTimeZone.getDefault();
        locale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
        JULIAN = JulianChronology.getInstance();
        ISO = ISOChronology.getInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(zone);
        Locale.setDefault(locale);
        zone = null;
    }

    public void testGetDateTime1() throws Exception {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+01:00");
        assertEquals(2004, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(12, test.getHourOfDay());
        assertEquals(24, test.getMinuteOfHour());
        assertEquals(48, test.getSecondOfMinute());
        assertEquals(501, test.getMillisOfSecond());
        assertEquals(LONDON, test.getZone());
    }
}
