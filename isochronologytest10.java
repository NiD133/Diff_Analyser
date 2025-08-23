package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the time-related fields in ISOChronology.
 * This test verifies that each time field has the correct name and is reported as supported.
 */
public class ISOChronologyTimeFieldsTest {

    // A fixed point in time for consistent test results: 2002-06-09T00:00:00.000Z
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0).getMillis();

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testTimeFields_haveCorrectNamesAndAreSupported() {
        final ISOChronology iso = ISOChronology.getInstance();

        assertTimeField(iso.halfdayOfDay(), "halfdayOfDay");
        assertTimeField(iso.clockhourOfHalfday(), "clockhourOfHalfday");
        assertTimeField(iso.hourOfHalfday(), "hourOfHalfday");
        assertTimeField(iso.clockhourOfDay(), "clockhourOfDay");
        assertTimeField(iso.hourOfDay(), "hourOfDay");
        assertTimeField(iso.minuteOfDay(), "minuteOfDay");
        assertTimeField(iso.minuteOfHour(), "minuteOfHour");
        assertTimeField(iso.secondOfDay(), "secondOfDay");
        assertTimeField(iso.secondOfMinute(), "secondOfMinute");
        assertTimeField(iso.millisOfDay(), "millisOfDay");
        assertTimeField(iso.millisOfSecond(), "millisOfSecond");
    }

    /**
     * Asserts that a given DateTimeField has the expected name and is supported.
     *
     * @param field the DateTimeField to check
     * @param expectedName the expected name of the field
     */
    private void assertTimeField(DateTimeField field, String expectedName) {
        assertEquals("Field name should match", expectedName, field.getName());
        assertTrue("Field should be supported", field.isSupported());
    }
}