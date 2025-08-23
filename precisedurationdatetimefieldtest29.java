package org.joda.time.field;

import java.util.Arrays;
import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;

/**
 * Tests for {@link PreciseDurationDateTimeField}, focusing on the
 * set(ReadablePartial, int, int[], String, Locale) method.
 *
 * This method sets a field's value in an integer array based on a text input.
 */
public class PreciseDurationDateTimeField_SetText_Test extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(PreciseDurationDateTimeField_SetText_Test.class);
    }

    // A simplified mock field with a configurable min/max range.
    // This isolates the test from other system components and makes the field's
    // behavior explicit.
    private static class MockFieldWithRange extends PreciseDurationDateTimeField {
        private final int min;
        private final int max;

        protected MockFieldWithRange(int min, int max) {
            super(DateTimeFieldType.secondOfMinute(), new MockUnitDurationField());
            this.min = min;
            this.max = max;
        }

        // The following methods are not used by the set-from-text functionality
        // but must be implemented for the abstract class.
        @Override
        public int get(long instant) { return 0; }
        @Override
        public DurationField getRangeDurationField() { return null; }

        // These methods define the field's valid range, which is critical for these tests.
        @Override
        public int getMaximumValue() { return this.max; }
        @Override
        public int getMinimumValue() { return this.min; }
    }

    // A minimal mock for the duration field required by the super constructor.
    // Its methods are not called by the functionality under test.
    private static class MockUnitDurationField extends BaseDurationField {
        protected MockUnitDurationField() {
            super(DurationFieldType.seconds());
        }
        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 1000; }
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
        @Override public long add(long instant, int value) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    // --- Test Fixture ---

    private static final ReadablePartial DUMMY_PARTIAL = new TimeOfDay();
    private static final int FIELD_INDEX_TO_SET = 2;

    private BaseDateTimeField fieldWithRange0to59;
    private int[] initialValues;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fieldWithRange0to59 = new MockFieldWithRange(0, 59);
        initialValues = new int[]{10, 20, 30, 40};
    }

    // --- Test Cases ---

    public void testSetText_updatesValueInArray() {
        // Arrange
        final int[] expectedValues = {10, 20, 29, 40};
        final String valueToSet = "29";

        // Act
        int[] resultValues = fieldWithRange0to59.set(DUMMY_PARTIAL, FIELD_INDEX_TO_SET, initialValues, valueToSet, Locale.ENGLISH);

        // Assert
        // In JUnit 4/5, assertArrayEquals(expectedValues, resultValues) would be preferred.
        assertTrue("The value at the specified index should be updated.",
                Arrays.equals(expectedValues, resultValues));
        assertNotSame("A new array should be returned when the value changes.", initialValues, resultValues);
    }

    public void testSetText_returnsSameArrayInstanceIfValueIsUnchanged() {
        // Arrange
        // The initial array already has 30 at the target index.
        final String valueToSet = "30";

        // Act
        int[] resultValues = fieldWithRange0to59.set(DUMMY_PARTIAL, FIELD_INDEX_TO_SET, initialValues, valueToSet, null);

        // Assert
        assertSame("The original array instance should be returned if the value is not changed.",
                initialValues, resultValues);
    }

    public void testSetText_throwsExceptionWhenValueIsAboveMaximum() {
        // Arrange
        final String valueTooHigh = "60"; // Max is 59
        int[] originalValues = Arrays.copyOf(initialValues, initialValues.length);

        // Act & Assert
        try {
            fieldWithRange0to59.set(DUMMY_PARTIAL, FIELD_INDEX_TO_SET, initialValues, valueTooHigh, null);
            fail("Expected IllegalArgumentException for value greater than maximum.");
        } catch (IllegalArgumentException ex) {
            // This is the expected behavior.
        }

        // Post-Assert: Ensure the original array was not mutated on failure.
        assertTrue("Input array should not be modified when an exception is thrown.",
                Arrays.equals(originalValues, initialValues));
    }

    public void testSetText_throwsExceptionWhenValueIsBelowMinimum() {
        // Arrange
        final String valueTooLow = "-1"; // Min is 0
        int[] originalValues = Arrays.copyOf(initialValues, initialValues.length);

        // Act & Assert
        try {
            fieldWithRange0to59.set(DUMMY_PARTIAL, FIELD_INDEX_TO_SET, initialValues, valueTooLow, null);
            fail("Expected IllegalArgumentException for value less than minimum.");
        } catch (IllegalArgumentException ex) {
            // This is the expected behavior.
        }

        // Post-Assert: Ensure the original array was not mutated on failure.
        assertTrue("Input array should not be modified when an exception is thrown.",
                Arrays.equals(originalValues, initialValues));
    }
}