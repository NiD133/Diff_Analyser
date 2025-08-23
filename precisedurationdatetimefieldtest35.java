package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PreciseDurationDateTimeField} class.
 */
public class PreciseDurationDateTimeFieldTest {

    @Test
    public void getMinimumValue_shouldReturnZero() {
        // Arrange: Create a minimal concrete implementation of the abstract class under test.
        // This test verifies the getMinimumValue() behavior, which is inherited from the
        // BaseDateTimeField superclass and is not overridden.
        BaseDateTimeField field = new StubPreciseDurationDateTimeField();

        // Act: Call the method under test. The instant (0L) is arbitrary
        // as the method's return value does not depend on it.
        int minimumValue = field.getMinimumValue(0L);

        // Assert: The minimum value should be 0, as defined in the superclass.
        assertEquals(0, minimumValue);
    }

    /**
     * A minimal, concrete implementation of PreciseDurationDateTimeField for testing purposes.
     * <p>
     * This "stub" class only provides the necessary components to be instantiated, allowing
     * tests to focus on specific, non-abstract methods of the class hierarchy. Abstract
     * methods that are not relevant to this test throw UnsupportedOperationException to
     * prevent accidental usage.
     */
    private static class StubPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        StubPreciseDurationDateTimeField() {
            super(DateTimeFieldType.millisOfSecond(), new StubPreciseDurationField());
        }

        // The following abstract methods are not relevant to this test and are stubbed out.

        @Override
        public int get(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long set(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getRangeDurationField() {
            return null; // Not needed for this test
        }

        @Override
        public int getMaximumValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isLenient() {
            return false;
        }

        @Override
        public long roundFloor(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long roundCeiling(long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long remainder(long instant) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A stub implementation of DurationField that is precise and has a valid unit size.
     * This is the minimal requirement to satisfy the constructor of PreciseDurationDateTimeField.
     */
    private static class StubPreciseDurationField extends BaseDurationField {

        StubPreciseDurationField() {
            super(DurationFieldType.millis());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // Must be >= 1 for the PreciseDurationDateTimeField constructor.
            return 1;
        }

        // The following abstract methods are not used in this test context.

        @Override
        public long getValueAsLong(long duration, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(int value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getMillis(long value, long instant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long instant, long value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            throw new UnsupportedOperationException();
        }
    }
}