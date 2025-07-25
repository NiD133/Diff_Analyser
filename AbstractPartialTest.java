package org.joda.time;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AbstractPartial}.
 */
public class TestAbstractPartial {

    private DateTimeZone originalTimeZone;
    private long testTimeNow;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(getTestTimeNow());
        originalTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalTimeZone);
    }

    private long getTestTimeNow() {
        if (testTimeNow == 0) {
            testTimeNow = (31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;
        }
        return testTimeNow;
    }

    /**
     * Tests that the getValue method returns the correct value for a given index.
     */
    @Test
    public void testGetValue() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(1970, mockPartial.getValue(0));
        assertEquals(1, mockPartial.getValue(1));

        // Test that IndexOutOfBoundsException is thrown for invalid indices
        try {
            mockPartial.getValue(-1);
            fail("Expected IndexOutOfBoundsException for index -1");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            mockPartial.getValue(2);
            fail("Expected IndexOutOfBoundsException for index 2");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Tests that the getValues method returns the correct values for all indices.
     */
    @Test
    public void testGetValues() {
        MockPartial mockPartial = new MockPartial();
        int[] values = mockPartial.getValues();
        assertEquals(2, values.length);
        assertEquals(1970, values[0]);
        assertEquals(1, values[1]);
    }

    /**
     * Tests that the getField method returns the correct field for a given index.
     */
    @Test
    public void testGetField() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mockPartial.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mockPartial.getField(1));

        // Test that IndexOutOfBoundsException is thrown for invalid indices
        try {
            mockPartial.getField(-1);
            fail("Expected IndexOutOfBoundsException for index -1");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            mockPartial.getField(2);
            fail("Expected IndexOutOfBoundsException for index 2");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Tests that the getFieldType method returns the correct field type for a given index.
     */
    @Test
    public void testGetFieldType() {
        MockPartial mockPartial = new MockPartial();
        assertEquals(DateTimeFieldType.year(), mockPartial.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mockPartial.getFieldType(1));

        // Test that IndexOutOfBoundsException is thrown for invalid indices
        try {
            mockPartial.getFieldType(-1);
            fail("Expected IndexOutOfBoundsException for index -1");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            mockPartial.getFieldType(2);
            fail("Expected IndexOutOfBoundsException for index 2");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Tests that the getFieldTypes method returns the correct field types for all indices.
     */
    @Test
    public void testGetFieldTypes() {
        MockPartial mockPartial = new MockPartial();
        DateTimeFieldType[] fieldTypes = mockPartial.getFieldTypes();
        assertEquals(2, fieldTypes.length);
        assertEquals(DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }

    /**
     * Tests that the equals method returns true for equal objects and false for unequal objects.
     */
    @Test
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

    // Mock classes

    static class MockPartial extends AbstractPartial {

        private int[] values = {1970, 1};

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public int getValue(int index) {
            return values[index];
        }

        @Override
        public void setValue(int index, int value) {
            values[index] = value;
        }

        @Override
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    static class MockProperty0 extends AbstractPartialFieldProperty {

        private MockPartial partial = new MockPartial();

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

        private MockPartial partial = new MockPartial();

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