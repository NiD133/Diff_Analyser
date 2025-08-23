package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    // This method is run before each test to ensure a clean state.
    @Before
    public void setUp() {
        // Reset counters to ensure tests are isolated and not affected by previous runs.
        MockCountingDurationField.reset();
    }

    @Test
    public void getType_shouldReturnTypeProvidedInConstructor() {
        // Arrange
        DateTimeFieldType expectedType = DateTimeFieldType.secondOfDay();
        DurationField mockDurationField = new MockCountingDurationField(DurationFieldType.minutes());
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(expectedType, mockDurationField);

        // Act
        DateTimeFieldType actualType = field.getType();

        // Assert
        assertEquals(expectedType, actualType);
    }

    //-----------------------------------------------------------------------
    // Mock Implementations for Testing
    //-----------------------------------------------------------------------

    private static final long MOCK_UNIT_MILLIS = 60L;

    /**
     * A concrete mock implementation of PreciseDurationDateTimeField for testing purposes.
     */
    private static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        /** A simple mock implementation that calculates the field value from the instant. */
        @Override
        public int get(long instant) {
            return (int) (instant / MOCK_UNIT_MILLIS);
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
     * A mock that uses standard, realistic duration fields from ISOChronology.
     * This mock is suitable for tests requiring integration with real field behavior.
     * Note: This mock is not used in the tests in this file but is kept for completeness,
     * as it may be used in other parts of the test suite.
     */
    private static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
        protected MockStandardBaseDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    /**
     * A mock DurationField that counts method invocations.
     * This is useful for verifying that certain methods are called (interaction testing).
     */
    private static class MockCountingDurationField extends BaseDurationField {
        // Invocation counters for test verification
        static int add_int_calls = 0;
        static int add_long_calls = 0;
        static int difference_long_calls = 0;

        /** Resets all invocation counters to zero. */
        public static void reset() {
            add_int_calls = 0;
            add_long_calls = 0;
            difference_long_calls = 0;
        }

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return MOCK_UNIT_MILLIS;
        }

        @Override
        public long add(long instant, int value) {
            add_int_calls++;
            return instant + (value * MOCK_UNIT_MILLIS);
        }

        @Override
        public long add(long instant, long value) {
            add_long_calls++;
            return instant + (value * MOCK_UNIT_MILLIS);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long_calls++;
            // Return a fixed, non-zero value for predictability in tests
            return 30;
        }
        
        // Unimplemented methods return zero, which is acceptable for this mock's purpose.
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
    }

    /**
     * A mock DurationField where the unit millis is zero.
     * Used to test constructor validation in the class under test.
     */
    private static class MockZeroDurationField extends BaseDurationField {
        protected MockZeroDurationField(DurationFieldType type) { super(type); }
        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 0; } // The key feature of this mock
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
        @Override public long add(long instant, int value) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    /**
     * A mock DurationField that is not precise.
     * Used to test constructor validation in the class under test.
     */
    private static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) { super(type); }
        @Override public boolean isPrecise() { return false; } // The key feature of this mock
        @Override public long getUnitMillis() { return 0; }
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
        @Override public long add(long instant, int value) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }
}