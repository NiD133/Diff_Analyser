package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the time-related DateTimeFields in IslamicChronology.
 * This test verifies that standard time fields are supported and have the correct names.
 */
public class IslamicChronologyTimeFieldsTest {

    private Chronology islamicChronology;

    @Before
    public void setUp() {
        // Use the default instance of IslamicChronology for testing.
        islamicChronology = IslamicChronology.getInstance();
    }

    @Test
    public void testTimeFieldsAreSupportedAndHaveCorrectNames() {
        assertField(islamicChronology.halfdayOfDay(), "halfdayOfDay");
        assertField(islamicChronology.clockhourOfHalfday(), "clockhourOfHalfday");
        assertField(islamicChronology.hourOfHalfday(), "hourOfHalfday");
        assertField(islamicChronology.clockhourOfDay(), "clockhourOfDay");
        assertField(islamicChronology.hourOfDay(), "hourOfDay");
        assertField(islamicChronology.minuteOfDay(), "minuteOfDay");
        assertField(islamicChronology.minuteOfHour(), "minuteOfHour");
        assertField(islamicChronology.secondOfDay(), "secondOfDay");
        assertField(islamicChronology.secondOfMinute(), "secondOfMinute");
        assertField(islamicChronology.millisOfDay(), "millisOfDay");
        assertField(islamicChronology.millisOfSecond(), "millisOfSecond");
    }

    /**
     * Helper method to assert that a DateTimeField is supported and has the expected name.
     * This reduces code duplication and improves the readability of the main test method.
     *
     * @param field        the DateTimeField to check
     * @param expectedName the expected name of the field
     */
    private void assertField(DateTimeField field, String expectedName) {
        assertEquals("Field name should be correct.", expectedName, field.getName());
        assertTrue("Field '" + expectedName + "' should be supported.", field.isSupported());
    }
}