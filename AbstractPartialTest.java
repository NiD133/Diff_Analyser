/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractPartial}.
 */
public class TestAbstractPartial {
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final long TEST_TIME_NOW = (31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;
    private static final long TEST_TIME1 = (31L + 28L + 31L + 6L - 1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
    private static final long TEST_TIME2 = (365L + 31L + 28L + 31L + 30L + 7L - 1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;

    private DateTimeZone originalDateTimeZone;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    @Test
    public void testGetValue_ValidIndex_ReturnsCorrectValue() {
        MockPartial mock = new MockPartial();
        assertEquals("Year should be 1970", 1970, mock.getValue(0));
        assertEquals("Month should be 1", 1, mock.getValue(1));
    }

    @Test
    public void testGetValue_IndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(2));
    }

    @Test
    public void testGetValues_ReturnsAllValues() {
        MockPartial mock = new MockPartial();
        int[] values = mock.getValues();
        assertEquals("Should have 2 values", 2, values.length);
        assertEquals("First value should be year", 1970, values[0]);
        assertEquals("Second value should be month", 1, values[1]);
    }

    @Test
    public void testGetField_ValidIndex_ReturnsCorrectField() {
        MockPartial mock = new MockPartial();
        Chronology chrono = BuddhistChronology.getInstanceUTC();
        assertEquals("Field 0 should be year", chrono.year(), mock.getField(0));
        assertEquals("Field 1 should be month", chrono.monthOfYear(), mock.getField(1));
    }

    @Test
    public void testGetField_IndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(2));
    }

    @Test
    public void testGetFieldType_ValidIndex_ReturnsCorrectType() {
        MockPartial mock = new MockPartial();
        assertEquals("Field type 0 should be year", DateTimeFieldType.year(), mock.getFieldType(0));
        assertEquals("Field type 1 should be month", DateTimeFieldType.monthOfYear(), mock.getFieldType(1));
    }

    @Test
    public void testGetFieldType_IndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(2));
    }

    @Test
    public void testGetFieldTypes_ReturnsAllFieldTypes() {
        MockPartial mock = new MockPartial();
        DateTimeFieldType[] fieldTypes = mock.getFieldTypes();
        assertEquals("Should have 2 field types", 2, fieldTypes.length);
        assertEquals("First field type should be year", DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals("Second field type should be month", DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }

    @Test
    public void testPropertyEquals() {
        MockProperty0 prop0 = new MockProperty0();
        
        // Equality cases
        assertTrue("Property should equal itself", prop0.equals(prop0));
        assertTrue("Property should equal same type", prop0.equals(new MockProperty0()));
        
        // Inequality cases
        assertFalse("Property should not equal different type", prop0.equals(new MockProperty1()));
        assertFalse("Property should not equal different value", prop0.equals(new MockProperty0Val()));
        assertFalse("Property should not equal different field", prop0.equals(new MockProperty0Field()));
        assertFalse("Property should not equal different chronology", prop0.equals(new MockProperty0Chrono()));
        assertFalse("Property should not equal string", prop0.equals(""));
        assertFalse("Property should not equal null", prop0.equals(null));
    }

    // Helper classes -----------------------------------------------------------------
    
    /** Mock implementation of AbstractPartial for testing */
    static class MockPartial extends AbstractPartial {
        private final int[] values = {1970, 1};
        private final Chronology chronology = BuddhistChronology.getInstanceUTC();

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0: return chrono.year();
                case 1: return chrono.monthOfYear();
                default: throw new IndexOutOfBoundsException("Invalid index: " + index);
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
            return chronology;
        }
    }
    
    /** Property implementation for field index 0 */
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

    /** Property implementation for field index 1 */
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

    /** Modified property with different field */
    static class MockProperty0Field extends MockProperty0 {
        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }

    /** Modified property with different value */
    static class MockProperty0Val extends MockProperty0 {
        @Override
        public int get() {
            return 99;
        }
    }

    /** Modified property with different chronology */
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