package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the abstract class {@link PreciseDurationDateTimeField}.
 * This test uses {@link PreciseDateTimeField}, a concrete implementation, to test the shared logic.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that the set() method throws an IllegalArgumentException when the provided
     * value is greater than the field's maximum allowed value.
     */
    @Test
    public void set_whenValueIsGreaterThanMaximum_throwsIllegalArgumentException() {
        // Arrange
        // 1. Define a custom field. The DateTimeFieldType is just illustrative for the exception message.
        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfDay();

        // 2. Set the field's unit of measurement. We'll use 'weeks'.
        DurationField weeksUnitField = GJChronology.getInstance().weeks();

        // 3. Define the total range of the field. We create a scaled duration field
        //    that represents a total range of 1058 weeks.
        int rangeDurationInWeeks = 1058;
        DurationField rangeField = new ScaledDurationField(
                weeksUnitField,
                DurationFieldType.weeks(),
                rangeDurationInWeeks
        );

        // 4. Instantiate the field. Its maximum value is calculated as:
        //    (range duration / unit duration) - 1
        //    = (1058 weeks / 1 week) - 1 = 1057.
        //    Therefore, the valid value range for this field is [0, 1057].
        PreciseDateTimeField customField = new PreciseDateTimeField(fieldType, weeksUnitField, rangeField);

        int valueToSet = 1058; // This value is deliberately out of bounds.
        long anyInstant = 12345L; // The specific instant does not affect this field's range.

        // Act & Assert
        try {
            customField.set(anyInstant, valueToSet);
            fail("Expected an IllegalArgumentException because the value is out of the valid range.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid value and the valid range.
            String expectedMessage = "Value 1058 for minuteOfDay must be in the range [0,1057]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}