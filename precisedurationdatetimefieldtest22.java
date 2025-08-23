package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the addWrapField method in PreciseDurationDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest {

    private MockPreciseDurationDateTimeField secondOfMinuteField;
    private TimeOfDay dummyPartial;

    @Before
    public void setUp() {
        // A mock field representing 'secondOfMinute', with a range of 0-59.
        secondOfMinuteField = new MockPreciseDurationDateTimeField();
        // A dummy ReadablePartial instance, required by the method under test.
        dummyPartial = new TimeOfDay();
    }

    @Test
    public void addWrapField_shouldDoNothingWhenAmountIsZero() {
        // Arrange
        final int fieldIndexToModify = 2;
        final int[] initialValues = {10, 20, 30, 40};
        final int amountToAdd = 0;
        final int[] expectedValues = {10, 20, 30, 40};

        // Act
        int[] actualValues = secondOfMinuteField.addWrapField(dummyPartial, fieldIndexToModify, initialValues, amountToAdd);

        // Assert
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void addWrapField_shouldAddAmountWithoutWrapping() {
        // Arrange
        final int fieldIndexToModify = 2;
        final int[] initialValues = {10, 20, 30, 40};
        // Adding 29 to 30 results in 59, which is the maximum value.
        final int amountToAdd = 29;
        final int[] expectedValues = {10, 20, 59, 40};

        // Act
        int[] actualValues = secondOfMinuteField.addWrapField(dummyPartial, fieldIndexToModify, initialValues, amountToAdd);

        // Assert
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void addWrapField_shouldWrapAroundToMinimumValue() {
        // Arrange
        final int fieldIndexToModify = 2;
        final int[] initialValues = {10, 20, 30, 40};
        // Adding 30 to 30 results in 60. The field wraps around (0-59), so the result is 0.
        final int amountToAdd = 30;
        final int[] expectedValues = {10, 20, 0, 40};

        // Act
        int[] actualValues = secondOfMinuteField.addWrapField(dummyPartial, fieldIndexToModify, initialValues, amountToAdd);

        // Assert
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void addWrapField_shouldWrapAroundPastMinimumValue() {
        // Arrange
        final int fieldIndexToModify = 2;
        final int[] initialValues = {10, 20, 30, 40};
        // Adding 31 to 30 results in 61. The field wraps around (0-59), so the result is 1.
        final int amountToAdd = 31;
        final int[] expectedValues = {10, 20, 1, 40};

        // Act
        int[] actualValues = secondOfMinuteField.addWrapField(dummyPartial, fieldIndexToModify, initialValues, amountToAdd);

        // Assert
        assertArrayEquals(expectedValues, actualValues);
    }

    //-----------------------------------------------------------------------
    // Mock classes used for testing
    //-----------------------------------------------------------------------

    /**
     * A mock PreciseDurationDateTimeField that simulates a "second of minute" field.
     * It has a fixed range from 0 to 59.
     */
    private static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockSecondsDurationField());
        }

        @Override
        public int get(long instant) {
            // Not used in these tests, but required for the abstract class.
            return (int) (instant / 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            // A dummy range field, sufficient for the test.
            return new MockMinutesDurationField();
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
        
        // getMinimumValue() defaults to 0 in the superclass BaseDateTimeField.
    }

    /**
     * A mock DurationField for seconds.
     */
    private static class MockSecondsDurationField extends BaseDurationField {
        protected MockSecondsDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60; // This value is not critical for the addWrapField test.
        }

        // The following methods are not used by the tests and can be minimal.
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return instant + (value * 60L); }
        @Override
        public long add(long instant, long value) { return instant + (value * 60L); }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    /**
     * A mock DurationField for minutes, used as the range field.
     */
    private static class MockMinutesDurationField extends MockSecondsDurationField {
        protected MockMinutesDurationField() {
            super();
        }
    }
}