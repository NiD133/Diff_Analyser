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

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AbstractPartial class.
 * This test class uses mock implementations to test the behavior of the abstract class.
 *
 * @author Stephen Colebourne
 */
class TestAbstractPartial {

    /** A well-defined time of 1970-06-09T00:00:00Z. */
    private static final long TEST_TIME_NOW = new DateTime(1970, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDefaultZone = null;

    @BeforeEach
    void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @AfterEach
    void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDefaultZone);
        originalDefaultZone = null;
    }

    //-----------------------------------------------------------------------
    // Tests for AbstractPartial
    //-----------------------------------------------------------------------

    @Test
    void testGetValue() {
        MockPartial mock = new MockPartial();
        assertEquals(1970, mock.getValue(0));
        assertEquals(1, mock.getValue(1));
    }

    @Test
    void testGetValue_throwsExceptionForInvalidIndex() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getValue(2));
    }

    @Test
    void testGetValues() {
        MockPartial mock = new MockPartial();
        int[] expected = new int[]{1970, 1};
        assertArrayEquals(expected, mock.getValues());
    }

    @Test
    void testGetField() {
        MockPartial mock = new MockPartial();
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mock.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mock.getField(1));
    }

    @Test
    void testGetField_throwsExceptionForInvalidIndex() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getField(2));
    }

    @Test
    void testGetFieldType() {
        MockPartial mock = new MockPartial();
        assertEquals(DateTimeFieldType.year(), mock.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mock.getFieldType(1));
    }

    @Test
    void testGetFieldType_throwsExceptionForInvalidIndex() {
        MockPartial mock = new MockPartial();
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> mock.getFieldType(2));
    }

    @Test
    void testGetFieldTypes() {
        MockPartial mock = new MockPartial();
        DateTimeFieldType[] expected = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        assertArrayEquals(expected, mock.getFieldTypes());
    }

    //-----------------------------------------------------------------------
    // The following tests verify the behavior of AbstractPartialFieldProperty.equals,
    // which is a related but separate class.
    //-----------------------------------------------------------------------

    @Test
    void propertyEquals_returnsTrueForSameInstance() {
        BaseProperty prop = new BaseProperty();
        assertTrue(prop.equals(prop));
    }

    @Test
    void propertyEquals_returnsTrueForEqualInstances() {
        BaseProperty prop1 = new BaseProperty();
        BaseProperty prop2 = new BaseProperty();
        assertTrue(prop1.equals(prop2));
    }

    @Test
    void propertyEquals_returnsFalseForDifferentPropertyField() {
        BaseProperty prop1 = new BaseProperty();
        PropertyWithDifferentField prop2 = new PropertyWithDifferentField();
        assertFalse(prop1.equals(prop2));
    }

    @Test
    void propertyEquals_returnsFalseForDifferentValue() {
        BaseProperty prop1 = new BaseProperty();
        PropertyWithDifferentValue prop2 = new PropertyWithDifferentValue();
        assertFalse(prop1.equals(prop2));
    }

    @Test
    void propertyEquals_returnsFalseForDifferentChronology() {
        BaseProperty prop1 = new BaseProperty();
        PropertyWithDifferentChronology prop2 = new PropertyWithDifferentChronology();
        assertFalse(prop1.equals(prop2));
    }

    @Test
    void propertyEquals_returnsFalseForDifferentObjectType() {
        BaseProperty prop = new BaseProperty();
        assertFalse(prop.equals(""));
    }

    @Test
    void propertyEquals_returnsFalseForNull() {
        BaseProperty prop = new BaseProperty();
        assertFalse(prop.equals(null));
    }

    //-----------------------------------------------------------------------
    // Mock implementations for testing
    //-----------------------------------------------------------------------

    /**
     * A mock implementation of AbstractPartial with two fields: year and month.
     */
    static class MockPartial extends AbstractPartial {
        private final int[] values = new int[]{1970, 1};

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
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    /**
     * Base property for equality tests, representing the 'year' property of MockPartial.
     */
    static class BaseProperty extends AbstractPartialFieldProperty {
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

    /**
     * A property with a different field (monthOfYear) for equality testing.
     */
    static class PropertyWithDifferentField extends AbstractPartialFieldProperty {
        private final MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(1); // Different field
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

    /**
     * A property with a different value for equality testing.
     */
    static class PropertyWithDifferentValue extends BaseProperty {
        @Override
        public int get() {
            return 99; // Different value
        }
    }

    /**
     * A property with a different chronology for equality testing.
     */
    static class PropertyWithDifferentChronology extends BaseProperty {
        @Override
        public ReadablePartial getReadablePartial() {
            return new MockPartial() {
                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC(); // Different chronology
                }
            };
        }
    }
}