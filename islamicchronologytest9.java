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

public class IslamicChronologyTestTest9 extends TestCase {

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

    public void testDateFields() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        assertEquals("era", islamic.era().getName());
        assertEquals("centuryOfEra", islamic.centuryOfEra().getName());
        assertEquals("yearOfCentury", islamic.yearOfCentury().getName());
        assertEquals("yearOfEra", islamic.yearOfEra().getName());
        assertEquals("year", islamic.year().getName());
        assertEquals("monthOfYear", islamic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", islamic.weekyearOfCentury().getName());
        assertEquals("weekyear", islamic.weekyear().getName());
        assertEquals("weekOfWeekyear", islamic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", islamic.dayOfYear().getName());
        assertEquals("dayOfMonth", islamic.dayOfMonth().getName());
        assertEquals("dayOfWeek", islamic.dayOfWeek().getName());
        assertEquals(true, islamic.era().isSupported());
        assertEquals(true, islamic.centuryOfEra().isSupported());
        assertEquals(true, islamic.yearOfCentury().isSupported());
        assertEquals(true, islamic.yearOfEra().isSupported());
        assertEquals(true, islamic.year().isSupported());
        assertEquals(true, islamic.monthOfYear().isSupported());
        assertEquals(true, islamic.weekyearOfCentury().isSupported());
        assertEquals(true, islamic.weekyear().isSupported());
        assertEquals(true, islamic.weekOfWeekyear().isSupported());
        assertEquals(true, islamic.dayOfYear().isSupported());
        assertEquals(true, islamic.dayOfMonth().isSupported());
        assertEquals(true, islamic.dayOfWeek().isSupported());
        assertEquals(islamic.eras(), islamic.era().getDurationField());
        assertEquals(islamic.centuries(), islamic.centuryOfEra().getDurationField());
        assertEquals(islamic.years(), islamic.yearOfCentury().getDurationField());
        assertEquals(islamic.years(), islamic.yearOfEra().getDurationField());
        assertEquals(islamic.years(), islamic.year().getDurationField());
        assertEquals(islamic.months(), islamic.monthOfYear().getDurationField());
        assertEquals(islamic.weekyears(), islamic.weekyearOfCentury().getDurationField());
        assertEquals(islamic.weekyears(), islamic.weekyear().getDurationField());
        assertEquals(islamic.weeks(), islamic.weekOfWeekyear().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfYear().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfMonth().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfWeek().getDurationField());
        assertEquals(null, islamic.era().getRangeDurationField());
        assertEquals(islamic.eras(), islamic.centuryOfEra().getRangeDurationField());
        assertEquals(islamic.centuries(), islamic.yearOfCentury().getRangeDurationField());
        assertEquals(islamic.eras(), islamic.yearOfEra().getRangeDurationField());
        assertEquals(null, islamic.year().getRangeDurationField());
        assertEquals(islamic.years(), islamic.monthOfYear().getRangeDurationField());
        assertEquals(islamic.centuries(), islamic.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, islamic.weekyear().getRangeDurationField());
        assertEquals(islamic.weekyears(), islamic.weekOfWeekyear().getRangeDurationField());
        assertEquals(islamic.years(), islamic.dayOfYear().getRangeDurationField());
        assertEquals(islamic.months(), islamic.dayOfMonth().getRangeDurationField());
        assertEquals(islamic.weeks(), islamic.dayOfWeek().getRangeDurationField());
    }
}
