package org.joda.time.format;

import java.io.CharArrayWriter;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

public class PeriodFormatterTestTest1 extends TestCase {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final DateTimeZone NEWYORK = DateTimeZone.forID("America/New_York");

    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);

    private static final Chronology BUDDHIST_PARIS = BuddhistChronology.getInstance(PARIS);

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365;

    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;

    private TimeZone originalTimeZone = null;

    private Locale originalLocale = null;

    private PeriodFormatter f = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPeriodFormatter.class);
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
        f = ISOPeriodFormat.standard();
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
        f = null;
    }

    //-----------------------------------------------------------------------
    public void testPrint_simple() {
        Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals("P1Y2M3W4DT5H6M7.008S", f.print(p));
    }
}
