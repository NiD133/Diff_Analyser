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
package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;

/**
 * Test suite for ConverterSet class.
 * Tests the functionality of adding, removing, and selecting converters from a set.
 *
 * @author Stephen Colebourne
 */
public class TestConverterSet extends TestCase {

    // Test converters for different primitive wrapper types
    private static final Converter BOOLEAN_CONVERTER = createTestConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = createTestConverter(Character.class);
    private static final Converter BYTE_CONVERTER = createTestConverter(Byte.class);
    private static final Converter SHORT_CONVERTER = createTestConverter(Short.class);
    private static final Converter DUPLICATE_SHORT_CONVERTER = createTestConverter(Short.class);
    private static final Converter INTEGER_CONVERTER = createTestConverter(Integer.class);
    
    /**
     * Creates a test converter for the specified type.
     */
    private static Converter createTestConverter(final Class<?> supportedType) {
        return new Converter() {
            public Class<?> getSupportedType() {
                return supportedType;
            }
        };
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    public TestConverterSet(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    // Class structure tests
    //-----------------------------------------------------------------------
    
    /**
     * Tests that ConverterSet has package-private visibility and constructor.
     */
    public void testClassVisibilityIsPackagePrivate() throws Exception {
        Class<?> converterSetClass = ConverterSet.class;
        
        // Verify class is package-private (not public, protected, or private)
        int classModifiers = converterSetClass.getModifiers();
        assertFalse("Class should not be public", Modifier.isPublic(classModifiers));
        assertFalse("Class should not be protected", Modifier.isProtected(classModifiers));
        assertFalse("Class should not be private", Modifier.isPrivate(classModifiers));
        
        // Verify single package-private constructor exists
        Constructor<?>[] constructors = converterSetClass.getDeclaredConstructors();
        assertEquals("Should have exactly one constructor", 1, constructors.length);
        
        Constructor<?> constructor = constructors[0];
        int constructorModifiers = constructor.getModifiers();
        assertFalse("Constructor should not be public", Modifier.isPublic(constructorModifiers));
        assertFalse("Constructor should not be protected", Modifier.isProtected(constructorModifiers));
        assertFalse("Constructor should not be private", Modifier.isPrivate(constructorModifiers));
    }

    //-----------------------------------------------------------------------
    // Hashtable performance tests
    //-----------------------------------------------------------------------
    
    /**
     * Tests that the internal hashtable can handle many select operations efficiently.
     * This test exercises the internal caching mechanism by performing multiple
     * select operations on various types.
     */
    public void testHashtablePerformanceWithManySelects() {
        // Given: A converter set with multiple converters
        Converter[] testConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet converterSet = new ConverterSet(testConverters);
        
        // When: Performing many select operations on different types
        converterSet.select(Boolean.class);      // Exact match
        converterSet.select(Character.class);    // Exact match
        converterSet.select(Byte.class);         // Exact match
        converterSet.select(Short.class);        // Exact match
        converterSet.select(Integer.class);      // No match
        converterSet.select(Long.class);         // No match
        converterSet.select(Float.class);        // No match
        converterSet.select(Double.class);       // No match
        converterSet.select(null);               // Null type
        
        // Test with various Joda-Time and Java types
        converterSet.select(Calendar.class);
        converterSet.select(GregorianCalendar.class);
        converterSet.select(DateTime.class);
        converterSet.select(DateMidnight.class);
        converterSet.select(ReadableInstant.class);
        converterSet.select(ReadableDateTime.class);
        converterSet.select(ReadWritableInstant.class);  // 16th operation - triggers hashtable resize
        converterSet.select(ReadWritableDateTime.class);
        converterSet.select(DateTime.class);             // Duplicate select
        
        // Then: The converter set should maintain its original size
        assertEquals("Converter set size should remain unchanged", 4, converterSet.size());
    }

    //-----------------------------------------------------------------------
    // Add operation tests
    //-----------------------------------------------------------------------
    
    /**
     * Tests adding a new converter that doesn't exist in the set.
     */
    public void testAddNewConverter() {
        // Given: A converter set without INTEGER_CONVERTER
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        
        // When: Adding a new converter
        ConverterSet resultSet = originalSet.add(INTEGER_CONVERTER, null);
        
        // Then: A new set should be created with increased size
        assertEquals("Original set should remain unchanged", 4, originalSet.size());
        assertEquals("Result set should have one more converter", 5, resultSet.size());
    }

    /**
     * Tests adding a converter that already exists (same instance).
     */
    public void testAddExistingConverterSameInstance() {
        // Given: A converter set that already contains SHORT_CONVERTER
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        
        // When: Adding the same converter instance
        ConverterSet resultSet = originalSet.add(SHORT_CONVERTER, null);
        
        // Then: The same set instance should be returned (no change needed)
        assertSame("Should return the same set instance when adding existing converter", 
                   originalSet, resultSet);
    }

    /**
     * Tests adding a converter that supports the same type as an existing one (different instance).
     */
    public void testAddConverterWithSameTypeButDifferentInstance() {
        // Given: A converter set with SHORT_CONVERTER
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        
        // When: Adding a different converter instance for the same type
        ConverterSet resultSet = originalSet.add(DUPLICATE_SHORT_CONVERTER, null);
        
        // Then: A new set should be created (replacement occurred)
        assertNotSame("Should create a new set when replacing converter", originalSet, resultSet);
        assertEquals("Original set should remain unchanged", 4, originalSet.size());
        assertEquals("Result set should have same size (replacement, not addition)", 4, resultSet.size());
    }

    //-----------------------------------------------------------------------
    // Remove operation tests
    //-----------------------------------------------------------------------
    
    /**
     * Tests removing an existing converter from the set.
     */
    public void testRemoveExistingConverter() {
        // Given: A converter set containing BYTE_CONVERTER
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        
        // When: Removing an existing converter
        ConverterSet resultSet = originalSet.remove(BYTE_CONVERTER, null);
        
        // Then: A new set should be created with reduced size
        assertEquals("Original set should remain unchanged", 4, originalSet.size());
        assertEquals("Result set should have one less converter", 3, resultSet.size());
    }

    /**
     * Tests removing a converter that doesn't exist in the set.
     */
    public void testRemoveNonExistentConverter() {
        // Given: A converter set that doesn't contain INTEGER_CONVERTER
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet originalSet = new ConverterSet(initialConverters);
        
        // When: Attempting to remove a non-existent converter
        ConverterSet resultSet = originalSet.remove(INTEGER_CONVERTER, null);
        
        // Then: The same set instance should be returned (no change needed)
        assertSame("Should return the same set when removing non-existent converter", 
                   originalSet, resultSet);
    }

    //-----------------------------------------------------------------------
    // Remove by index tests
    //-----------------------------------------------------------------------
    
    /**
     * Tests removing a converter by an invalid high index.
     */
    public void testRemoveByIndexTooHigh() {
        // Given: A converter set with 4 elements (valid indices 0-3)
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet converterSet = new ConverterSet(initialConverters);
        
        // When/Then: Attempting to remove by index 200 should throw exception
        try {
            converterSet.remove(200, null);
            fail("Should throw IndexOutOfBoundsException for index too high");
        } catch (IndexOutOfBoundsException expected) {
            // Expected behavior
        }
        
        // Verify set remains unchanged
        assertEquals("Set size should remain unchanged after failed removal", 4, converterSet.size());
    }

    /**
     * Tests removing a converter by a negative index.
     */
    public void testRemoveByNegativeIndex() {
        // Given: A converter set with 4 elements
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        ConverterSet converterSet = new ConverterSet(initialConverters);
        
        // When/Then: Attempting to remove by negative index should throw exception
        try {
            converterSet.remove(-1, null);
            fail("Should throw IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // Expected behavior
        }
        
        // Verify set remains unchanged
        assertEquals("Set size should remain unchanged after failed removal", 4, converterSet.size());
    }
}