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

public class EthiopicChronologyTestTest9 extends TestCase {

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
}
