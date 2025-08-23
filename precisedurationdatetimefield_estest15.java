package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the PreciseDurationDateTimeField class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class PreciseDurationDateTimeField_ESTestTest15 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    /**
     * This test verifies that getMinimumValue() returns 0. This value is inherited
     * from the parent BaseDateTimeField and is expected to be constant.
     */
    @Test
    public void getMinimumValue_shouldReturnZero() {
        // Arrange
        // To test the inherited getMinimumValue() method, we need an instance of a concrete
        // subclass of PreciseDurationDateTimeField. We use PreciseDateTimeField for this.
        // Its constructor requires a type, a unit field, and a range field.
        DateTimeFieldType fieldType = DateTimeFieldType.halfdayOfDay();

        // Create a "unit" duration field representing half-days.
        final long halfDayMillis = MILLIS_PER_DAY / 2;
        DurationField unitField = new PreciseDurationField(DurationFieldType.halfdays(), halfDayMillis);

        // Create a "range" duration field representing a full day.
        DurationField rangeField = new PreciseDurationField(DurationFieldType.days(), MILLIS_PER_DAY);

        // Instantiate the field. The specific fields chosen do not affect the outcome
        // of getMinimumValue(), but are required for object construction.
        PreciseDateTimeField halfdayField = new PreciseDateTimeField(fieldType, unitField, rangeField);

        // Act
        int actualMinimumValue = halfdayField.getMinimumValue();

        // Assert
        int expectedMinimumValue = 0;
        assertEquals("The minimum value for the field should be 0.", expectedMinimumValue, actualMinimumValue);
    }
}