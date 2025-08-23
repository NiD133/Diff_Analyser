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
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

public class IslamicChronologyTestTest8 extends TestCase {

    private static long SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();

    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365;

    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;

    private TimeZone originalTimeZone = null;

    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        return new TestSuite(TestIslamicChronology.class);
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
        final IslamicChronology islamic = IslamicChronology.getInstance();
        assertEquals("eras", islamic.eras().getName());
        assertEquals("centuries", islamic.centuries().getName());
        assertEquals("years", islamic.years().getName());
        assertEquals("weekyears", islamic.weekyears().getName());
        assertEquals("months", islamic.months().getName());
        assertEquals("weeks", islamic.weeks().getName());
        assertEquals("days", islamic.days().getName());
        assertEquals("halfdays", islamic.halfdays().getName());
        assertEquals("hours", islamic.hours().getName());
        assertEquals("minutes", islamic.minutes().getName());
        assertEquals("seconds", islamic.seconds().getName());
        assertEquals("millis", islamic.millis().getName());
        assertEquals(false, islamic.eras().isSupported());
        assertEquals(true, islamic.centuries().isSupported());
        assertEquals(true, islamic.years().isSupported());
        assertEquals(true, islamic.weekyears().isSupported());
        assertEquals(true, islamic.months().isSupported());
        assertEquals(true, islamic.weeks().isSupported());
        assertEquals(true, islamic.days().isSupported());
        assertEquals(true, islamic.halfdays().isSupported());
        assertEquals(true, islamic.hours().isSupported());
        assertEquals(true, islamic.minutes().isSupported());
        assertEquals(true, islamic.seconds().isSupported());
        assertEquals(true, islamic.millis().isSupported());
        assertEquals(false, islamic.centuries().isPrecise());
        assertEquals(false, islamic.years().isPrecise());
        assertEquals(false, islamic.weekyears().isPrecise());
        assertEquals(false, islamic.months().isPrecise());
        assertEquals(false, islamic.weeks().isPrecise());
        assertEquals(false, islamic.days().isPrecise());
        assertEquals(false, islamic.halfdays().isPrecise());
        assertEquals(true, islamic.hours().isPrecise());
        assertEquals(true, islamic.minutes().isPrecise());
        assertEquals(true, islamic.seconds().isPrecise());
        assertEquals(true, islamic.millis().isPrecise());
        final IslamicChronology islamicUTC = IslamicChronology.getInstanceUTC();
        assertEquals(false, islamicUTC.centuries().isPrecise());
        assertEquals(false, islamicUTC.years().isPrecise());
        assertEquals(false, islamicUTC.weekyears().isPrecise());
        assertEquals(false, islamicUTC.months().isPrecise());
        assertEquals(true, islamicUTC.weeks().isPrecise());
        assertEquals(true, islamicUTC.days().isPrecise());
        assertEquals(true, islamicUTC.halfdays().isPrecise());
        assertEquals(true, islamicUTC.hours().isPrecise());
        assertEquals(true, islamicUTC.minutes().isPrecise());
        assertEquals(true, islamicUTC.seconds().isPrecise());
        assertEquals(true, islamicUTC.millis().isPrecise());
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final IslamicChronology islamicGMT = IslamicChronology.getInstance(gmt);
        assertEquals(false, islamicGMT.centuries().isPrecise());
        assertEquals(false, islamicGMT.years().isPrecise());
        assertEquals(false, islamicGMT.weekyears().isPrecise());
        assertEquals(false, islamicGMT.months().isPrecise());
        assertEquals(true, islamicGMT.weeks().isPrecise());
        assertEquals(true, islamicGMT.days().isPrecise());
        assertEquals(true, islamicGMT.halfdays().isPrecise());
        assertEquals(true, islamicGMT.hours().isPrecise());
        assertEquals(true, islamicGMT.minutes().isPrecise());
        assertEquals(true, islamicGMT.seconds().isPrecise());
        assertEquals(true, islamicGMT.millis().isPrecise());
    }
}
