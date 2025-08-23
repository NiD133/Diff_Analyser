package org.joda.time.chrono;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * Tests the properties of the date fields in CopticChronology.
 * This includes field names, support, duration fields, and range duration fields.
 */
public class CopticChronologyFieldsTest extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // A fixed point in time for consistent testing: June 9, 2002 (ISO)
    private static final long TEST_TIME_NOW =
            new DateTime(2002, 6, 9, 0, 0, ISO_UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Override
    protected void setUp() throws Exception {
        // Set a fixed current time and default time zone to ensure tests are reproducible.
        // CopticChronology.getInstance() uses the default time zone.
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
    }

    /**
     * Tests that the names of the date fields are as expected.
     */
    public void testFieldNames() {
        CopticChronology coptic = CopticChronology.getInstance();

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
    }

    /**
     * Tests that all date-related fields are supported by the Coptic chronology.
     */
    public void testAllDateFieldsAreSupported() {
        CopticChronology coptic = CopticChronology.getInstance();
        List<DateTimeField> fields = Arrays.asList(
                coptic.era(), coptic.centuryOfEra(), coptic.yearOfCentury(),
                coptic.yearOfEra(), coptic.year(), coptic.monthOfYear(),
                coptic.weekyearOfCentury(), coptic.weekyear(), coptic.weekOfWeekyear(),
                coptic.dayOfYear(), coptic.dayOfMonth(), coptic.dayOfWeek()
        );

        for (DateTimeField field : fields) {
            assertTrue("Field " + field.getName() + " should be supported", field.isSupported());
        }
    }

    /**
     * Tests that the duration field for each date field is correctly defined.
     * The duration field represents the unit of the field (e.g., years for yearOfCentury).
     */
    public void testFieldDurationFields() {
        CopticChronology coptic = CopticChronology.getInstance();

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
    }

    /**
     * Tests that the range duration field for each date field is correctly defined.
     * The range duration field represents the larger time unit that the field is a part of
     * (e.g., centuries for yearOfCentury).
     */
    public void testFieldRangeDurationFields() {
        CopticChronology coptic = CopticChronology.getInstance();

        assertNull(coptic.era().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.centuryOfEra().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.yearOfCentury().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.yearOfEra().getRangeDurationField());
        assertNull(coptic.year().getRangeDurationField());
        assertEquals(coptic.years(), coptic.monthOfYear().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.weekyearOfCentury().getRangeDurationField());
        assertNull(coptic.weekyear().getRangeDurationField());
        assertEquals(coptic.weekyears(), coptic.weekOfWeekyear().getRangeDurationField());
        assertEquals(coptic.years(), coptic.dayOfYear().getRangeDurationField());
        assertEquals(coptic.months(), coptic.dayOfMonth().getRangeDurationField());
        assertEquals(coptic.weeks(), coptic.dayOfWeek().getRangeDurationField());
    }
}