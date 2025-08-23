package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreciseDurationDateTimeField_ESTestTest10 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    /**
     * Tests that roundFloor() does not change the instant when the field's
     * unit is one millisecond, as any instant is already on a valid "floor".
     */
    @Test
    public void roundFloor_whenUnitIsOneMillisecond_returnsSameInstant() {
        // Arrange
        // The class under test is PreciseDateTimeField, a concrete implementation of
        // the abstract PreciseDurationDateTimeField.
        // We create a field with a unit duration of 1 millisecond.
        DateTimeFieldType fieldType = DateTimeFieldType.millisOfSecond();
        DurationField unitMillisField = MillisDurationField.INSTANCE; // Unit is 1 millisecond

        // The range field is required by the constructor but not used by roundFloor.
        // We can use any precise field, like seconds from a standard chronology.
        DurationField rangeField = ISOChronology.getInstanceUTC().seconds();

        PreciseDateTimeField millisOfSecondField = new PreciseDateTimeField(fieldType, unitMillisField, rangeField);

        long instant = -5225L;

        // Act
        long roundedInstant = millisOfSecondField.roundFloor(instant);

        // Assert
        assertEquals("For a 1ms unit field, roundFloor should not alter the value.", instant, roundedInstant);
    }
}