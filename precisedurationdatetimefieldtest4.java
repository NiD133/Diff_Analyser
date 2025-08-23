package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Unit tests for the abstract PreciseDurationDateTimeField class.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A concrete implementation of the abstract class under test.
     * <p>
     * This is necessary because {@link PreciseDurationDateTimeField} is abstract and cannot be
     * instantiated directly. The methods are implemented to satisfy the compiler, but their
     * internal logic is irrelevant for the test, as they are not called.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        TestablePreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // The following methods are stubs to create a concrete class for testing.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public long set(long instant, int value) {
            return 0L;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }

        @Override
        public int getMaximumValue() {
            return 0;
        }

        @Override
        public long roundFloor(long instant) {
            return 0L;
        }
    }

    @Test
    public void toString_shouldReturnFormattedName() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfDay();
        
        // Use a real, precise duration field from Joda-Time to satisfy the constructor's requirements.
        // This is clearer than using a custom mock duration field.
        DurationField preciseUnitField = ISOChronology.getInstanceUTC().seconds();

        BaseDateTimeField field = new TestablePreciseDurationDateTimeField(fieldType, preciseUnitField);
        String expectedString = "DateTimeField[secondOfDay]";

        // Act
        String actualString = field.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}