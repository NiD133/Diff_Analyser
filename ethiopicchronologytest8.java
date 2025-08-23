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

public class EthiopicChronologyTestTest8 extends TestCase {

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

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("eras", ethiopic.eras().getName());
        assertEquals("centuries", ethiopic.centuries().getName());
        assertEquals("years", ethiopic.years().getName());
        assertEquals("weekyears", ethiopic.weekyears().getName());
        assertEquals("months", ethiopic.months().getName());
        assertEquals("weeks", ethiopic.weeks().getName());
        assertEquals("days", ethiopic.days().getName());
        assertEquals("halfdays", ethiopic.halfdays().getName());
        assertEquals("hours", ethiopic.hours().getName());
        assertEquals("minutes", ethiopic.minutes().getName());
        assertEquals("seconds", ethiopic.seconds().getName());
        assertEquals("millis", ethiopic.millis().getName());
        assertEquals(false, ethiopic.eras().isSupported());
        assertEquals(true, ethiopic.centuries().isSupported());
        assertEquals(true, ethiopic.years().isSupported());
        assertEquals(true, ethiopic.weekyears().isSupported());
        assertEquals(true, ethiopic.months().isSupported());
        assertEquals(true, ethiopic.weeks().isSupported());
        assertEquals(true, ethiopic.days().isSupported());
        assertEquals(true, ethiopic.halfdays().isSupported());
        assertEquals(true, ethiopic.hours().isSupported());
        assertEquals(true, ethiopic.minutes().isSupported());
        assertEquals(true, ethiopic.seconds().isSupported());
        assertEquals(true, ethiopic.millis().isSupported());
        assertEquals(false, ethiopic.centuries().isPrecise());
        assertEquals(false, ethiopic.years().isPrecise());
        assertEquals(false, ethiopic.weekyears().isPrecise());
        assertEquals(false, ethiopic.months().isPrecise());
        assertEquals(false, ethiopic.weeks().isPrecise());
        assertEquals(false, ethiopic.days().isPrecise());
        assertEquals(false, ethiopic.halfdays().isPrecise());
        assertEquals(true, ethiopic.hours().isPrecise());
        assertEquals(true, ethiopic.minutes().isPrecise());
        assertEquals(true, ethiopic.seconds().isPrecise());
        assertEquals(true, ethiopic.millis().isPrecise());
        final EthiopicChronology ethiopicUTC = EthiopicChronology.getInstanceUTC();
        assertEquals(false, ethiopicUTC.centuries().isPrecise());
        assertEquals(false, ethiopicUTC.years().isPrecise());
        assertEquals(false, ethiopicUTC.weekyears().isPrecise());
        assertEquals(false, ethiopicUTC.months().isPrecise());
        assertEquals(true, ethiopicUTC.weeks().isPrecise());
        assertEquals(true, ethiopicUTC.days().isPrecise());
        assertEquals(true, ethiopicUTC.halfdays().isPrecise());
        assertEquals(true, ethiopicUTC.hours().isPrecise());
        assertEquals(true, ethiopicUTC.minutes().isPrecise());
        assertEquals(true, ethiopicUTC.seconds().isPrecise());
        assertEquals(true, ethiopicUTC.millis().isPrecise());
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final EthiopicChronology ethiopicGMT = EthiopicChronology.getInstance(gmt);
        assertEquals(false, ethiopicGMT.centuries().isPrecise());
        assertEquals(false, ethiopicGMT.years().isPrecise());
        assertEquals(false, ethiopicGMT.weekyears().isPrecise());
        assertEquals(false, ethiopicGMT.months().isPrecise());
        assertEquals(true, ethiopicGMT.weeks().isPrecise());
        assertEquals(true, ethiopicGMT.days().isPrecise());
        assertEquals(true, ethiopicGMT.halfdays().isPrecise());
        assertEquals(true, ethiopicGMT.hours().isPrecise());
        assertEquals(true, ethiopicGMT.minutes().isPrecise());
        assertEquals(true, ethiopicGMT.seconds().isPrecise());
        assertEquals(true, ethiopicGMT.millis().isPrecise());
    }
}
