package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the set(ReadablePartial, ...) method in PreciseDurationDateTimeField.
 * This test suite uses mock implementations to isolate the field's behavior.
 */
public class PreciseDurationDateTimeFieldTest {

    // The field under test, configured to behave like a "second of minute" field (0-59).
    private BaseDateTimeField field;
    
    // A dummy ReadablePartial, as its state is not used in these tests.
    private final ReadablePartial dummyPartial = new TimeOfDay();

    @Before
    public void setUp() {
        // Initialize the mock field before each test.
        field = new MockPreciseDurationDateTimeField();
        
        // Reset static counters in the mock duration field to ensure test isolation.
        MockCountingDurationField.reset();
    }

    @Test
    public void setInPartial_whenNewValueIsValid_updatesArray() {
        // Arrange
        int[] initialValues = {10, 20, 30, 40};
        int[] expectedValues = {10, 20, 29, 40}; // Expect the third element to be updated

        // Act
        int[] result = field.set(dummyPartial, 2, initialValues, 29);

        // Assert
        assertArrayEquals(expectedValues, result);
    }

    @Test
    public void setInPartial_whenNewValueIsSameAsOld_returnsUnchangedArray() {
        // Arrange
        int[] initialValues = {10, 20, 30, 40};
        int[] expectedValues = {10, 20, 30, 40}; // Expect no change

        // Act
        int[] result = field.set(dummyPartial, 2, initialValues, 30);

        // Assert
        assertArrayEquals(expectedValues, result);
    }

    @Test
    public void setInPartial_whenValueIsTooLarge_throwsIllegalArgumentException() {
        // Arrange
        int[] values = {10, 20, 30, 40};
        int[] originalValues = values.clone(); // Keep a copy to verify non-mutation

        // Act & Assert
        try {
            // The mock field's maximum value is 59.
            field.set(dummyPartial, 2, values, 60);
            fail("Expected an IllegalArgumentException for value exceeding maximum.");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Verify that the input array was not modified upon failure.
        assertArrayEquals("Input array should not be modified on exception", originalValues, values);
    }

    @Test
    public void setInPartial_whenValueIsTooSmall_throwsIllegalArgumentException() {
        // Arrange
        int[] values = {10, 20, 30, 40};
        int[] originalValues = values.clone(); // Keep a copy to verify non-mutation

        // Act & Assert
        try {
            // The field's minimum value is 0.
            field.set(dummyPartial, 2, values, -1);
            fail("Expected an IllegalArgumentException for value below minimum.");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Verify that the input array was not modified upon failure.
        assertArrayEquals("Input array should not be modified on exception", originalValues, values);
    }


    //-----------------------------------------------------------------------
    // Mock implementations used for testing PreciseDurationDateTimeField.
    //-----------------------------------------------------------------------

    /**
     * A mock PreciseDurationDateTimeField that simulates a "second-of-minute" field.
     * It has a fixed range of 0 to 59.
     */
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        @Override
        public int get(long instant) {
            // A simple implementation, not critical for the set() tests.
            return (int) (instant / 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }

        @Override
        public int getMinimumValue() {
            return 0;
        }
    }

    /**
     * A mock DurationField that counts method invocations.
     * Note: The counters are static and must be reset between tests to ensure isolation.
     */
    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }
        
        public static void reset() {
            add_int = 0;
            add_long = 0;
            difference_long = 0;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60;
        }

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
        
        // Unused methods for this test
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }
}