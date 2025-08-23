package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the properties of date-related fields in EthiopicChronology.
 * This test verifies field names, support, duration fields, and range duration fields.
 */
public class EthiopicChronologyDateFieldsTest {

    // A specific, well-defined instant for testing.
    // The original calculation for 2002-06-09 was complex; this is much clearer.
    private static final long TEST_TIME_NOW =
            new DateTime(2002, 6, 9, 0, 0, ISOChronology.getInstanceUTC()).getMillis();

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() throws Exception {
        // Set up a fixed time and default zone/locale for predictable test results.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        // Restore original global state to avoid side effects on other tests.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testDateFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance(LONDON);

        // This test verifies the fundamental properties of each date field.
        // By calling a helper method, we avoid repetition and make the expected
        // configuration of each field explicit and easy to read.

        // --- Era and Year-based fields ---
        assertDateField(ethiopic.era(), "era", ethiopic.eras(), null);
        assertDateField(ethiopic.yearOfEra(), "yearOfEra", ethiopic.years(), ethiopic.eras());
        assertDateField(ethiopic.centuryOfEra(), "centuryOfEra", ethiopic.centuries(), ethiopic.eras());
        assertDateField(ethiopic.yearOfCentury(), "yearOfCentury", ethiopic.years(), ethiopic.centuries());
        assertDateField(ethiopic.year(), "year", ethiopic.years(), null);

        // --- Month and Day fields ---
        assertDateField(ethiopic.monthOfYear(), "monthOfYear", ethiopic.months(), ethiopic.years());
        assertDateField(ethiopic.dayOfMonth(), "dayOfMonth", ethiopic.days(), ethiopic.months());
        assertDateField(ethiopic.dayOfYear(), "dayOfYear", ethiopic.days(), ethiopic.years());
        assertDateField(ethiopic.dayOfWeek(), "dayOfWeek", ethiopic.days(), ethiopic.weeks());

        // --- Week-based fields ---
        assertDateField(ethiopic.weekyear(), "weekyear", ethiopic.weekyears(), null);
        assertDateField(ethiopic.weekyearOfCentury(), "weekyearOfCentury", ethiopic.weekyears(), ethiopic.centuries());
        assertDateField(ethiopic.weekOfWeekyear(), "weekOfWeekyear", ethiopic.weeks(), ethiopic.weekyears());
    }

    /**
     * Helper method to assert the common properties of a DateTimeField.
     *
     * @param field              the field to test
     * @param name               the expected name of the field
     * @param durationField      the expected duration field
     * @param rangeDurationField the expected range duration field
     */
    private void assertDateField(DateTimeField field, String name, DurationField durationField, DurationField rangeDurationField) {
        assertNotNull("Field '" + name + "' should not be null", field);

        // All date fields in this test are expected to be supported.
        assertTrue("Field '" + name + "' should be supported", field.isSupported());

        // Verify the field's name.
        assertEquals("Field name mismatch for '" + name + "'", name, field.getName());

        // Verify the duration and range duration fields, using assertSame to check
        // for the exact expected singleton instance.
        assertSame("DurationField mismatch for '" + name + "'", durationField, field.getDurationField());
        assertSame("RangeDurationField mismatch for '" + name + "'", rangeDurationField, field.getRangeDurationField());
    }
}