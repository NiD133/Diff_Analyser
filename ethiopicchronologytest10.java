package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

public class EthiopicChronologyTestTest10 extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;

    private static long SKIP = 1 * MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

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
        return new TestSuite(TestEthiopicChronology.class);
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
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("halfdayOfDay", ethiopic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", ethiopic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", ethiopic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", ethiopic.clockhourOfDay().getName());
        assertEquals("hourOfDay", ethiopic.hourOfDay().getName());
        assertEquals("minuteOfDay", ethiopic.minuteOfDay().getName());
        assertEquals("minuteOfHour", ethiopic.minuteOfHour().getName());
        assertEquals("secondOfDay", ethiopic.secondOfDay().getName());
        assertEquals("secondOfMinute", ethiopic.secondOfMinute().getName());
        assertEquals("millisOfDay", ethiopic.millisOfDay().getName());
        assertEquals("millisOfSecond", ethiopic.millisOfSecond().getName());
        assertEquals(true, ethiopic.halfdayOfDay().isSupported());
        assertEquals(true, ethiopic.clockhourOfHalfday().isSupported());
        assertEquals(true, ethiopic.hourOfHalfday().isSupported());
        assertEquals(true, ethiopic.clockhourOfDay().isSupported());
        assertEquals(true, ethiopic.hourOfDay().isSupported());
        assertEquals(true, ethiopic.minuteOfDay().isSupported());
        assertEquals(true, ethiopic.minuteOfHour().isSupported());
        assertEquals(true, ethiopic.secondOfDay().isSupported());
        assertEquals(true, ethiopic.secondOfMinute().isSupported());
        assertEquals(true, ethiopic.millisOfDay().isSupported());
        assertEquals(true, ethiopic.millisOfSecond().isSupported());
    }
}
