package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains the improved test case.
 * The original test was part of a larger, auto-generated suite.
 */
public class PreciseDurationDateTimeField_ESTestTest16 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    /**
     * Tests that getMaximumValueForSet returns the correct maximum value,
     * which is determined by the field's range duration, not its input parameters.
     * The maximum value for a field is the total number of its units that fit
     * into its range, minus one (because the minimum value is 0).
     */
    @Test
    public void getMaximumValueForSet_shouldReturnMaximumValueBasedOnRangeDuration() {
        // Arrange
        // 1. Define a "millisecond of day" field for the test.
        // This makes the test's purpose concrete and easy to reason about.
        DateTimeFieldType milliOfDayType = DateTimeFieldType.millisOfDay();

        // 2. The field's unit is one millisecond.
        DurationField unitField = MillisDurationField.INSTANCE;

        // 3. The field's range is one day. Using a standard UTC chronology is simpler
        // and clearer than the original test's ZonedJulianChronology.
        DurationField rangeField = GregorianChronology.getInstanceUTC().days();

        // 4. Instantiate the field under test.
        // Note: The original test used `PreciseDateTimeField`, a non-public concrete
        // implementation of the abstract PreciseDurationDateTimeField.
        PreciseDateTimeField milliOfDayField = new PreciseDateTimeField(milliOfDayType, unitField, rangeField);

        // 5. Calculate the expected maximum value. For "millisecond of day", this is
        // the number of milliseconds in a day minus one.
        int expectedMaximumValue = DateTimeConstants.MILLIS_PER_DAY - 1;

        // The implementation of getMaximumValueForSet in this class does not use its
        // parameters. We use 0L and 0 to signify that their specific values are not important.
        long arbitraryInstant = 0L;
        int arbitraryValue = 0;

        // Act
        int actualMaximumValue = milliOfDayField.getMaximumValueForSet(arbitraryInstant, arbitraryValue);

        // Assert
        assertEquals("Maximum value should be the number of milliseconds in a day minus one",
                expectedMaximumValue, actualMaximumValue);
    }
}