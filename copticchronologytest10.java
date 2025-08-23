package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class CopticChronologyTestTest10 extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;

    private static long SKIP = 1 * MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();

    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365;

    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;

    private TimeZone originalTimeZone = null;

    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * MILLIS_PER_DAY;
        return new TestSuite(TestCopticChronology.class);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    public void testTimeFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertEquals("halfdayOfDay", coptic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", coptic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", coptic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", coptic.clockhourOfDay().getName());
        assertEquals("hourOfDay", coptic.hourOfDay().getName());
        assertEquals("minuteOfDay", coptic.minuteOfDay().getName());
        assertEquals("minuteOfHour", coptic.minuteOfHour().getName());
        assertEquals("secondOfDay", coptic.secondOfDay().getName());
        assertEquals("secondOfMinute", coptic.secondOfMinute().getName());
        assertEquals("millisOfDay", coptic.millisOfDay().getName());
        assertEquals("millisOfSecond", coptic.millisOfSecond().getName());
        assertEquals(true, coptic.halfdayOfDay().isSupported());
        assertEquals(true, coptic.clockhourOfHalfday().isSupported());
        assertEquals(true, coptic.hourOfHalfday().isSupported());
        assertEquals(true, coptic.clockhourOfDay().isSupported());
        assertEquals(true, coptic.hourOfDay().isSupported());
        assertEquals(true, coptic.minuteOfDay().isSupported());
        assertEquals(true, coptic.minuteOfHour().isSupported());
        assertEquals(true, coptic.secondOfDay().isSupported());
        assertEquals(true, coptic.secondOfMinute().isSupported());
        assertEquals(true, coptic.millisOfDay().isSupported());
        assertEquals(true, coptic.millisOfSecond().isSupported());
    }
}
