package org.joda.time.field;

import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;

/**
 * Test class for PreciseDurationDateTimeField.
 */
public class PreciseDurationDateTimeFieldTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        // Corrected to run tests from this class
        return new TestSuite(PreciseDurationDateTimeFieldTest.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    /**
     * A mock PreciseDurationDateTimeField for testing purposes.
     * It simulates a field with a unit size of 60 milliseconds.
     * The core `get(long instant)` method is implemented as `instant / 60`,
     * which allows for predictable return values in tests.
     */
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        @Override
        public int get(long instant) {
            // Simplified logic for predictable test outcomes.
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
    }

    // NOTE: The following mock classes are used by other tests in the full suite.
    
    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {

        protected MockStandardBaseDateTimeField() {
            super();
        }

        @Override
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    //-----------------------------------------------------------------------
    static class MockCountingDurationField extends BaseDurationField {

        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // A non-standard unit of 60ms is used for testing purposes.
            return 60;
        }

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
    }

    //-----------------------------------------------------------------------
    static class MockZeroDurationField extends BaseDurationField {

        protected MockZeroDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 0;
        }

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

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    static class MockImpreciseDurationField extends BaseDurationField {

        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return false;
        }

        @Override
        public long getUnitMillis() {
            return 0;
        }

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

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Tests that getAsText returns the string representation of the field's value,
     * ignoring the provided Locale.
     */
    public void testGetAsText_returnsStringRepresentationOfValue() {
        // Arrange
        // The getAsText() method in the base class calls get() and converts the integer result to a String.
        // Our mock field's get(instant) is implemented as `instant / 60`.
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();

        final int FIELD_VALUE = 29;
        final long INSTANT_TO_TEST = 60L * FIELD_VALUE; // An instant that yields a value of 29
        final String EXPECTED_TEXT = "29";

        // Act & Assert
        // The default implementation of getAsText should ignore the locale.
        assertEquals(
                "getAsText should format the value correctly with a non-null locale",
                EXPECTED_TEXT,
                field.getAsText(INSTANT_TO_TEST, Locale.ENGLISH)
        );

        assertEquals(
                "getAsText should format the value correctly with a null locale",
                EXPECTED_TEXT,
                field.getAsText(INSTANT_TO_TEST, null)
        );
    }
}