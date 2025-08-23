package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link PreciseDurationDateTimeField}.
 * This test focuses on the add(long, long) method.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A minimal concrete implementation of the abstract PreciseDurationDateTimeField
     * to allow for testing the functionality of the base class.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected TestablePreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // Provide dummy implementations for abstract methods not under test.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }
    }

    @Test
    public void add_withLongValue_delegatesToUnderlyingDurationField() {
        // Arrange
        long initialInstant = 1L;
        long valueToAdd = 1L;
        long expectedResult = 61L;

        // Create a mock DurationField to control its behavior and verify interactions.
        DurationField mockDurationField = mock(DurationField.class);

        // Stub the mock's methods. These are required by the SUT's constructor
        // and the method we are testing (add).
        when(mockDurationField.isPrecise()).thenReturn(true);
        when(mockDurationField.getUnitMillis()).thenReturn(60L);
        when(mockDurationField.add(initialInstant, valueToAdd)).thenReturn(expectedResult);

        // Instantiate the class under test with the mock dependency.
        PreciseDurationDateTimeField field = new TestablePreciseDurationDateTimeField(
                DateTimeFieldType.secondOfMinute(),
                mockDurationField
        );

        // Act
        long actualResult = field.add(initialInstant, valueToAdd);

        // Assert
        // 1. Verify that the method returns the correct value.
        assertEquals("The result of add should be the value returned by the duration field.",
                expectedResult, actualResult);

        // 2. Verify that the call was delegated to the duration field's add method
        //    exactly once with the correct parameters.
        verify(mockDurationField, times(1)).add(initialInstant, valueToAdd);
    }
}