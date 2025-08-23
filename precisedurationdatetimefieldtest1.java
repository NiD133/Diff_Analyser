package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
class PreciseDurationDateTimeFieldTest {

    // A concrete implementation of the abstract class under test.
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        TestPreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // The following methods are abstract and must be implemented, but are not
        // relevant for the constructor tests. They return dummy values.
        @Override
        public int get(long instant) {
            return 0;
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
    }

    // A minimal, valid DurationField for successful construction.
    private static class StubPreciseDurationField extends BaseDurationField {
        StubPreciseDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60L;
        }

        // Unused dummy implementations
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    // A DurationField stub that is explicitly imprecise.
    private static class ImpreciseDurationField extends StubPreciseDurationField {
        @Override
        public boolean isPrecise() {
            return false;
        }
    }

    // A DurationField stub that has a unit millis of zero.
    private static class ZeroMillisDurationField extends StubPreciseDurationField {
        @Override
        public long getUnitMillis() {
            return 0L;
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTest {

        @Test
        void shouldConstructSuccessfullyWithValidParameters() {
            // Arrange
            DurationField unitField = new StubPreciseDurationField();
            DateTimeFieldType fieldType = DateTimeFieldType.secondOfMinute();

            // Act
            PreciseDurationDateTimeField field = new TestPreciseDurationDateTimeField(fieldType, unitField);

            // Assert
            assertNotNull(field);
            assertEquals(fieldType, field.getType());
            assertEquals(unitField, field.getDurationField());
            assertEquals(60L, field.getUnitMillis());
        }

        @Test
        void shouldThrowExceptionWhenTypeIsNull() {
            // The superclass (BaseDateTimeField) is responsible for this check.
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new TestPreciseDurationDateTimeField(null, new StubPreciseDurationField())
            );
            assertEquals("The type must not be null", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenUnitFieldIsNull() {
            // A NullPointerException is thrown when the constructor calls unit.isPrecise()
            assertThrows(NullPointerException.class, () ->
                    new TestPreciseDurationDateTimeField(DateTimeFieldType.secondOfMinute(), null)
            );
        }

        @Test
        void shouldThrowExceptionWhenUnitFieldIsImprecise() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new TestPreciseDurationDateTimeField(DateTimeFieldType.minuteOfHour(), new ImpreciseDurationField())
            );
            assertEquals("Unit duration field must be precise", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenUnitMillisIsZero() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new TestPreciseDurationDateTimeField(DateTimeFieldType.minuteOfHour(), new ZeroMillisDurationField())
            );
            assertEquals("The unit milliseconds must be at least 1", exception.getMessage());
        }
    }
}