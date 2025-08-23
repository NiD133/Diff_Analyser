package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link PreciseDurationDateTimeField} class.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that roundCeiling() has no effect when the field's unit
     * is a single millisecond, as any instant is already at a "ceiling"
     * for this granular unit.
     */
    @Test
    public void roundCeiling_whenUnitIsOneMillisecond_returnsSameInstant() {
        // Arrange
        // The roundCeiling() method's behavior in PreciseDurationDateTimeField
        // depends only on the unit field. We use a concrete implementation,
        // PreciseDateTimeField, to test this behavior. The other constructor
        // arguments (type and range field) are not relevant to this test,
        // so we provide simple, valid values.
        DurationField millisecondUnit = MillisDurationField.INSTANCE;
        PreciseDateTimeField millisecondField = new PreciseDateTimeField(
                DateTimeFieldType.millisOfSecond(), // A semantically related type
                millisecondUnit,
                millisecondUnit // A simple, valid range field
        );

        long anInstantInMillis = 100_000_000_000_000L;

        // Act
        long roundedInstant = millisecondField.roundCeiling(anInstantInMillis);

        // Assert
        assertEquals(
                "Rounding an instant to the ceiling of a 1-millisecond unit should not change its value.",
                anInstantInMillis,
                roundedInstant
        );
    }
}