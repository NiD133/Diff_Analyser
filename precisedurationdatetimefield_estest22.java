package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that the constructor throws an IllegalArgumentException if the provided
     * unit duration field has a unit millisecond value less than 1.
     */
    @Test
    public void constructor_shouldThrowException_whenUnitMillisIsZero() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfDay();

        // An UnsupportedDurationField is used here because its getUnitMillis() method
        // returns 0, which is an invalid value for the constructor.
        DurationField unitFieldWithZeroMillis = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        
        // The range field is not relevant for this test, so we can reuse the same field.
        DurationField dummyRangeField = unitFieldWithZeroMillis;

        // Act & Assert
        try {
            // We instantiate a concrete subclass (PreciseDateTimeField) to test the 
            // validation logic in the abstract PreciseDurationDateTimeField's constructor.
            new PreciseDateTimeField(fieldType, unitFieldWithZeroMillis, dummyRangeField);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            assertEquals("The unit milliseconds must be at least 1", e.getMessage());
        }
    }
}