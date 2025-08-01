package org.joda.time.chrono;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Unit tests for EthiopicChronology.
 */
public class TestEthiopicChronology extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long DAYS_IN_YEAR_2002 = 365 * 8 + 366 * 3;
    private static final long TEST_TIME_NOW = (DAYS_IN_YEAR_2002 + 31 + 28 + 31 + 30 + 31 + 9 - 1) * MILLIS_PER_DAY;

    private static final DateTimeZone ZONE_PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone ZONE_LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone ZONE_TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestEthiopicChronology.class);
    }

    public TestEthiopicChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(ZONE_LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, EthiopicChronology.getInstanceUTC().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(ZONE_LONDON, EthiopicChronology.getInstance().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance().getClass());
    }

    public void testFactoryWithZone() {
        assertEquals(ZONE_TOKYO, EthiopicChronology.getInstance(ZONE_TOKYO).getZone());
        assertEquals(ZONE_PARIS, EthiopicChronology.getInstance(ZONE_PARIS).getZone());
        assertEquals(ZONE_LONDON, EthiopicChronology.getInstance(null).getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance(ZONE_TOKYO).getClass());
    }

    public void testEquality() {
        assertSame(EthiopicChronology.getInstance(ZONE_TOKYO), EthiopicChronology.getInstance(ZONE_TOKYO));
        assertSame(EthiopicChronology.getInstance(ZONE_LONDON), EthiopicChronology.getInstance(ZONE_LONDON));
        assertSame(EthiopicChronology.getInstance(ZONE_PARIS), EthiopicChronology.getInstance(ZONE_PARIS));
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
        assertSame(EthiopicChronology.getInstance(), EthiopicChronology.getInstance(ZONE_LONDON));
    }

    public void testWithUTC() {
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(ZONE_LONDON).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(ZONE_TOKYO).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC().withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(EthiopicChronology.getInstance(ZONE_TOKYO), EthiopicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_TOKYO));
        assertSame(EthiopicChronology.getInstance(ZONE_LONDON), EthiopicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_LONDON));
        assertSame(EthiopicChronology.getInstance(ZONE_PARIS), EthiopicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_PARIS));
        assertSame(EthiopicChronology.getInstance(ZONE_LONDON), EthiopicChronology.getInstance(ZONE_TOKYO).withZone(null));
        assertSame(EthiopicChronology.getInstance(ZONE_PARIS), EthiopicChronology.getInstance().withZone(ZONE_PARIS));
        assertSame(EthiopicChronology.getInstance(ZONE_PARIS), EthiopicChronology.getInstanceUTC().withZone(ZONE_PARIS));
    }

    public void testToString() {
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance(ZONE_LONDON).toString());
        assertEquals("EthiopicChronology[Asia/Tokyo]", EthiopicChronology.getInstance(ZONE_TOKYO).toString());
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance().toString());
        assertEquals("EthiopicChronology[UTC]", EthiopicChronology.getInstanceUTC().toString());
    }

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

    public void testDateFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("era", ethiopic.era().getName());
        assertEquals("centuryOfEra", ethiopic.centuryOfEra().getName());
        assertEquals("yearOfCentury", ethiopic.yearOfCentury().getName());
        assertEquals("yearOfEra", ethiopic.yearOfEra().getName());
        assertEquals("year", ethiopic.year().getName());
        assertEquals("monthOfYear", ethiopic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", ethiopic.weekyearOfCentury().getName());
        assertEquals("weekyear", ethiopic.weekyear().getName());
        assertEquals("weekOfWeekyear", ethiopic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", ethiopic.dayOfYear().getName());
        assertEquals("dayOfMonth", ethiopic.dayOfMonth().getName());
        assertEquals("dayOfWeek", ethiopic.dayOfWeek().getName());

        assertEquals(true, ethiopic.era().isSupported());
        assertEquals(true, ethiopic.centuryOfEra().isSupported());
        assertEquals(true, ethiopic.yearOfCentury().isSupported());
        assertEquals(true, ethiopic.yearOfEra().isSupported());
        assertEquals(true, ethiopic.year().isSupported());
        assertEquals(true, ethiopic.monthOfYear().isSupported());
        assertEquals(true, ethiopic.weekyearOfCentury().isSupported());
        assertEquals(true, ethiopic.weekyear().isSupported());
        assertEquals(true, ethiopic.weekOfWeekyear().isSupported());
        assertEquals(true, ethiopic.dayOfYear().isSupported());
        assertEquals(true, ethiopic.dayOfMonth().isSupported());
        assertEquals(true, ethiopic.dayOfWeek().isSupported());

        assertEquals(ethiopic.eras(), ethiopic.era().getDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.centuryOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfCentury().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.year().getDurationField());
        assertEquals(ethiopic.months(), ethiopic.monthOfYear().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyearOfCentury().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyear().getDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.weekOfWeekyear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfYear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfMonth().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfWeek().getDurationField());

        assertEquals(null, ethiopic.era().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.centuryOfEra().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.yearOfCentury().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.yearOfEra().getRangeDurationField());
        assertEquals(null, ethiopic.year().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.monthOfYear().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, ethiopic.weekyear().getRangeDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekOfWeekyear().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.dayOfYear().getRangeDurationField());
        assertEquals(ethiopic.months(), ethiopic.dayOfMonth().getRangeDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.dayOfWeek().getRangeDurationField());
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

    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

    public void testEra() {
        assertEquals(1, EthiopicChronology.EE);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
            fail();
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    public void testSampleDate() {
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).withChronology(ETHIOPIC_UTC);
        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(20, dt.getCenturyOfEra());
        assertEquals(96, dt.getYearOfCentury());
        assertEquals(1996, dt.getYearOfEra());

        assertEquals(1996, dt.getYear());
        DateTime.Property fld = dt.year();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1997, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(4));
        assertEquals(new DateTime(1996, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addWrapFieldToCopy(4));

        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, ZONE_PARIS).withChronology(ETHIOPIC_UTC);
        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(1996, dt.getYear());
        assertEquals(1996, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // PARIS is UTC+2 in summer (12-2=10)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testDurationYear() {
        // Leap 1999, NotLeap 1996,97,98
        DateTime dt96 = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt97 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt98 = new DateTime(1998, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt99 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt00 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        DurationField fld = dt96.year().getDurationField();
        assertEquals(ETHIOPIC_UTC.years(), fld);
        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt96.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4, dt96.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2));

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt96.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4L, dt96.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2L));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getUnitMillis());

        assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY + 1L, dt96.getMillis()));

        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4));

        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1L));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2L));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3L));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4L));
    }

    public void testDurationMonth() {
        // Leap 1999, NotLeap 1996,97,98
        DateTime dt11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt01 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        DurationField fld = dt11.monthOfYear().getDurationField();
        assertEquals(ETHIOPIC_UTC.months(), fld);
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3L, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4L, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13L));

        assertEquals(0, fld.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1L));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2L));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3L));
    }

    public void testLeap_5_13() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(false, dt.dayOfMonth().isLeap());
        assertEquals(false, dt.dayOfYear().isLeap());
    }

    public void testLeap_6_13() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(true, dt.dayOfMonth().isLeap());
        assertEquals(true, dt.dayOfYear().isLeap());
    }
}