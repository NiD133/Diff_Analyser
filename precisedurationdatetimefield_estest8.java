package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 * This test focuses on the behavior of the set() method.
 */
public class PreciseDurationDateTimeFieldTest {

    @Test
    public void set_withNegativeInstant_returnsCorrectlyCalculatedInstant() {
        // ARRANGE
        // The test uses a concrete PreciseDateTimeField to test the set() logic,
        // which is inherited from BaseDateTimeField. This logic depends on the get()
        // implementation in PreciseDateTimeField, especially for negative instants.

        // 1. Define the field's unit and range.
        // The unit duration is 1 millisecond.
        DurationField unitField = MillisDurationField.INSTANCE;
        // The range duration is 1 day (86,400,000 milliseconds).
        // Using a standard day field from a UTC chronology is a simple way to get this.
        DurationField rangeField = GregorianChronology.getInstanceUTC().days();

        // 2. Create the field under test.
        // The DateTimeFieldType is metadata and not used in this calculation.
        PreciseDateTimeField field = new PreciseDateTimeField(
                DateTimeFieldType.millisOfDay(), unitField, rangeField);

        // 3. Define test inputs.
        long initialInstant = -3012L;
        int valueToSet = 82;

        // ACT
        long resultInstant = field.set(initialInstant, valueToSet);

        // ASSERT
        // The expected value is calculated based on the formula in BaseDateTimeField.set():
        //   result = instant - (get(instant) - value) * unitMillis
        //
        // For a negative instant, PreciseDateTimeField.get(instant) is calculated as:
        //   get(instant) = range - 1 + (((instant + 1) / unitMillis) % range)
        //
        // Calculation steps:
        //   unitMillis = 1
        //   range = 86,400,000 / 1 = 86,400,000
        //
        //   currentValue = get(-3012L)
        //                = 86,400,000 - 1 + (((-3012 + 1) / 1) % 86,400,000)
        //                = 86,399,999 + (-3011 % 86,400,000)
        //                = 86,399,999 - 3011
        //                = 86,396,988
        //
        //   resultInstant = -3012L - (86,396,988 - 82) * 1L
        //                 = -3012L - 86,396,906L
        //                 = -86,399,918L
        long expectedInstant = -86399918L;
        assertEquals(expectedInstant, resultInstant);
    }
}