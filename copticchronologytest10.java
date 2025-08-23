package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import org.joda.time.DateTimeField;
import org.junit.Test;

/**
 * Unit tests for the time-related DateTimeFields in CopticChronology.
 * This test verifies that all standard time fields are supported and have the correct names.
 */
public class CopticChronologyTimeFieldsTest {

    @Test
    public void timeFields_areSupportedAndHaveCorrectNames() {
        // Arrange: Get a UTC instance of the chronology to ensure test consistency.
        CopticChronology chronology = CopticChronology.getInstanceUTC();

        // A map of expected field names to the actual DateTimeField objects.
        // Using a map makes the test data-driven, concise, and easy to extend.
        Map<String, DateTimeField> fieldsToTest = new LinkedHashMap<>();
        fieldsToTest.put("halfdayOfDay", chronology.halfdayOfDay());
        fieldsToTest.put("clockhourOfHalfday", chronology.clockhourOfHalfday());
        fieldsToTest.put("hourOfHalfday", chronology.hourOfHalfday());
        fieldsToTest.put("clockhourOfDay", chronology.clockhourOfDay());
        fieldsToTest.put("hourOfDay", chronology.hourOfDay());
        fieldsToTest.put("minuteOfDay", chronology.minuteOfDay());
        fieldsToTest.put("minuteOfHour", chronology.minuteOfHour());
        fieldsToTest.put("secondOfDay", chronology.secondOfDay());
        fieldsToTest.put("secondOfMinute", chronology.secondOfMinute());
        fieldsToTest.put("millisOfDay", chronology.millisOfDay());
        fieldsToTest.put("millisOfSecond", chronology.millisOfSecond());

        // Act & Assert: Iterate through the map and verify each field.
        for (Map.Entry<String, DateTimeField> entry : fieldsToTest.entrySet()) {
            String expectedName = entry.getKey();
            DateTimeField field = entry.getValue();

            // Assert that the field is supported
            assertTrue("Field '" + expectedName + "' should be supported.", field.isSupported());

            // Assert that the field has the correct name
            assertEquals("Field '" + expectedName + "' has an incorrect name.", expectedName, field.getName());
        }
    }
}