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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * Unit tests for the AbstractPartial class.
 * Tests the basic functionality of partial date/time representations.
 *
 * @author Stephen Colebourne
 */
public class TestAbstractPartial extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    
    // Test time: June 9, 1970 (160 days into year)
    private static final long TEST_TIME_NOW = 
            160L * DateTimeConstants.MILLIS_PER_DAY - DateTimeConstants.MILLIS_PER_DAY;
            
    // Test time: April 6, 1970 at 12:24
    private static final long TEST_TIME1 =
        96L * DateTimeConstants.MILLIS_PER_DAY - DateTimeConstants.MILLIS_PER_DAY
        + 12L * DateTimeConstants.MILLIS_PER_HOUR
        + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // Test time: May 7, 1971 at 14:28
    private static final long TEST_TIME2 =
        492L * DateTimeConstants.MILLIS_PER_DAY - DateTimeConstants.MILLIS_PER_DAY
        + 14L * DateTimeConstants.MILLIS_PER_HOUR
        + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
        
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
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalTimeZone);
        originalTimeZone = null;
    }

    //-----------------------------------------------------------------------
    // Tests for getValue(int index)
    //-----------------------------------------------------------------------
    
    public void testGetValue_ReturnsCorrectFieldValues() throws Throwable {
        // Given a partial with year=1970 and month=1
        TestablePartial partial = new TestablePartial();
        
        // When getting values by index
        // Then correct values are returned
        assertEquals("Year should be 1970", 1970, partial.getValue(0));
        assertEquals("Month should be 1", 1, partial.getValue(1));
    }
    
    public void testGetValue_ThrowsExceptionForInvalidIndex() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        // Negative index should throw exception
        try {
            partial.getValue(-1);
            fail("Should throw IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        
        // Index beyond size should throw exception
        try {
            partial.getValue(2);
            fail("Should throw IndexOutOfBoundsException for index >= size");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getValues()
    //-----------------------------------------------------------------------
    
    public void testGetValues_ReturnsArrayOfAllFieldValues() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        int[] values = partial.getValues();
        
        assertEquals("Should return 2 values", 2, values.length);
        assertEquals("First value should be year 1970", 1970, values[0]);
        assertEquals("Second value should be month 1", 1, values[1]);
    }

    //-----------------------------------------------------------------------
    // Tests for getField(int index)
    //-----------------------------------------------------------------------
    
    public void testGetField_ReturnsCorrectDateTimeFields() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        assertEquals("First field should be year", 
                    BuddhistChronology.getInstanceUTC().year(), 
                    partial.getField(0));
        assertEquals("Second field should be monthOfYear", 
                    BuddhistChronology.getInstanceUTC().monthOfYear(), 
                    partial.getField(1));
    }
    
    public void testGetField_ThrowsExceptionForInvalidIndex() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        try {
            partial.getField(-1);
            fail("Should throw IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        
        try {
            partial.getField(2);
            fail("Should throw IndexOutOfBoundsException for index >= size");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getFieldType(int index)
    //-----------------------------------------------------------------------
    
    public void testGetFieldType_ReturnsCorrectFieldTypes() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        assertEquals("First field type should be year", 
                    DateTimeFieldType.year(), 
                    partial.getFieldType(0));
        assertEquals("Second field type should be monthOfYear", 
                    DateTimeFieldType.monthOfYear(), 
                    partial.getFieldType(1));
    }
    
    public void testGetFieldType_ThrowsExceptionForInvalidIndex() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        try {
            partial.getFieldType(-1);
            fail("Should throw IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
        
        try {
            partial.getFieldType(2);
            fail("Should throw IndexOutOfBoundsException for index >= size");
        } catch (IndexOutOfBoundsException expected) {
            // Expected exception
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getFieldTypes()
    //-----------------------------------------------------------------------
    
    public void testGetFieldTypes_ReturnsArrayOfAllFieldTypes() throws Throwable {
        TestablePartial partial = new TestablePartial();
        
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        
        assertEquals("Should return 2 field types", 2, fieldTypes.length);
        assertEquals("First should be year", DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals("Second should be monthOfYear", DateTimeFieldType.monthOfYear(), fieldTypes[1]);
    }

    //-----------------------------------------------------------------------
    // Tests for AbstractPartialFieldProperty.equals()
    //-----------------------------------------------------------------------
    
    public void testPropertyEquals_SameInstance() throws Throwable {
        TestableProperty property = new TestableProperty(0);
        
        assertTrue("Property should equal itself", property.equals(property));
    }
    
    public void testPropertyEquals_EquivalentProperties() throws Throwable {
        TestableProperty property1 = new TestableProperty(0);
        TestableProperty property2 = new TestableProperty(0);
        
        assertTrue("Properties with same field index should be equal", 
                  property1.equals(property2));
    }
    
    public void testPropertyEquals_DifferentFieldIndex() throws Throwable {
        TestableProperty yearProperty = new TestableProperty(0);
        TestableProperty monthProperty = new TestableProperty(1);
        
        assertFalse("Properties with different field indices should not be equal", 
                   yearProperty.equals(monthProperty));
    }
    
    public void testPropertyEquals_DifferentValue() throws Throwable {
        TestableProperty property = new TestableProperty(0);
        TestablePropertyWithDifferentValue differentValueProperty = 
            new TestablePropertyWithDifferentValue(0);
        
        assertFalse("Properties with different values should not be equal", 
                   property.equals(differentValueProperty));
    }
    
    public void testPropertyEquals_DifferentField() throws Throwable {
        TestableProperty property = new TestableProperty(0);
        TestablePropertyWithDifferentField differentFieldProperty = 
            new TestablePropertyWithDifferentField(0);
        
        assertFalse("Properties with different fields should not be equal", 
                   property.equals(differentFieldProperty));
    }
    
    public void testPropertyEquals_DifferentChronology() throws Throwable {
        TestableProperty property = new TestableProperty(0);
        TestablePropertyWithDifferentChronology differentChronoProperty = 
            new TestablePropertyWithDifferentChronology(0);
        
        assertFalse("Properties with different chronologies should not be equal", 
                   property.equals(differentChronoProperty));
    }
    
    public void testPropertyEquals_NonPropertyObjects() throws Throwable {
        TestableProperty property = new TestableProperty(0);
        
        assertFalse("Property should not equal a string", property.equals(""));
        assertFalse("Property should not equal null", property.equals(null));
    }

    //-----------------------------------------------------------------------
    // Test implementations
    //-----------------------------------------------------------------------
    
    /**
     * A concrete implementation of AbstractPartial for testing.
     * Represents a partial with year and month fields.
     */
    static class TestablePartial extends AbstractPartial {
        
        private static final int YEAR_INDEX = 0;
        private static final int MONTH_INDEX = 1;
        private static final int DEFAULT_YEAR = 1970;
        private static final int DEFAULT_MONTH = 1;
        
        private int[] fieldValues = new int[] {DEFAULT_YEAR, DEFAULT_MONTH};
        
        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case YEAR_INDEX:
                    return chrono.year();
                case MONTH_INDEX:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
        }

        public int size() {
            return 2;
        }
        
        public int getValue(int index) {
            if (index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
            return fieldValues[index];
        }

        public void setValue(int index, int value) {
            fieldValues[index] = value;
        }

        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }
    
    /**
     * A testable property implementation for testing AbstractPartialFieldProperty.
     */
    static class TestableProperty extends AbstractPartialFieldProperty {
        private final TestablePartial partial = new TestablePartial();
        private final int fieldIndex;
        
        TestableProperty(int fieldIndex) {
            this.fieldIndex = fieldIndex;
        }
        
        @Override
        public DateTimeField getField() {
            return partial.getField(fieldIndex);
        }
        
        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }
        
        @Override
        public int get() {
            return partial.getValue(fieldIndex);
        }
    }
    
    /**
     * Property that returns a different field than expected.
     */
    static class TestablePropertyWithDifferentField extends TestableProperty {
        TestablePropertyWithDifferentField(int fieldIndex) {
            super(fieldIndex);
        }
        
        @Override
        public DateTimeField getField() {
            // Return hour field instead of the expected field
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }
    
    /**
     * Property that returns a different value.
     */
    static class TestablePropertyWithDifferentValue extends TestableProperty {
        TestablePropertyWithDifferentValue(int fieldIndex) {
            super(fieldIndex);
        }
        
        @Override
        public int get() {
            return 99;
        }
    }
    
    /**
     * Property with a partial that uses a different chronology.
     */
    static class TestablePropertyWithDifferentChronology extends TestableProperty {
        TestablePropertyWithDifferentChronology(int fieldIndex) {
            super(fieldIndex);
        }
        
        @Override
        public ReadablePartial getReadablePartial() {
            return new TestablePartial() {
                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC();
                }
            };
        }
    }
}