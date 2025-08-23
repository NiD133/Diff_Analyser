package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.Seconds;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for PreciseDurationDateTimeField.
 * Note: The original test code was auto-generated and has been refactored for clarity.
 */
public class PreciseDurationDateTimeFieldTest {

    // PreciseDateTimeField is a test-specific concrete implementation of the abstract
    // PreciseDurationDateTimeField class, used by the original test suite.
    // We assume it has a constructor: PreciseDateTimeField(DateTimeFieldType, DurationField, DurationField)

    @Test
    public void roundFloor_withOneMilliUnit_shouldReturnSameInstant() {
        // Arrange
        // The key property for this test is a unit duration of 1 millisecond,
        // which provides the finest possible precision.
        DurationField oneMilliUnitField = MillisDurationField.INSTANCE;

        // The other constructor arguments (field type and range field) are not relevant
        // for the roundFloor logic but are required to create the object.
        // We use sensible, related types for better readability.
        DateTimeFieldType fieldType = DateTimeFieldType.millisOfSecond();
        DurationField rangeField = Seconds.seconds().getDurationField();

        PreciseDateTimeField millisecondField = new PreciseDateTimeField(fieldType, oneMilliUnitField, rangeField);

        long instant = 0L;

        // Act
        // Rounding an instant to the floor with a 1-millisecond precision field
        // should not change the value, as any instant is already on a millisecond boundary.
        long result = millisecondField.roundFloor(instant);

        // Assert
        assertEquals("Rounding to the floor with 1ms precision should be a no-op", instant, result);
    }
}