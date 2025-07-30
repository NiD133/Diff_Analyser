package org.joda.time.field;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

import java.util.Arrays;
import java.util.Locale;

/**
 * Unit tests for PreciseDurationDateTimeField.
 */
public class TestPreciseDurationDateTimeField extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPreciseDurationDateTimeField.class);
    }

    public TestPreciseDurationDateTimeField(String name) {
        super(name);
    }

    @Override
    protected void setUp() {
        // Setup resources if needed
    }

    @Override
    protected void tearDown() {
        // Clean up resources if needed
    }

    // Test constructor behavior
    public void testConstructor() {
        testValidConstructor();
        testConstructorWithNullArguments();
        testConstructorWithImpreciseDuration();
        testConstructorWithZeroDuration();
    }

    private void testValidConstructor() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Field type should be secondOfMinute", DateTimeFieldType.secondOfMinute(), field.getType());
    }

    private void testConstructorWithNullArguments() {
        try {
            new MockPreciseDurationDateTimeField(null, null);
            fail("Constructor should throw IllegalArgumentException for null arguments");
        } catch (IllegalArgumentException expected) {
        }
    }

    private void testConstructorWithImpreciseDuration() {
        try {
            new MockPreciseDurationDateTimeField(
                DateTimeFieldType.minuteOfHour(),
                new MockImpreciseDurationField(DurationFieldType.minutes()));
            fail("Constructor should throw IllegalArgumentException for imprecise duration");
        } catch (IllegalArgumentException expected) {
        }
    }

    private void testConstructorWithZeroDuration() {
        try {
            new MockPreciseDurationDateTimeField(
                DateTimeFieldType.minuteOfHour(),
                new MockZeroDurationField(DurationFieldType.minutes()));
            fail("Constructor should throw IllegalArgumentException for zero duration");
        } catch (IllegalArgumentException expected) {
        }
    }

    // Test getType method
    public void testGetType() {
        BaseDateTimeField field = createFieldWithCountingDuration();
        assertEquals("Field type should be secondOfDay", DateTimeFieldType.secondOfDay(), field.getType());
    }

    // Test getName method
    public void testGetName() {
        BaseDateTimeField field = createFieldWithCountingDuration();
        assertEquals("Field name should be secondOfDay", "secondOfDay", field.getName());
    }

    // Test toString method
    public void testToString() {
        BaseDateTimeField field = createFieldWithCountingDuration();
        assertEquals("String representation should be DateTimeField[secondOfDay]", "DateTimeField[secondOfDay]", field.toString());
    }

    // Test isSupported method
    public void testIsSupported() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertTrue("Field should be supported", field.isSupported());
    }

    // Test isLenient method
    public void testIsLenient() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse("Field should not be lenient", field.isLenient());
    }

    // Test get method
    public void testGet() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Value at 0 should be 0", 0, field.get(0));
        assertEquals("Value at 60 should be 1", 1, field.get(60));
        assertEquals("Value at 123 should be 2", 2, field.get(123));
    }

    // Test getAsText methods
    public void testGetAsText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Text representation should be 29", "29", field.getAsText(60L * 29, Locale.ENGLISH));
        assertEquals("Text representation should be 29", "29", field.getAsText(60L * 29, null));
    }

    public void testGetAsTextWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Text representation should be 20", "20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("Text representation should be 40", "40", field.getAsText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
    }

    // Test getAsShortText methods
    public void testGetAsShortText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Short text representation should be 29", "29", field.getAsShortText(60L * 29, Locale.ENGLISH));
        assertEquals("Short text representation should be 29", "29", field.getAsShortText(60L * 29, null));
    }

    public void testGetAsShortTextWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Short text representation should be 20", "20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("Short text representation should be 40", "40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
    }

    // Test add methods
    public void testAdd() {
        MockCountingDurationField.add_int = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Adding 1 to 1L should result in 61", 61, field.add(1L, 1));
        assertEquals("Add method should be called once", 1, MockCountingDurationField.add_int);
    }

    public void testAddLong() {
        MockCountingDurationField.add_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Adding 1L to 1L should result in 61", 61, field.add(1L, 1L));
        assertEquals("Add method should be called once", 1, MockCountingDurationField.add_long);
    }

    public void testAddWithTimeOfDay() {
        int[] values = new int[]{10, 20, 30, 40};
        int[] expected = new int[]{10, 20, 31, 40};
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] result = field.add(new TimeOfDay(), 2, values, 1);
        assertTrue("Result should match expected values", Arrays.equals(expected, result));
    }

    // Test addWrapField methods
    public void testAddWrapField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Adding wrap field should result in 29 * 60L", 29 * 60L, field.addWrapField(60L * 29, 0));
        assertEquals("Adding wrap field should result in 59 * 60L", 59 * 60L, field.addWrapField(60L * 29, 30));
    }

    public void testAddWrapFieldWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int[] values = new int[]{10, 20, 30, 40};
        int[] expected = new int[]{10, 20, 59, 40};
        int[] result = field.addWrapField(new TimeOfDay(), 2, values, 29);
        assertTrue("Result should match expected values", Arrays.equals(result, expected));
    }

    // Test getDifference methods
    public void testGetDifference() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Difference should be 30", 30, field.getDifference(0L, 0L));
        assertEquals("Difference method should be called once", 1, MockCountingDurationField.difference_long);
    }

    public void testGetDifferenceAsLong() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Difference should be 30", 30, field.getDifferenceAsLong(0L, 0L));
        assertEquals("Difference method should be called once", 1, MockCountingDurationField.difference_long);
    }

    // Test set methods
    public void testSet() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Setting value should result in 0", 0, field.set(120L, 0));
        assertEquals("Setting value should result in 29 * 60", 29 * 60, field.set(120L, 29));
    }

    public void testSetWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int[] values = new int[]{10, 20, 30, 40};
        int[] expected = new int[]{10, 20, 29, 40};
        int[] result = field.set(new TimeOfDay(), 2, values, 29);
        assertTrue("Result should match expected values", Arrays.equals(result, expected));
    }

    public void testSetWithString() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Setting value should result in 0", 0, field.set(0L, "0"));
        assertEquals("Setting value should result in 29 * 60", 29 * 60, field.set(0L, "29"));
    }

    // Test convertText method
    public void testConvertText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Conversion should result in 0", 0, field.convertText("0", null));
        assertEquals("Conversion should result in 29", 29, field.convertText("29", null));
        try {
            field.convertText("2A", null);
            fail("Conversion should throw IllegalArgumentException for invalid text");
        } catch (IllegalArgumentException expected) {
        }
    }

    // Test isLeap methods
    public void testIsLeap() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse("Field should not be leap", field.isLeap(0L));
    }

    public void testGetLeapAmount() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Leap amount should be 0", 0, field.getLeapAmount(0L));
    }

    public void testGetLeapDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertNull("Leap duration field should be null", field.getLeapDurationField());
    }

    // Test getMinimumValue methods
    public void testGetMinimumValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Minimum value should be 0", 0, field.getMinimumValue());
    }

    public void testGetMinimumValueWithLong() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Minimum value should be 0", 0, field.getMinimumValue(0L));
    }

    public void testGetMinimumValueWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Minimum value should be 0", 0, field.getMinimumValue(new TimeOfDay()));
    }

    // Test getMaximumValue methods
    public void testGetMaximumValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Maximum value should be 59", 59, field.getMaximumValue());
    }

    public void testGetMaximumValueWithLong() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Maximum value should be 59", 59, field.getMaximumValue(0L));
    }

    public void testGetMaximumValueWithTimeOfDay() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Maximum value should be 59", 59, field.getMaximumValue(new TimeOfDay()));
    }

    // Test getMaximumTextLength method
    public void testGetMaximumTextLength() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Maximum text length should be 2", 2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    // Test rounding methods
    public void testRoundFloor() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Rounding floor should result in -120L", -120L, field.roundFloor(-61L));
        assertEquals("Rounding floor should result in 0L", 0L, field.roundFloor(0L));
    }

    public void testRoundCeiling() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Rounding ceiling should result in -60L", -60L, field.roundCeiling(-61L));
        assertEquals("Rounding ceiling should result in 60L", 60L, field.roundCeiling(1L));
    }

    public void testRoundHalfFloor() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Rounding half floor should result in 0L", 0L, field.roundHalfFloor(0L));
        assertEquals("Rounding half floor should result in 60L", 60L, field.roundHalfFloor(31L));
    }

    public void testRoundHalfCeiling() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Rounding half ceiling should result in 0L", 0L, field.roundHalfCeiling(0L));
        assertEquals("Rounding half ceiling should result in 60L", 60L, field.roundHalfCeiling(31L));
    }

    public void testRoundHalfEven() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Rounding half even should result in 0L", 0L, field.roundHalfEven(0L));
        assertEquals("Rounding half even should result in 60L", 60L, field.roundHalfEven(31L));
        assertEquals("Rounding half even should result in 120L", 120L, field.roundHalfEven(90L));
    }

    public void testRemainder() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("Remainder should be 0L", 0L, field.remainder(0L));
        assertEquals("Remainder should be 30L", 30L, field.remainder(30L));
    }

    // Helper methods for creating fields
    private BaseDateTimeField createFieldWithCountingDuration() {
        return new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(),
            new MockCountingDurationField(DurationFieldType.minutes()));
    }

    // Mock classes for testing
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(),
                new MockCountingDurationField(DurationFieldType.seconds()));
        }

        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        @Override
        public int get(long instant) {
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
}