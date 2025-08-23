package org.joda.time.field;

import static org.junit.Assert.assertFalse;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    //-----------------------------------------------------------------------
    // Test scenario: The isLenient() method
    //-----------------------------------------------------------------------

    @Test
    public void isLenient_shouldReturnFalse() {
        // Arrange: Create a concrete instance of the abstract class under test.
        // The isLenient() method is inherited from BaseDateTimeField and is not lenient by default.
        BaseDateTimeField field = new StubPreciseDurationDateTimeField();

        // Act & Assert: Verify that the field is not lenient.
        assertFalse(field.isLenient());
    }

    //-----------------------------------------------------------------------
    // Test Helper Classes (Stubs)
    //-----------------------------------------------------------------------

    /**
     * A stub implementation of the abstract class under test, {@link PreciseDurationDateTimeField}.
     * This class is necessary because the class under test is abstract and cannot be instantiated directly.
     * The method implementations are minimal, as they are not relevant to the test for isLenient().
     */
    private static class StubPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected StubPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new StubPreciseDurationField(DurationFieldType.seconds()));
        }

        @Override
        public int get(long instant) {
            // Not relevant for this test.
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            // Not relevant for this test.
            return new StubPreciseDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            // Not relevant for this test.
            return 59;
        }
    }

    /**
     * A stub implementation of {@link BaseDurationField} required by the
     * {@link PreciseDurationDateTimeField} constructor.
     * It must be "precise" and have a unit millisecond value of at least 1.
     */
    private static class StubPreciseDurationField extends BaseDurationField {

        protected StubPreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // A non-zero value is required by the superclass constructor.
            return 60;
        }

        // The following methods are abstract in BaseDurationField and must be implemented.
        // Their behavior is irrelevant for this test, so they return trivial values.
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return instant; }
        @Override
        public long add(long instant, long value) { return instant; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }
}