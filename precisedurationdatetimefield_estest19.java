package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the remainder() method in {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldRemainderTest {

    /**
     * Tests that the remainder is always zero when the field's unit duration is one millisecond.
     * The remainder is calculated as `instant % unitMillis`, which will always be zero if unitMillis is 1.
     */
    @Test
    public void remainder_whenUnitIsOneMillisecond_shouldReturnZero() {
        // Arrange
        // The remainder() method's behavior depends solely on the unit duration field.
        // We create a field with a unit of 1 millisecond to test this specific case.
        DurationField oneMillisecondUnitField = MillisDurationField.INSTANCE;

        // The range field is required by the PreciseDateTimeField constructor but is not used
        // by the remainder() method, so we can use a placeholder.
        DurationField unsupportedRangeField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());

        // PreciseDateTimeField is a concrete implementation of the abstract PreciseDurationDateTimeField.
        PreciseDateTimeField fieldWithOneMillisUnit = new PreciseDateTimeField(
                DateTimeFieldType.millisOfSecond(), // A semantically appropriate type
                oneMillisecondUnitField,
                unsupportedRangeField
        );

        long instant = -1142L;

        // Act
        long actualRemainder = fieldWithOneMillisUnit.remainder(instant);

        // Assert
        // For a unit of 1 millisecond, any instant will be perfectly divisible,
        // resulting in a remainder of 0.
        assertEquals(0L, actualRemainder);
    }
}