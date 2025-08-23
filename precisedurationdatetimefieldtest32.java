package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A concrete implementation of PreciseDurationDateTimeField for testing purposes.
     * The SUT (System Under Test) is abstract, so we need a minimal concrete
     * class to instantiate and test its non-abstract methods.
     */
    private static class TestPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        TestPreciseDurationDateTimeField() {
            // The super constructor requires a precise duration field.
            super(DateTimeFieldType.secondOfMinute(), new StubPreciseDurationField());
        }

        // --- Methods required to make the class concrete ---

        @Override
        public int get(long instant) {
            // Not used in the test, can return a dummy value.
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            // Not used in the test, can return null.
            return null;
        }

        @Override
        public int getMaximumValue() {
            // Not used in the test, can return a dummy value.
            return 59;
        }
    }

    /**
     * A minimal, precise DurationField required by the SUT's constructor.
     */
    private static class StubPreciseDurationField extends BaseDurationField {

        protected StubPreciseDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Must be >= 1 to satisfy the SUT's constructor.
            return 1000L;
        }

        // --- Unused methods that must be implemented ---
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

    //-----------------------------------------------------------------------

    @Test
    public void getLeapAmount_shouldAlwaysReturnZero() {
        // A "precise" duration field has a fixed-length unit (e.g., a second is always 1000ms).
        // Therefore, it can never have a leap amount. This test verifies this invariant.

        // Arrange
        BaseDateTimeField field = new TestPreciseDurationDateTimeField();
        long anyInstant = 123456789L; // The result should be independent of the instant.

        // Act
        int leapAmount = field.getLeapAmount(anyInstant);

        // Assert
        assertEquals("A precise field should not have a leap amount", 0, leapAmount);
    }
}