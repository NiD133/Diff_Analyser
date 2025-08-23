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

public class CopticChronologyTestTest9 extends TestCase {

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

    public void testDateFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertEquals("era", coptic.era().getName());
        assertEquals("centuryOfEra", coptic.centuryOfEra().getName());
        assertEquals("yearOfCentury", coptic.yearOfCentury().getName());
        assertEquals("yearOfEra", coptic.yearOfEra().getName());
        assertEquals("year", coptic.year().getName());
        assertEquals("monthOfYear", coptic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", coptic.weekyearOfCentury().getName());
        assertEquals("weekyear", coptic.weekyear().getName());
        assertEquals("weekOfWeekyear", coptic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", coptic.dayOfYear().getName());
        assertEquals("dayOfMonth", coptic.dayOfMonth().getName());
        assertEquals("dayOfWeek", coptic.dayOfWeek().getName());
        assertEquals(true, coptic.era().isSupported());
        assertEquals(true, coptic.centuryOfEra().isSupported());
        assertEquals(true, coptic.yearOfCentury().isSupported());
        assertEquals(true, coptic.yearOfEra().isSupported());
        assertEquals(true, coptic.year().isSupported());
        assertEquals(true, coptic.monthOfYear().isSupported());
        assertEquals(true, coptic.weekyearOfCentury().isSupported());
        assertEquals(true, coptic.weekyear().isSupported());
        assertEquals(true, coptic.weekOfWeekyear().isSupported());
        assertEquals(true, coptic.dayOfYear().isSupported());
        assertEquals(true, coptic.dayOfMonth().isSupported());
        assertEquals(true, coptic.dayOfWeek().isSupported());
        assertEquals(coptic.eras(), coptic.era().getDurationField());
        assertEquals(coptic.centuries(), coptic.centuryOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfCentury().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.year().getDurationField());
        assertEquals(coptic.months(), coptic.monthOfYear().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyearOfCentury().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyear().getDurationField());
        assertEquals(coptic.weeks(), coptic.weekOfWeekyear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfYear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfMonth().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfWeek().getDurationField());
        assertEquals(null, coptic.era().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.centuryOfEra().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.yearOfCentury().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.yearOfEra().getRangeDurationField());
        assertEquals(null, coptic.year().getRangeDurationField());
        assertEquals(coptic.years(), coptic.monthOfYear().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, coptic.weekyear().getRangeDurationField());
        assertEquals(coptic.weekyears(), coptic.weekOfWeekyear().getRangeDurationField());
        assertEquals(coptic.years(), coptic.dayOfYear().getRangeDurationField());
        assertEquals(coptic.months(), coptic.dayOfMonth().getRangeDurationField());
        assertEquals(coptic.weeks(), coptic.dayOfWeek().getRangeDurationField());
    }
}
