package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the abstract {@link PreciseDurationDateTimeField} class.
 * <p>
 * This test suite uses mock implementations to test the concrete functionality
 * provided by the abstract class under test.
 */
public class PreciseDurationDateTimeFieldTest {

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    /**
     * Tests that getMinimumValue() consistently returns 0.
     * This behavior is inherited from the superclass {@link BaseDateTimeField}.
     */
    @Test
    public void getMinimumValue_shouldReturnZero() {
        // Arrange: Create a concrete instance of the abstract class under test.
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();

        // Act: Call the method under test.
        int minimumValue = field.getMinimumValue();

        // Assert: Verify that the minimum value is 0.
        assertEquals(0, minimumValue);
    }


    //-----------------------------------------------------------------------
    // Mock Implementations for Testing
    //-----------------------------------------------------------------------

    /**
     * A minimal concrete implementation of PreciseDurationDateTimeField for testing.
     * It is designed to simulate a "second-of-minute" field.
     */
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        /** Default constructor, creates a field of type "secondOfMinute". */
        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        /** Constructor allowing custom type and duration field. */
        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        /** Mocked implementation. */
        @Override
        public int get(long instant) {
            return (int) (instant / 60L);
        }

        /** Mocked implementation. */
        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        /** Mocked implementation, consistent with a "second-of-minute" field. */
        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock that uses standard ISOChronology fields for its duration and range.
     * Likely used in other tests to verify interaction with real field implementations.
     */
    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
        protected MockStandardBaseDateTimeField() {
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
     * Note: The use of static counters can be problematic for parallel test execution.
     */
    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 60; }
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }

        @Override
        public long add(long instant, int value) {
            add_int++;
            return instant + (value * 60L);
        }

        @Override
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * 60L);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30;
        }
    }

    /**
     * A mock DurationField with a unit millis of zero.
     * Used to test constructor validation in PreciseDurationDateTimeField.
     */
    static class MockZeroDurationField extends BaseDurationField {
        protected MockZeroDurationField(DurationFieldType type) { super(type); }
        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 0; /* Key property for this mock */ }
        @Override public long getValueAsLong(long d, long i) { return 0; }
        @Override public long getMillis(int v, long i) { return 0; }
        @Override public long getMillis(long v, long i) { return 0; }
        @Override public long add(long i, int v) { return 0; }
        @Override public long add(long i, long v) { return 0; }
        @Override public long getDifferenceAsLong(long min, long sub) { return 0; }
    }

    /**
     * An imprecise mock DurationField.
     * Used to test constructor validation in PreciseDurationDateTimeField.
     */
    static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) { super(type); }
        @Override public boolean isPrecise() { return false; /* Key property for this mock */ }
        @Override public long getUnitMillis() { return 0; }
        @Override public long getValueAsLong(long d, long i) { return 0; }
        @Override public long getMillis(int v, long i) { return 0; }
        @Override public long getMillis(long v, long i) { return 0; }
        @Override public long add(long i, int v) { return 0; }
        @Override public long add(long i, long v) { return 0; }
        @Override public long getDifferenceAsLong(long min, long sub) { return 0; }
    }
}