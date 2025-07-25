package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * Tests for the {@link AbstractPartial} class.
 * <p>
 * This class provides a base implementation for partial date-time classes
 * like {@link YearMonth} and {@link TimeOfDay}. These tests ensure the
 * base implementation behaves as expected.
 */
public class TestAbstractPartial extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // Define fixed test times for consistency
    private static final long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private static final long TEST_TIME1 =
            (31L + 28L + 31L + 6L - 1L) * DateTimeConstants.MILLIS_PER_DAY
                    + 12L * DateTimeConstants.MILLIS_PER_HOUR
                    + 24L * DateTimeConstants.MILLIS_PER_MINUTE;

    private static final long TEST_TIME2 =
            (365L + 31L + 28L + 31L + 30L + 7L - 1L) * DateTimeConstants.MILLIS_PER_DAY
                    + 14L * DateTimeConstants.MILLIS_PER_HOUR
                    + 28L * DateTimeConstants.MILLIS_PER_MINUTE;

    private DateTimeZone originalDateTimeZone;

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
        // Fix the current time to TEST_TIME_NOW for predictable results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Store the original time zone and set the default to UTC
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original time zone and reset the current time
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        originalDateTimeZone = null;
    }

    /**
     * Tests the {@link AbstractPartial#getValue(int)} method.
     */
    public void testGetValue() {
        MockPartial mock = new MockPartial();

        // Verify that the method returns the correct values for valid indices
        assertEquals(1970, mock.getValue(0));
        assertEquals(1, mock.getValue(1));

        // Verify that the method throws an IndexOutOfBoundsException for invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(2));
    }

    /**
     * Tests the {@link AbstractPartial#getValues()} method.
     */
    public void testGetValues() {
        MockPartial mock = new MockPartial();
        int[] values = mock.getValues();

        // Verify the length and contents of the returned array
        assertEquals(2, values.length);
        assertEquals(1970, values[0]);
        assertEquals(1, values[1]);
    }

    /**
     * Tests the {@link AbstractPartial#getField(int)} method.
     */
    public void testGetField() {
        MockPartial mock = new MockPartial();

        // Verify that the method returns the correct fields for valid indices
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mock.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mock.getField(1));

        // Verify that the method throws an IndexOutOfBoundsException for invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(2));
    }

    /**
     * Tests the {@link AbstractPartial#getFieldType(int)} method.
     */
    public void testGetFieldType() {
        MockPartial mock = new MockPartial();

        // Verify that the method returns the correct field types for valid indices
        assertEquals(DateTimeFieldType.year(), mock.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mock.getFieldType(1));

        // Verify that the method throws an IndexOutOfBoundsException for invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(2));
    }

    /**
     * Tests the {@link AbstractPartial#getFieldTypes()} method.
     */
    public void testGetFieldTypes() {
        MockPartial mock = new MockPartial();
        DateTimeFieldType[] fieldTypes = mock.getFieldTypes();

        // Verify the length and contents of the returned array
        assertEquals(2, fieldTypes.length);
        assertEquals(DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }

    /**
     * Tests the {@link AbstractPartialFieldProperty#equals(Object)} method.
     */
    public void testGetPropertyEquals() {
        MockProperty0 prop0 = new MockProperty0();

        // Verify equality with self and another instance of the same class
        assertEquals(true, prop0.equals(prop0));
        assertEquals(true, prop0.equals(new MockProperty0()));

        // Verify inequality with instances of different classes
        assertEquals(false, prop0.equals(new MockProperty1()));
        assertEquals(false, prop0.equals(new MockProperty0Val()));
        assertEquals(false, prop0.equals(new MockProperty0Field()));
        assertEquals(false, prop0.equals(new MockProperty0Chrono()));

        // Verify inequality with non-property objects
        assertEquals(false, prop0.equals(""));
        assertEquals(false, prop0.equals(null));
    }

    //-----------------------------------------------------------------------
    /**
     * A mock implementation of {@link AbstractPartial} for testing purposes.
     */
    static class MockPartial extends AbstractPartial {

        int[] val = new int[]{1970, 1};

        MockPartial() {
            super();
        }

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
            return 2;
        }

        @Override
        public int getValue(int index) {
            return val[index];
        }

        public void setValue(int index, int value) {
            val[index] = value;
        }

        @Override
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }

        @Override
        public DateTimeFieldType getFieldType(int index) {
            switch (index) {
                case 0: return DateTimeFieldType.year();
                case 1: return DateTimeFieldType.monthOfYear();
                default: throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public DateTimeFieldType[] getFieldTypes() {
            return new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
        }

        @Override
        public DateTimeField getField(int index) {
            return getField(index, getChronology());
        }

        @Override
        public DateTimeField[] getFields() {
            return new DateTimeField[] { getField(0), getField(1) };
        }

        @Override
        public int[] getValues() {
            return val;
        }

        @Override
        public int get(DateTimeFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSupported(DateTimeFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(DateTimeFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int indexOfSupported(DateTimeFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int indexOf(DurationFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int indexOfSupported(DurationFieldType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DateTime toDateTime(ReadableInstant baseInstant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(ReadablePartial other) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isAfter(ReadablePartial partial) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isBefore(ReadablePartial partial) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEqual(ReadablePartial partial) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Mock implementations of {@link AbstractPartialFieldProperty} for testing purposes.
     */
    static class MockProperty0 extends AbstractPartialFieldProperty {
        MockPartial partial = new MockPartial();

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
        MockPartial partial = new MockPartial();

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

    private void assertThrows(Class<? extends Exception> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception of type " + exceptionClass.getName() + " but no exception was thrown.");
        } catch (Exception e) {
            if (!exceptionClass.isInstance(e)) {
                fail("Expected exception of type " + exceptionClass.getName() + " but got " + e.getClass().getName());
            }
        }
    }
}