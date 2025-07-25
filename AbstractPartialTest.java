package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * This class is a JUnit unit test for YearMonthDay.
 * It tests the functionality of the AbstractPartial class and its related components.
 * 
 * Author: Stephen Colebourne
 */
public class TestAbstractPartial extends TestCase {

    // Constants for time zones and test times
    private static final DateTimeZone PARIS_TIME_ZONE = DateTimeZone.forID("Europe/Paris");
    private static final long TEST_TIME_NOW = calculateMillisForDate(5, 9); // 5th month, 9th day
    private static final long TEST_TIME1 = calculateMillisForDate(4, 6) + calculateMillisForTime(12, 24); // 4th month, 6th day, 12:24
    private static final long TEST_TIME2 = calculateMillisForDate(13, 7) + calculateMillisForTime(14, 28); // 13th month, 7th day, 14:28

    private DateTimeZone originalTimeZone = null;

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
        // Set the current time to a fixed value for testing
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original time settings
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalTimeZone);
        originalTimeZone = null;
    }

    // Helper method to calculate milliseconds for a given date
    private static long calculateMillisForDate(int month, int day) {
        return (31L + 28L + 31L + month + day - 1L) * DateTimeConstants.MILLIS_PER_DAY;
    }

    // Helper method to calculate milliseconds for a given time
    private static long calculateMillisForTime(int hour, int minute) {
        return hour * DateTimeConstants.MILLIS_PER_HOUR + minute * DateTimeConstants.MILLIS_PER_MINUTE;
    }

    // Test cases
    public void testGetValue() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(1970, mockPartial.getValue(0));
        assertEquals(1, mockPartial.getValue(1));

        assertThrowsIndexOutOfBounds(() -> mockPartial.getValue(-1));
        assertThrowsIndexOutOfBounds(() -> mockPartial.getValue(2));
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

        assertThrowsIndexOutOfBounds(() -> mockPartial.getField(-1));
        assertThrowsIndexOutOfBounds(() -> mockPartial.getField(2));
    }

    public void testGetFieldType() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(DateTimeFieldType.year(), mockPartial.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mockPartial.getFieldType(1));

        assertThrowsIndexOutOfBounds(() -> mockPartial.getFieldType(-1));
        assertThrowsIndexOutOfBounds(() -> mockPartial.getFieldType(2));
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

    // Helper method to assert that an IndexOutOfBoundsException is thrown
    private void assertThrowsIndexOutOfBounds(Runnable runnable) {
        try {
            runnable.run();
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Expected exception
        }
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
                    throw new IndexOutOfBoundsException("Invalid index: " + index);
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