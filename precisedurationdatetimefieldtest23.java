package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Tests for {@link PreciseDurationDateTimeField}.
 * This class focuses on the delegation behavior of the field.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A test-specific spy for the DurationField. It allows us to verify that
     * methods are called and to control their return values, without relying on
     * static state.
     */
    private static class SpyDurationField extends BaseDurationField {
        private int differenceAsLongCallCount = 0;
        private final long differenceToReturn;

        SpyDurationField(long differenceToReturn) {
            super(DurationFieldType.seconds());
            this.differenceToReturn = differenceToReturn;
        }

        /**
         * The method we are spying on. It increments a counter and returns a preset value.
         */
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            differenceAsLongCallCount++;
            return differenceToReturn;
        }

        public int getDifferenceAsLongCallCount() {
            return differenceAsLongCallCount;
        }

        // Methods required by the SUT's constructor and the abstract parent class.
        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Must be > 0 for the PreciseDurationDateTimeField constructor.
            return 1000L;
        }

        // Dummy implementations for other abstract methods.
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
    }

    /**
     * A concrete implementation of PreciseDurationDateTimeField for testing,
     * allowing it to be instantiated with a specific DurationField.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected TestPreciseDurationDateTimeField(DurationField unit) {
            super(DateTimeFieldType.secondOfMinute(), unit);
        }

        // Provide dummy implementations for abstract methods not relevant to this test.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            // Not used in the test for getDifference, so can return null.
            return null;
        }
    }

    //-----------------------------------------------------------------------
    @Test
    public void getDifference_shouldDelegateToUnderlyingDurationField() {
        // Arrange
        final long expectedDifference = 30L;
        SpyDurationField spyDurationField = new SpyDurationField(expectedDifference);

        // The class under test, configured with our spy duration field.
        PreciseDurationDateTimeField field = new TestPreciseDurationDateTimeField(spyDurationField);

        // Act
        long actualDifference = field.getDifference(120_000L, 60_000L);

        // Assert
        // 1. Verify the result is the one returned by our spy.
        assertEquals(expectedDifference, actualDifference);

        // 2. Verify that the spy's method was called exactly once.
        assertEquals(1, spyDurationField.getDifferenceAsLongCallCount());
    }
}