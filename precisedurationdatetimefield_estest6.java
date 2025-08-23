package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link PreciseDurationDateTimeField} class.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that setting the field's value to zero on a given instant effectively
     * subtracts the original value of the field from that instant.
     *
     * <p>The {@code set} method is implemented as:
     * {@code instant + (newValue - get(instant)) * unitMillis}
     *
     * <p>In this test, {@code newValue} is 0, so this simplifies to:
     * {@code instant - get(instant) * unitMillis}
     * which is the definition of rounding down to the beginning of the range.
     *
     * <p>We construct a field representing "millisecond of day".
     * The initial instant is 2979ms. The {@code get()} value is therefore 2979.
     * Setting the value to 0 should result in a new instant of 0ms (the epoch).
     */
    @Test
    public void set_toZero_resetsTheFieldValue() {
        // Arrange
        // Create a field that behaves like "millisecond of day" to test the set logic.
        // The unit is milliseconds, and the range is one day.
        DateTimeFieldType fieldType = DateTimeFieldType.millisOfDay();
        DurationField millisecondUnit = MillisDurationField.INSTANCE;
        DurationField dayRange = GJChronology.getInstanceUTC().days();
        PreciseDateTimeField millisecondOfDayField = new PreciseDateTimeField(fieldType, millisecondUnit, dayRange);

        long initialInstant = 2979L; // 2979 milliseconds past the epoch
        int valueToSet = 0;

        // Act
        long resultInstant = millisecondOfDayField.set(initialInstant, valueToSet);

        // Assert
        long expectedInstant = 0L; // The start of the day (which is the epoch)
        assertEquals(expectedInstant, resultInstant);
    }
}