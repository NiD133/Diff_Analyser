package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DurationField;
import org.junit.Test;

/**
 * This test class verifies the fundamental properties of the date-related fields
 * in ISOChronology. It checks each field's name, support status, duration field,
 * and range duration field to ensure they are configured correctly.
 */
public class ISOChronologyDateFieldPropertiesTest {

    private static final ISOChronology CHRONOLOGY_INSTANCE = ISOChronology.getInstanceUTC();

    /**
     * A helper method to assert the core properties of a DateTimeField.
     * This reduces repetition and makes the test's intent clearer.
     *
     * @param field The DateTimeField to test.
     * @param expectedName The expected name of the field.
     * @param expectedDurationField The expected duration field.
     * @param expectedRangeDurationField The expected range duration field.
     */
    private void assertFieldProperties(
            DateTimeField field,
            String expectedName,
            DurationField expectedDurationField,
            DurationField expectedRangeDurationField) {

        // Check that the field has the correct name
        assertEquals(expectedName, field.getName());

        // All date fields in ISOChronology are expected to be supported
        assertTrue("Field '" + expectedName + "' should be supported", field.isSupported());

        // Check that the field's duration is correctly linked
        assertEquals("Mismatched duration field for '" + expectedName + "'",
                expectedDurationField, field.getDurationField());

        // Check that the field's range duration is correctly linked
        if (expectedRangeDurationField == null) {
            assertNull("Expected null range duration field for '" + expectedName + "'", field.getRangeDurationField());
        } else {
            assertEquals("Mismatched range duration field for '" + expectedName + "'",
                    expectedRangeDurationField, field.getRangeDurationField());
        }
    }

    @Test
    public void testDateFieldProperties() {
        // Era and Century fields
        assertFieldProperties(CHRONOLOGY_INSTANCE.era(), "era", CHRONOLOGY_INSTANCE.eras(), null);
        assertFieldProperties(CHRONOLOGY_INSTANCE.centuryOfEra(), "centuryOfEra", CHRONOLOGY_INSTANCE.centuries(), CHRONOLOGY_INSTANCE.eras());

        // Year-based fields
        assertFieldProperties(CHRONOLOGY_INSTANCE.year(), "year", CHRONOLOGY_INSTANCE.years(), null);
        assertFieldProperties(CHRONOLOGY_INSTANCE.yearOfEra(), "yearOfEra", CHRONOLOGY_INSTANCE.years(), CHRONOLOGY_INSTANCE.eras());
        assertFieldProperties(CHRONOLOGY_INSTANCE.yearOfCentury(), "yearOfCentury", CHRONOLOGY_INSTANCE.years(), CHRONOLOGY_INSTANCE.centuries());

        // Weekyear-based fields
        assertFieldProperties(CHRONOLOGY_INSTANCE.weekyear(), "weekyear", CHRONOLOGY_INSTANCE.weekyears(), null);
        assertFieldProperties(CHRONOLOGY_INSTANCE.weekyearOfCentury(), "weekyearOfCentury", CHRONOLOGY_INSTANCE.weekyears(), CHRONOLOGY_INSTANCE.centuries());
        assertFieldProperties(CHRONOLOGY_INSTANCE.weekOfWeekyear(), "weekOfWeekyear", CHRONOLOGY_INSTANCE.weeks(), CHRONOLOGY_INSTANCE.weekyears());

        // Month and Day fields
        assertFieldProperties(CHRONOLOGY_INSTANCE.monthOfYear(), "monthOfYear", CHRONOLOGY_INSTANCE.months(), CHRONOLOGY_INSTANCE.years());
        assertFieldProperties(CHRONOLOGY_INSTANCE.dayOfYear(), "dayOfYear", CHRONOLOGY_INSTANCE.days(), CHRONOLOGY_INSTANCE.years());
        assertFieldProperties(CHRONOLOGY_INSTANCE.dayOfMonth(), "dayOfMonth", CHRONOLOGY_INSTANCE.days(), CHRONOLOGY_INSTANCE.months());
        assertFieldProperties(CHRONOLOGY_INSTANCE.dayOfWeek(), "dayOfWeek", CHRONOLOGY_INSTANCE.days(), CHRONOLOGY_INSTANCE.weeks());
    }
}