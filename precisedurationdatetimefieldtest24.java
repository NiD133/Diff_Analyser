package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 * This class focuses on verifying the behavior inherited from BaseDateTimeField,
 * specifically the delegation of calls to the underlying DurationField.
 */
public class PreciseDurationDateTimeFieldTest {

    //
    // Test for getDifferenceAsLong(long, long)
    //

    @Test
    public void getDifferenceAsLong_shouldDelegateToUnderlyingDurationField() {
        // Arrange
        // Create a mock DurationField that counts calls to its methods.
        final CountingDurationField mockDurationField = new CountingDurationField(DurationFieldType.seconds());

        // The class under test is abstract, so we use a concrete test implementation.
        // We inject our mock DurationField to verify interactions.
        final BaseDateTimeField field = new TestPreciseDurationDateTimeField(
                DateTimeFieldType.secondOfMinute(), mockDurationField);

        final long expectedDifference = 30L; // This value is hardcoded in our mock.

        // Act
        final long actualDifference = field.getDifferenceAsLong(1000L, 0L);

        // Assert
        // 1. Verify that the method returned the expected value from the mock.
        assertEquals("The returned value should be the one from the underlying duration field",
                expectedDifference, actualDifference);

        // 2. Verify that the call was correctly delegated to the duration field.
        assertEquals("The duration field's getDifferenceAsLong should be called exactly once",
                1, mockDurationField.getGetDifferenceAsLongCallCount());
    }

    //
    // Test Helper Mocks
    //

    /**
     * A concrete implementation of the abstract PreciseDurationDateTimeField for testing.
     * It allows us to instantiate the class under test with a specific DurationField.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected TestPreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // Provide minimal implementations for abstract methods not relevant to this test.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock DurationField that counts method invocations to allow for interaction testing.
     * Unlike the original, this mock uses instance fields for its counters to ensure
     * test isolation.
     */
    private static class CountingDurationField extends BaseDurationField {
        private int getDifferenceAsLongCallCount = 0;

        protected CountingDurationField(DurationFieldType type) {
            super(type);
        }

        /**
         * This is the method we want to track. It increments a counter and returns a fixed value.
         */
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            getDifferenceAsLongCallCount++;
            return 30L; // Return a predictable, non-zero value for the test.
        }

        public int getGetDifferenceAsLongCallCount() {
            return getDifferenceAsLongCallCount;
        }

        // --- Unused mock methods returning default values ---

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60L; // A realistic value from the original mock.
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            return instant;
        }

        @Override
        public long add(long instant, long value) {
            return instant;
        }
    }
}