package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the behavior of PreciseDurationDateTimeField.
 * Note: The original test was auto-generated. This version has been refactored for clarity.
 */
public class PreciseDurationDateTimeField_ESTestTest2 {

    /**
     * Tests that roundCeiling() correctly rounds a positive instant up to the
     * start of the next unit boundary.
     */
    @Test
    public void roundCeiling_forPositiveInstant_roundsUpToNextUnit() {
        // Arrange
        // An instant shortly after the epoch (1970-01-01T00:00:00.000Z).
        long instantMillis = 1902L;

        // The unit of our field is a standard day. The rounding logic depends on this unit.
        DurationField daysUnitField = ISOChronology.getInstanceUTC().days();

        // The roundCeiling method is in PreciseDurationDateTimeField, which is abstract.
        // We use a concrete subclass, PreciseDateTimeField, for testing.
        // The specific DateTimeFieldType and the overall duration field are not relevant for
        // the rounding logic, so we provide simple, standard values.
        PreciseDateTimeField dayBasedField = new PreciseDateTimeField(
                DateTimeFieldType.dayOfMonth(), // Arbitrary type to satisfy constructor
                daysUnitField,                  // The unit field that defines the rounding boundary
                daysUnitField                   // Arbitrary duration field to satisfy constructor
        );

        // The ceiling of any time on the first day (e.g., 1902ms after the epoch)
        // should be the start of the second day.
        long expectedRoundedMillis = DateTimeConstants.MILLIS_PER_DAY;

        // Act
        long actualRoundedMillis = dayBasedField.roundCeiling(instantMillis);

        // Assert
        assertEquals(expectedRoundedMillis, actualRoundedMillis);
    }
}