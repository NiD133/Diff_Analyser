package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class is derived from an EvoSuite-generated class.
 * The contained test has been refactored for clarity.
 */
public class PreciseDurationDateTimeField_ESTestTest3 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    /**
     * Tests that roundCeiling() for an instant that is already on a unit boundary (like the epoch 0L)
     * returns the instant itself, without rounding up.
     */
    @Test
    public void roundCeiling_whenInstantIsOnBoundary_returnsSameInstant() {
        // Arrange: Create a simple millisecond-based field.
        // The roundCeiling method is implemented in the abstract PreciseDurationDateTimeField class,
        // so we use a concrete subclass (PreciseDateTimeField) for testing.
        // The unit field (milliseconds) is the key component for this test. The range field (seconds)
        // is required by the constructor but is not used by the roundCeiling method.
        PreciseDateTimeField millisecondField = new PreciseDateTimeField(
                DateTimeFieldType.millisOfSecond(),
                MillisDurationField.INSTANCE,
                ISOChronology.getInstanceUTC().seconds());

        long epochInstant = 0L;

        // Act: Call the method under test.
        long roundedInstant = millisecondField.roundCeiling(epochInstant);

        // Assert: The value should be unchanged because 0 is a perfect multiple of the unit (1 millisecond).
        assertEquals("Rounding the epoch (0L) up to the nearest millisecond should not change it.",
                epochInstant, roundedInstant);
    }
}