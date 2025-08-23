package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;

/**
 * Tests for the abstract PreciseDurationDateTimeField class.
 *
 * This test class uses mock implementations to test the functionality of the abstract class.
 */
public class PreciseDurationDateTimeFieldTest extends TestCase {

    // Note: The main() and suite() methods from JUnit 3 have been removed.
    // Modern IDEs and build tools (like Maven or Gradle) can discover and run
    // tests automatically without this boilerplate, making the code cleaner.

    //-----------------------------------------------------------------------
    // Test cases
    //-----------------------------------------------------------------------

    public void testGetName() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfDay();
        // We use a concrete mock implementation because PreciseDurationDateTimeField is abstract.
        BaseDateTimeField secondOfDayField = new MockPreciseDurationDateTimeField(
            fieldType,
            new MockCountingDurationField(DurationFieldType.seconds())
        );

        // Act
        String actualName = secondOfDayField.getName();

        // Assert
        assertEquals("The field name should match the DateTimeFieldType name", "secondOfDay", actualName);
    }

    //-----------------------------------------------------------------------
    // Mocks for testing the abstract PreciseDurationDateTimeField
    //-----------------------------------------------------------------------

    /**
     * A concrete, instantiable mock of PreciseDurationDateTimeField for testing.
     * <p>
     * This class provides minimal implementations for the abstract methods,
     * allowing it to be instantiated. Methods not under test throw
     * UnsupportedOperationException to indicate they are not implemented.
     */
    private static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        /**
         * Main constructor.
         */
        public MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
            super(type, unit);
        }

        // Methods with test-specific mock behavior
        @Override
        public int get(long instant) {
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }

        // Dummy implementations to make the class concrete
        @Override
        public long set(long instant, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationField getDurationField() {
            return super.getDurationField(); // Returns the unit field passed in constructor
        }

        @Override
        public int getMinimumValue() {
            return 0;
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

    //-----------------------------------------------------------------------
    // Mocks for DurationField used in tests
    //-----------------------------------------------------------------------

    /**
     * A mock DurationField that is precise and counts method invocations.
     * Call counts are instance fields to ensure test isolation.
     */
    private static class MockCountingDurationField extends BaseDurationField {
        int addIntCallCount = 0;
        int addLongCallCount = 0;
        int getDifferenceLongCallCount = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60; // Mocking 60 milliseconds per unit
        }

        @Override
        public long add(long instant, int value) {
            addIntCallCount++;
            return instant + (value * getUnitMillis());
        }

        @Override
        public long add(long instant, long value) {
            addLongCallCount++;
            return instant + (value * getUnitMillis());
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            getDifferenceLongCallCount++;
            return 30; // Arbitrary mock value
        }

        // Unused abstract method implementations
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }

    /**
     * A mock DurationField that is precise but has a unit millis of zero.
     * Used to test constructor validation in PreciseDurationDateTimeField.
     */
    private static class MockZeroDurationField extends BaseDurationField {
        protected MockZeroDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 0; // The key property of this mock
        }

        // Unused abstract method implementations
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

    /**
     * A mock DurationField that is imprecise.
     * Used to test constructor validation in PreciseDurationDateTimeField.
     */
    private static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return false; // The key property of this mock
        }

        @Override
        public long getUnitMillis() {
            return 60;
        }

        // Unused abstract method implementations
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
}