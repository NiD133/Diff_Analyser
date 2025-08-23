package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 *
 * <p>This test class focuses on the behavior of the abstract PreciseDurationDateTimeField
 * by using mock implementations to provide concrete behavior for testing.
 */
class PreciseDurationDateTimeFieldTest {

    @Test
    void isSupported_shouldAlwaysReturnTrue() {
        // Arrange
        // The isSupported() method is fundamental and should always be true for this field type.
        BaseDateTimeField field = new TestPreciseDurationDateTimeField();

        // Act
        boolean isSupported = field.isSupported();

        // Assert
        assertTrue(isSupported, "isSupported() should consistently return true");
    }

    // =================================================================
    // MOCK IMPLEMENTATIONS FOR TESTING
    // =================================================================

    /**
     * A concrete, minimal implementation of PreciseDurationDateTimeField for testing its abstract methods.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        TestPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        TestPreciseDurationDateTimeField(DateTimeFieldType type, DurationField durationField) {
            super(type, durationField);
        }

        @Override
        public int get(long instant) {
            // A simple implementation for testing purposes.
            return (int) (instant / 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock that uses standard ISO chronology fields for duration and range.
     */
    private static class MockStandardBaseDateTimeField extends TestPreciseDurationDateTimeField {
        MockStandardBaseDateTimeField() {
            super();
        }

        @Override
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    /**
     * A mock DurationField that counts method invocations.
     * <p>
     * Note: The counters are instance fields to ensure test isolation, preventing side effects
     * between different test cases.
     */
    private static class MockCountingDurationField extends BaseDurationField {
        int addIntCalls = 0;
        int addLongCalls = 0;
        int getDifferenceAsLongCalls = 0;

        MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60;
        }

        @Override
        public long add(long instant, int value) {
            addIntCalls++;
            return instant + (value * 60L);
        }

        @Override
        public long add(long instant, long value) {
            addLongCalls++;
            return instant + (value * 60L);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            getDifferenceAsLongCalls++;
            return 30;
        }

        // Unused methods return default values
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }

    /**
     * A mock DurationField whose unit millis is zero, to test constructor validation.
     */
    private static class MockZeroUnitDurationField extends BaseDurationField {
        MockZeroUnitDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 0; // The key property for this mock
        }

        // Unused methods return default values
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

    /**
     * A mock DurationField that is not precise, to test constructor validation.
     */
    private static class MockImpreciseDurationField extends BaseDurationField {
        MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return false; // The key property for this mock
        }

        @Override
        public long getUnitMillis() {
            return 60;
        }

        // Unused methods return default values
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
}