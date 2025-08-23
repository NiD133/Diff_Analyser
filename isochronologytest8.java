package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class ISOChronologyTestTest8 extends TestCase {

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
        return new TestSuite(TestISOChronology.class);
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

    private void testAdd(String start, DurationFieldType type, int amt, String end) {
        DateTime dtStart = new DateTime(start, ISOChronology.getInstanceUTC());
        DateTime dtEnd = new DateTime(end, ISOChronology.getInstanceUTC());
        assertEquals(dtEnd, dtStart.withFieldAdded(type, amt));
        assertEquals(dtStart, dtEnd.withFieldAdded(type, -amt));
        DurationField field = type.getField(ISOChronology.getInstanceUTC());
        int diff = field.getDifference(dtEnd.getMillis(), dtStart.getMillis());
        assertEquals(amt, diff);
        if (type == DurationFieldType.years() || type == DurationFieldType.months() || type == DurationFieldType.days()) {
            YearMonthDay ymdStart = new YearMonthDay(start, ISOChronology.getInstanceUTC());
            YearMonthDay ymdEnd = new YearMonthDay(end, ISOChronology.getInstanceUTC());
            assertEquals(ymdEnd, ymdStart.withFieldAdded(type, amt));
            assertEquals(ymdStart, ymdEnd.withFieldAdded(type, -amt));
        }
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final ISOChronology iso = ISOChronology.getInstance();
        assertEquals("eras", iso.eras().getName());
        assertEquals("centuries", iso.centuries().getName());
        assertEquals("years", iso.years().getName());
        assertEquals("weekyears", iso.weekyears().getName());
        assertEquals("months", iso.months().getName());
        assertEquals("weeks", iso.weeks().getName());
        assertEquals("days", iso.days().getName());
        assertEquals("halfdays", iso.halfdays().getName());
        assertEquals("hours", iso.hours().getName());
        assertEquals("minutes", iso.minutes().getName());
        assertEquals("seconds", iso.seconds().getName());
        assertEquals("millis", iso.millis().getName());
        assertEquals(false, iso.eras().isSupported());
        assertEquals(true, iso.centuries().isSupported());
        assertEquals(true, iso.years().isSupported());
        assertEquals(true, iso.weekyears().isSupported());
        assertEquals(true, iso.months().isSupported());
        assertEquals(true, iso.weeks().isSupported());
        assertEquals(true, iso.days().isSupported());
        assertEquals(true, iso.halfdays().isSupported());
        assertEquals(true, iso.hours().isSupported());
        assertEquals(true, iso.minutes().isSupported());
        assertEquals(true, iso.seconds().isSupported());
        assertEquals(true, iso.millis().isSupported());
        assertEquals(false, iso.centuries().isPrecise());
        assertEquals(false, iso.years().isPrecise());
        assertEquals(false, iso.weekyears().isPrecise());
        assertEquals(false, iso.months().isPrecise());
        assertEquals(false, iso.weeks().isPrecise());
        assertEquals(false, iso.days().isPrecise());
        assertEquals(false, iso.halfdays().isPrecise());
        assertEquals(true, iso.hours().isPrecise());
        assertEquals(true, iso.minutes().isPrecise());
        assertEquals(true, iso.seconds().isPrecise());
        assertEquals(true, iso.millis().isPrecise());
        final ISOChronology isoUTC = ISOChronology.getInstanceUTC();
        assertEquals(false, isoUTC.centuries().isPrecise());
        assertEquals(false, isoUTC.years().isPrecise());
        assertEquals(false, isoUTC.weekyears().isPrecise());
        assertEquals(false, isoUTC.months().isPrecise());
        assertEquals(true, isoUTC.weeks().isPrecise());
        assertEquals(true, isoUTC.days().isPrecise());
        assertEquals(true, isoUTC.halfdays().isPrecise());
        assertEquals(true, isoUTC.hours().isPrecise());
        assertEquals(true, isoUTC.minutes().isPrecise());
        assertEquals(true, isoUTC.seconds().isPrecise());
        assertEquals(true, isoUTC.millis().isPrecise());
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final ISOChronology isoGMT = ISOChronology.getInstance(gmt);
        assertEquals(false, isoGMT.centuries().isPrecise());
        assertEquals(false, isoGMT.years().isPrecise());
        assertEquals(false, isoGMT.weekyears().isPrecise());
        assertEquals(false, isoGMT.months().isPrecise());
        assertEquals(true, isoGMT.weeks().isPrecise());
        assertEquals(true, isoGMT.days().isPrecise());
        assertEquals(true, isoGMT.halfdays().isPrecise());
        assertEquals(true, isoGMT.hours().isPrecise());
        assertEquals(true, isoGMT.minutes().isPrecise());
        assertEquals(true, isoGMT.seconds().isPrecise());
        assertEquals(true, isoGMT.millis().isPrecise());
        final DateTimeZone offset = DateTimeZone.forOffsetHours(1);
        final ISOChronology isoOffset1 = ISOChronology.getInstance(offset);
        assertEquals(false, isoOffset1.centuries().isPrecise());
        assertEquals(false, isoOffset1.years().isPrecise());
        assertEquals(false, isoOffset1.weekyears().isPrecise());
        assertEquals(false, isoOffset1.months().isPrecise());
        assertEquals(true, isoOffset1.weeks().isPrecise());
        assertEquals(true, isoOffset1.days().isPrecise());
        assertEquals(true, isoOffset1.halfdays().isPrecise());
        assertEquals(true, isoOffset1.hours().isPrecise());
        assertEquals(true, isoOffset1.minutes().isPrecise());
        assertEquals(true, isoOffset1.seconds().isPrecise());
        assertEquals(true, isoOffset1.millis().isPrecise());
    }
}
