package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * JUnit test suite for testing the AbstractPartial class.
 * This class tests the functionality of the AbstractPartial class and its subclasses.
 * It includes tests for value retrieval, field retrieval, and property equality.
 */
public class TestAbstractPartial extends TestCase {

    // Constants for test time calculations
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    private static final long TEST_TIME_NOW = calculateMillisForDate(5, 10); // 5th May
    private static final long TEST_TIME1 = calculateMillisForDate(4, 6) + calculateMillisForTime(12, 24); // 6th April, 12:24
    private static final long TEST_TIME2 = calculateMillisForDate(7, 7) + calculateMillisForTime(14, 28); // 7th July, 14:28

    private DateTimeZone originalZone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartial.class);
    }

    public TestAbstractPartial(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalZone);
        originalZone = null;
    }

    // Helper method to calculate milliseconds for a given day and month
    private static long calculateMillisForDate(int month, int day) {
        return (31L + 28L + 31L + (month - 1) * 30L + day - 1L) * DateTimeConstants.MILLIS_PER_DAY;
    }

    // Helper method to calculate milliseconds for a given hour and minute
    private static long calculateMillisForTime(int hour, int minute) {
        return hour * DateTimeConstants.MILLIS_PER_HOUR + minute * DateTimeConstants.MILLIS_PER_MINUTE;
    }

    // Test cases for AbstractPartial class

    public void testGetValue() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(1970, mockPartial.getValue(0));
        assertEquals(1, mockPartial.getValue(1));

        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getValue(2));
    }

    public void testGetValues() {
        MockPartial mockPartial = new MockPartial();
        int[] values = mockPartial.getValues();
        assertEquals(2, values.length);
        assertEquals(1970, values[0]);
        assertEquals(1, values[1]);
    }

    public void testGetField() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mockPartial.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mockPartial.getField(1));

        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getField(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getField(2));
    }

    public void testGetFieldType() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(DateTimeFieldType.year(), mockPartial.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mockPartial.getFieldType(1));

        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getFieldType(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mockPartial.getFieldType(2));
    }

    public void testGetFieldTypes() {
        MockPartial mockPartial = new MockPartial();
        DateTimeFieldType[] fieldTypes = mockPartial.getFieldTypes();
        assertEquals(2, fieldTypes.length);
        assertEquals(DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }

    public void testGetPropertyEquals() {
        MockProperty0 property0 = new MockProperty0();
        assertTrue(property0.equals(property0));
        assertTrue(property0.equals(new MockProperty0()));
        assertFalse(property0.equals(new MockProperty1()));
        assertFalse(property0.equals(new MockProperty0Val()));
        assertFalse(property0.equals(new MockProperty0Field()));
        assertFalse(property0.equals(new MockProperty0Chrono()));
        assertFalse(property0.equals(""));
        assertFalse(property0.equals(null));
    }

    // Mock classes for testing

    static class MockPartial extends AbstractPartial {

        private final int[] values = {1970, 1};

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
        }

        @Override
        public int size() {
            return values.length;
        }

        @Override
        public int getValue(int index) {
            return values[index];
        }

        public void setValue(int index, int value) {
            values[index] = value;
        }

        @Override
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    static class MockProperty0 extends AbstractPartialFieldProperty {
        private final MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(0);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(0);
        }
    }

    static class MockProperty1 extends AbstractPartialFieldProperty {
        private final MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(1);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(1);
        }
    }

    static class MockProperty0Field extends MockProperty0 {
        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }

    static class MockProperty0Val extends MockProperty0 {
        @Override
        public int get() {
            return 99;
        }
    }

    static class MockProperty0Chrono extends MockProperty0 {
        @Override
        public ReadablePartial getReadablePartial() {
            return new MockPartial() {
                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC();
                }
            };
        }
    }
}