package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

public class GregorianChronologyTestTest9 extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

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
        return new TestSuite(TestGregorianChronology.class);
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
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("eras", greg.eras().getName());
        assertEquals("centuries", greg.centuries().getName());
        assertEquals("years", greg.years().getName());
        assertEquals("weekyears", greg.weekyears().getName());
        assertEquals("months", greg.months().getName());
        assertEquals("weeks", greg.weeks().getName());
        assertEquals("days", greg.days().getName());
        assertEquals("halfdays", greg.halfdays().getName());
        assertEquals("hours", greg.hours().getName());
        assertEquals("minutes", greg.minutes().getName());
        assertEquals("seconds", greg.seconds().getName());
        assertEquals("millis", greg.millis().getName());
        assertEquals(false, greg.eras().isSupported());
        assertEquals(true, greg.centuries().isSupported());
        assertEquals(true, greg.years().isSupported());
        assertEquals(true, greg.weekyears().isSupported());
        assertEquals(true, greg.months().isSupported());
        assertEquals(true, greg.weeks().isSupported());
        assertEquals(true, greg.days().isSupported());
        assertEquals(true, greg.halfdays().isSupported());
        assertEquals(true, greg.hours().isSupported());
        assertEquals(true, greg.minutes().isSupported());
        assertEquals(true, greg.seconds().isSupported());
        assertEquals(true, greg.millis().isSupported());
        assertEquals(false, greg.centuries().isPrecise());
        assertEquals(false, greg.years().isPrecise());
        assertEquals(false, greg.weekyears().isPrecise());
        assertEquals(false, greg.months().isPrecise());
        assertEquals(false, greg.weeks().isPrecise());
        assertEquals(false, greg.days().isPrecise());
        assertEquals(false, greg.halfdays().isPrecise());
        assertEquals(true, greg.hours().isPrecise());
        assertEquals(true, greg.minutes().isPrecise());
        assertEquals(true, greg.seconds().isPrecise());
        assertEquals(true, greg.millis().isPrecise());
        final GregorianChronology gregUTC = GregorianChronology.getInstanceUTC();
        assertEquals(false, gregUTC.centuries().isPrecise());
        assertEquals(false, gregUTC.years().isPrecise());
        assertEquals(false, gregUTC.weekyears().isPrecise());
        assertEquals(false, gregUTC.months().isPrecise());
        assertEquals(true, gregUTC.weeks().isPrecise());
        assertEquals(true, gregUTC.days().isPrecise());
        assertEquals(true, gregUTC.halfdays().isPrecise());
        assertEquals(true, gregUTC.hours().isPrecise());
        assertEquals(true, gregUTC.minutes().isPrecise());
        assertEquals(true, gregUTC.seconds().isPrecise());
        assertEquals(true, gregUTC.millis().isPrecise());
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregGMT = GregorianChronology.getInstance(gmt);
        assertEquals(false, gregGMT.centuries().isPrecise());
        assertEquals(false, gregGMT.years().isPrecise());
        assertEquals(false, gregGMT.weekyears().isPrecise());
        assertEquals(false, gregGMT.months().isPrecise());
        assertEquals(true, gregGMT.weeks().isPrecise());
        assertEquals(true, gregGMT.days().isPrecise());
        assertEquals(true, gregGMT.halfdays().isPrecise());
        assertEquals(true, gregGMT.hours().isPrecise());
        assertEquals(true, gregGMT.minutes().isPrecise());
        assertEquals(true, gregGMT.seconds().isPrecise());
        assertEquals(true, gregGMT.millis().isPrecise());
    }
}
