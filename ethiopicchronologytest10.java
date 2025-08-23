package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verifies the properties of the standard time-related fields in EthiopicChronology.
 *
 * <p>This test ensures that fields like 'hourOfDay', 'minuteOfHour', etc., are correctly
 * configured, supported, and have their expected programmatic names. These fields are
 * fundamental to the chronology's time-keeping functionality.
 */
public class EthiopicChronologyTimeFieldsTest {

    // The test focuses on the chronology's internal structure, so a specific time zone
    // like UTC is sufficient and ensures test consistency across different environments.
    private static final Chronology ETHIOPIC_CHRONOLOGY_UTC = EthiopicChronology.getInstanceUTC();

    @Test
    public void testTimeFields_areSupportedAndNamedCorrectly() {
        // This test confirms that EthiopicChronology supports the standard time fields
        // and that they are correctly named, which is a basic sanity check.
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.halfdayOfDay(), "halfdayOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.clockhourOfHalfday(), "clockhourOfHalfday");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.hourOfHalfday(), "hourOfHalfday");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.clockhourOfDay(), "clockhourOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.hourOfDay(), "hourOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.minuteOfDay(), "minuteOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.minuteOfHour(), "minuteOfHour");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.secondOfDay(), "secondOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.secondOfMinute(), "secondOfMinute");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.millisOfDay(), "millisOfDay");
        assertFieldIsSupportedAndHasName(ETHIOPIC_CHRONOLOGY_UTC.millisOfSecond(), "millisOfSecond");
    }

    /**
     * Helper assertion that checks if a DateTimeField is supported and has the expected name.
     *
     * @param field        the DateTimeField to check
     * @param expectedName the name the field is expected to have
     */
    private void assertFieldIsSupportedAndHasName(DateTimeField field, String expectedName) {
        assertTrue("Field '" + expectedName + "' should be supported", field.isSupported());
        assertEquals("Field '" + expectedName + "' has an incorrect name", expectedName, field.getName());
    }
}