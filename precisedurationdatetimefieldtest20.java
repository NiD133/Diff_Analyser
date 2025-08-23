package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the add(ReadablePartial, ...) method in BaseDateTimeField.
 *
 * This test class focuses on the cascading add/subtract logic for a field within a partial instant.
 * The field under test is a mock "second of minute" field, and the partial is a TimeOfDay.
 * The tests verify that adding or subtracting seconds correctly handles carry-over and borrow
 * operations with the minute and hour fields.
 */
public class PreciseDurationDateTimeFieldAddPartialTest {

    // The index of the 'secondOfMinute' field in a TimeOfDay partial's values array.
    private static final int SECOND_OF_MINUTE_FIELD_INDEX = 2;

    // The subject under test (SUT). A mock field representing 'secondOfMinute'.
    private BaseDateTimeField secondOfMinuteField;

    @Before
    public void setUp() {
        // The method under test, BaseDateTimeField.add(ReadablePartial, ...),
        // primarily relies on getMaximumValue() and getMinimumValue() to perform
        // its cascading logic. This mock provides the necessary behavior.
        secondOfMinuteField = new MockSecondOfMinuteField();
    }

    @Test
    public void add_whenAmountIsZero_shouldNotChangeValues() {
        // Arrange
        int[] initialTime = {10, 20, 30, 40}; // 10:20:30.040
        int[] expectedTime = {10, 20, 30, 40};

        // Act
        int[] actualTime = secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, 0);

        // Assert
        assertArrayEquals(expectedTime, actualTime);
    }

    @Test
    public void add_whenAmountIsPositiveWithoutCarry_shouldIncrementField() {
        // Arrange
        int[] initialTime = {10, 20, 30, 40}; // 10:20:30.040
        int[] expectedTime = {10, 20, 31, 40}; // Expect 10:20:31.040

        // Act
        int[] actualTime = secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, 1);

        // Assert
        assertArrayEquals(expectedTime, actualTime);
    }

    @Test
    public void add_whenAmountIsPositiveWithCarry_shouldRollOverToNextField() {
        // Arrange
        // Adding 30 seconds to 10:20:30 should result in 10:21:00
        int[] initialTime = {10, 20, 30, 40};
        int[] expectedTime = {10, 21, 0, 40};

        // Act
        int[] actualTime = secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, 30);

        // Assert
        assertArrayEquals(expectedTime, actualTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_whenAmountCausesOverflow_shouldThrowException() {
        // Arrange
        // Adding 30 seconds to 23:59:30 would roll over past 23:59:59, which is invalid for TimeOfDay.
        int[] initialTime = {23, 59, 30, 40};

        // Act: This should throw an exception as the hour field would overflow.
        secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, 30);
    }

    @Test
    public void add_whenAmountIsNegativeWithoutBorrow_shouldDecrementField() {
        // Arrange
        int[] initialTime = {10, 20, 30, 40}; // 10:20:30.040
        int[] expectedTime = {10, 20, 29, 40}; // Expect 10:20:29.040

        // Act
        int[] actualTime = secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, -1);

        // Assert
        assertArrayEquals(expectedTime, actualTime);
    }

    @Test
    public void add_whenAmountIsNegativeWithBorrow_shouldRollUnderFromNextField() {
        // Arrange
        // Subtracting 31 seconds from 10:20:30 should result in 10:19:59
        int[] initialTime = {10, 20, 30, 40};
        int[] expectedTime = {10, 19, 59, 40};

        // Act
        int[] actualTime = secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, -31);

        // Assert
        assertArrayEquals(expectedTime, actualTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_whenAmountCausesUnderflow_shouldThrowException() {
        // Arrange
        // Subtracting 31 seconds from 00:00:30 would roll under past 00:00:00, which is invalid.
        int[] initialTime = {0, 0, 30, 40};

        // Act: This should throw an exception as the hour field would underflow.
        secondOfMinuteField.add(new TimeOfDay(), SECOND_OF_MINUTE_FIELD_INDEX, initialTime, -31);
    }

    //-----------------------------------------------------------------------
    // Mock classes for the test
    //-----------------------------------------------------------------------

    /**
     * A simplified mock of a "second of minute" field.
     * It extends PreciseDurationDateTimeField to gain access to the method under test,
     * which is actually the final method BaseDateTimeField.add(ReadablePartial, ...).
     */
    private static class MockSecondOfMinuteField extends PreciseDurationDateTimeField {

        protected MockSecondOfMinuteField() {
            // The constructor requires a precise duration field. We use seconds() as it's appropriate.
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        // The method under test does not use get() or set(), so these can be stubbed.
        @Override
        public int get(long instant) {
            return 0; // Not used in these tests
        }


        @Override
        public long set(long instant, int value) {
            return 0; // Not used in these tests
        }

        // This is used by the add logic to handle wrapping. For seconds, the max is 59.
        @Override
        public int getMaximumValue() {
            return 59;
        }

        // This is used by the add logic to determine the range for carry-over.
        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }
}