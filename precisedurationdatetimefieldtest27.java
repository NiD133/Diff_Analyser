package org.joda.time.field;

import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Unit tests for the PreciseDurationDateTimeField class.
 */
public class PreciseDurationDateTimeFieldTest extends TestCase {

    /**
     * Main method for running tests from the command line, a JUnit 3 convention.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test suite provider for JUnit 3.
     */
    public static TestSuite suite() {
        return new TestSuite(PreciseDurationDateTimeFieldTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Reset static counters before each test to ensure test isolation.
        MockCountingDurationField.reset();
    }

    //-----------------------------------------------------------------------
    public void testSet_fromString_updatesInstantCorrectly() {
        // Arrange
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        final long UNIT_MILLIS = MockPreciseDurationDateTimeField.UNIT_MILLIS;

        // --- Scenario 1: Set value on an instant that is already at zero ---
        long initialInstant1 = 0L; // Corresponds to a field value of 0.
        int newValue1 = 29;
        long expectedInstant1 = newValue1 * UNIT_MILLIS; // 29 * 60ms = 1740ms

        // Act
        long actualInstant1 = field.set(initialInstant1, Integer.toString(newValue1), Locale.ENGLISH);

        // Assert
        assertEquals("Setting from 0 to 29 should result in an instant of 29 * unitMillis",
                expectedInstant1, actualInstant1);
        assertEquals("The new instant should reflect the new field value",
                newValue1, field.get(actualInstant1));

        // --- Scenario 2: Set value on an instant with a non-zero value ---
        // get(1234L) = 1234 / 60 = 20. Remainder is 34.
        long initialInstant2 = 1234L;
        int newValue2 = 5; // Set the field value to 5.
        // The 'set' operation first removes the original value (20 units) and then adds the new one (5 units).
        // 1234 - (20 * 60) + (5 * 60) = 1234 - 1200 + 300 = 334.
        long expectedInstant2 = 334L;

        // Act
        long actualInstant2 = field.set(initialInstant2, Integer.toString(newValue2), null);

        // Assert
        assertEquals("Setting from 20 to 5 should correctly adjust the instant",
                expectedInstant2, actualInstant2);
        assertEquals("The new instant should reflect the new field value",
                newValue2, field.get(actualInstant2));
    }

    //-----------------------------------------------------------------------
    // MOCK IMPLEMENTATIONS
    //-----------------------------------------------------------------------

    /**
     * A mock PreciseDurationDateTimeField that represents a "second of minute"
     * but with a non-standard unit of 60 milliseconds for testing purposes.
     */
    private static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        // Using a non-standard unit of 60ms simplifies test calculations.
        static final long UNIT_MILLIS = 60L;

        MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        @Override
        public int get(long instant) {
            // Calculates the number of 60ms units in the given instant.
            return (int) (instant / UNIT_MILLIS);
        }

        @Override
        public DurationField getRangeDurationField() {
            // A dummy range field for minutes.
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A mock DurationField that counts method calls and uses a fixed unit
     * for its calculations.
     */
    private static class MockCountingDurationField extends BaseDurationField {
        private static int add_int_calls = 0;
        private static int add_long_calls = 0;
        private static int difference_long_calls = 0;

        MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        public static void reset() {
            add_int_calls = 0;
            add_long_calls = 0;
            difference_long_calls = 0;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return MockPreciseDurationDateTimeField.UNIT_MILLIS;
        }

        @Override
        public long add(long instant, int value) {
            add_int_calls++;
            return instant + (value * getUnitMillis());
        }

        @Override
        public long add(long instant, long value) {
            add_long_calls++;
            return instant + (value * getUnitMillis());
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long_calls++;
            // Return a fixed value, not used in the test above but required by the interface.
            return 30;
        }

        // Unused methods required by the abstract parent class.
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
    }
}