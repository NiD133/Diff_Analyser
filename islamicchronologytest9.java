package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;

/**
 * Tests the properties of the date fields in IslamicChronology.
 * This test verifies field names, support, and associated duration fields.
 */
public class IslamicChronologyFieldsTest extends TestCase {

    // A specific date to fix the "current" time for tests, corresponds to 2002-06-09Z.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, ISOChronology.getInstanceUTC()).getMillis();

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(IslamicChronologyFieldsTest.class);
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

    /**
     * Tests that the date fields of IslamicChronology are correctly configured.
     * It checks the name, supported status, duration field, and range duration field for each date-related field.
     */
    public void testDateFields() {
        final IslamicChronology islamic = IslamicChronology.getInstance(LONDON);

        assertField(islamic.era(), "era", islamic.eras(), null);
        assertField(islamic.centuryOfEra(), "centuryOfEra", islamic.centuries(), islamic.eras());
        assertField(islamic.yearOfCentury(), "yearOfCentury", islamic.years(), islamic.centuries());
        assertField(islamic.yearOfEra(), "yearOfEra", islamic.years(), islamic.eras());
        assertField(islamic.year(), "year", islamic.years(), null);
        assertField(islamic.monthOfYear(), "monthOfYear", islamic.months(), islamic.years());
        assertField(islamic.weekyearOfCentury(), "weekyearOfCentury", islamic.weekyears(), islamic.centuries());
        assertField(islamic.weekyear(), "weekyear", islamic.weekyears(), null);
        assertField(islamic.weekOfWeekyear(), "weekOfWeekyear", islamic.weeks(), islamic.weekyears());
        assertField(islamic.dayOfYear(), "dayOfYear", islamic.days(), islamic.years());
        assertField(islamic.dayOfMonth(), "dayOfMonth", islamic.days(), islamic.months());
        assertField(islamic.dayOfWeek(), "dayOfWeek", islamic.days(), islamic.weeks());
    }

    /**
     * Helper method to assert the properties of a DateTimeField.
     *
     * @param field the field to test
     * @param expectedName the expected name of the field
     * @param expectedDurationField the expected duration field
     * @param expectedRangeDurationField the expected range duration field
     */
    private void assertField(
            DateTimeField field,
            String expectedName,
            DurationField expectedDurationField,
            DurationField expectedRangeDurationField) {

        // All date fields in IslamicChronology are expected to be supported.
        assertTrue("Field should be supported: " + expectedName, field.isSupported());

        assertEquals("Field name mismatch for " + expectedName, expectedName, field.getName());
        assertEquals("Duration field mismatch for " + expectedName, expectedDurationField, field.getDurationField());
        assertEquals("Range duration field mismatch for " + expectedName, expectedRangeDurationField, field.getRangeDurationField());
    }
}