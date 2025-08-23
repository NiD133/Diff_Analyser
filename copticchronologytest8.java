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

public class CopticChronologyTestTest8 extends TestCase {

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

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertEquals("eras", coptic.eras().getName());
        assertEquals("centuries", coptic.centuries().getName());
        assertEquals("years", coptic.years().getName());
        assertEquals("weekyears", coptic.weekyears().getName());
        assertEquals("months", coptic.months().getName());
        assertEquals("weeks", coptic.weeks().getName());
        assertEquals("days", coptic.days().getName());
        assertEquals("halfdays", coptic.halfdays().getName());
        assertEquals("hours", coptic.hours().getName());
        assertEquals("minutes", coptic.minutes().getName());
        assertEquals("seconds", coptic.seconds().getName());
        assertEquals("millis", coptic.millis().getName());
        assertEquals(false, coptic.eras().isSupported());
        assertEquals(true, coptic.centuries().isSupported());
        assertEquals(true, coptic.years().isSupported());
        assertEquals(true, coptic.weekyears().isSupported());
        assertEquals(true, coptic.months().isSupported());
        assertEquals(true, coptic.weeks().isSupported());
        assertEquals(true, coptic.days().isSupported());
        assertEquals(true, coptic.halfdays().isSupported());
        assertEquals(true, coptic.hours().isSupported());
        assertEquals(true, coptic.minutes().isSupported());
        assertEquals(true, coptic.seconds().isSupported());
        assertEquals(true, coptic.millis().isSupported());
        assertEquals(false, coptic.centuries().isPrecise());
        assertEquals(false, coptic.years().isPrecise());
        assertEquals(false, coptic.weekyears().isPrecise());
        assertEquals(false, coptic.months().isPrecise());
        assertEquals(false, coptic.weeks().isPrecise());
        assertEquals(false, coptic.days().isPrecise());
        assertEquals(false, coptic.halfdays().isPrecise());
        assertEquals(true, coptic.hours().isPrecise());
        assertEquals(true, coptic.minutes().isPrecise());
        assertEquals(true, coptic.seconds().isPrecise());
        assertEquals(true, coptic.millis().isPrecise());
        final CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        assertEquals(false, copticUTC.centuries().isPrecise());
        assertEquals(false, copticUTC.years().isPrecise());
        assertEquals(false, copticUTC.weekyears().isPrecise());
        assertEquals(false, copticUTC.months().isPrecise());
        assertEquals(true, copticUTC.weeks().isPrecise());
        assertEquals(true, copticUTC.days().isPrecise());
        assertEquals(true, copticUTC.halfdays().isPrecise());
        assertEquals(true, copticUTC.hours().isPrecise());
        assertEquals(true, copticUTC.minutes().isPrecise());
        assertEquals(true, copticUTC.seconds().isPrecise());
        assertEquals(true, copticUTC.millis().isPrecise());
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final CopticChronology copticGMT = CopticChronology.getInstance(gmt);
        assertEquals(false, copticGMT.centuries().isPrecise());
        assertEquals(false, copticGMT.years().isPrecise());
        assertEquals(false, copticGMT.weekyears().isPrecise());
        assertEquals(false, copticGMT.months().isPrecise());
        assertEquals(true, copticGMT.weeks().isPrecise());
        assertEquals(true, copticGMT.days().isPrecise());
        assertEquals(true, copticGMT.halfdays().isPrecise());
        assertEquals(true, copticGMT.hours().isPrecise());
        assertEquals(true, copticGMT.minutes().isPrecise());
        assertEquals(true, copticGMT.seconds().isPrecise());
        assertEquals(true, copticGMT.millis().isPrecise());
    }
}
