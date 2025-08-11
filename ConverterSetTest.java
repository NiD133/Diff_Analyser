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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is a JUnit test for ConverterSet.
 * Mostly for coverage.
 *
 * @author Stephen Colebourne
 */
public class TestConverterSet {

    private static final Converter c1 = new Converter() {
        public Class<?> getSupportedType() { return Boolean.class; }
    };
    private static final Converter c2 = new Converter() {
        public Class<?> getSupportedType() { return Character.class; }
    };
    private static final Converter c3 = new Converter() {
        public Class<?> getSupportedType() { return Byte.class; }
    };
    private static final Converter c4 = new Converter() {
        public Class<?> getSupportedType() { return Short.class; }
    };
    private static final Converter c4a = new Converter() {
        public Class<?> getSupportedType() { return Short.class; }
    };
    private static final Converter c5 = new Converter() {
        public Class<?> getSupportedType() { return Integer.class; }
    };

    private Converter[] standardConverters;

    @Before
    public void setUp() {
        standardConverters = new Converter[] { c1, c2, c3, c4 };
    }

    //-----------------------------------------------------------------------
    @Test
    public void testClassVisibility() throws Exception {
        Class<?> cls = ConverterSet.class;
        int modifiers = cls.getModifiers();
        
        // Verify class has package-private visibility
        assertEquals("Class should have package-private visibility", 
                     false, Modifier.isPublic(modifiers));
        assertEquals("Class should have package-private visibility", 
                     false, Modifier.isProtected(modifiers));
        assertEquals("Class should have package-private visibility", 
                     false, Modifier.isPrivate(modifiers));
    }

    @Test
    public void testConstructorVisibility() throws Exception {
        Constructor<?>[] constructors = ConverterSet.class.getDeclaredConstructors();
        assertEquals("Should have exactly one constructor", 1, constructors.length);
        
        int modifiers = constructors[0].getModifiers();
        // Verify constructor has package-private visibility
        assertEquals("Constructor should have package-private visibility", 
                     false, Modifier.isPublic(modifiers));
        assertEquals("Constructor should have package-private visibility", 
                     false, Modifier.isProtected(modifiers));
        assertEquals("Constructor should have package-private visibility", 
                     false, Modifier.isPrivate(modifiers));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testSelectWithMultipleTypes_DoesNotChangeSetSize() {
        ConverterSet set = new ConverterSet(standardConverters);
        int initialSize = set.size();

        // Exercise cache with various types
        set.select(Boolean.class);
        set.select(Character.class);
        set.select(Byte.class);
        set.select(Short.class);
        set.select(Integer.class);
        set.select(Long.class);
        set.select(Float.class);
        set.select(Double.class);
        set.select(null);
        set.select(Calendar.class);
        set.select(GregorianCalendar.class);
        set.select(DateTime.class);
        set.select(DateMidnight.class);
        set.select(ReadableInstant.class);
        set.select(ReadableDateTime.class);
        set.select(ReadWritableInstant.class);
        set.select(ReadWritableDateTime.class);
        set.select(DateTime.class);

        assertEquals("Set size should remain unchanged after multiple select calls",
                     initialSize, set.size());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAddNewConverter_AddsToSet() {
        ConverterSet set = new ConverterSet(standardConverters);
        ConverterSet result = set.add(c5, null);
        
        assertEquals("Original set should remain unchanged", 4, set.size());
        assertEquals("New set should have added converter", 5, result.size());
    }

    @Test
    public void testAddExistingConverter_ReturnsOriginalSet() {
        ConverterSet set = new ConverterSet(standardConverters);
        ConverterSet result = set.add(c4, null);
        
        assertSame("Should return same set when adding existing converter", 
                   set, result);
    }

    @Test
    public void testAddEquivalentConverter_ReturnsNewSet() {
        ConverterSet set = new ConverterSet(standardConverters);
        ConverterSet result = set.add(c4a, null);
        
        assertNotSame("Should return new set when adding equivalent converter", 
                      set, result);
        assertEquals("Original set should remain unchanged", 4, set.size());
        assertEquals("New set should have same size", 4, result.size());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testRemoveExistingConverter_RemovesFromSet() {
        ConverterSet set = new ConverterSet(standardConverters);
        ConverterSet result = set.remove(c3, null);
        
        assertEquals("Original set should remain unchanged", 4, set.size());
        assertEquals("New set should have removed converter", 3, result.size());
    }

    @Test
    public void testRemoveMissingConverter_ReturnsOriginalSet() {
        ConverterSet set = new ConverterSet(standardConverters);
        ConverterSet result = set.remove(c5, null);
        
        assertSame("Should return same set when removing missing converter", 
                   set, result);
    }

    //-----------------------------------------------------------------------
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByInvalidHighIndex_ThrowsException() {
        ConverterSet set = new ConverterSet(standardConverters);
        set.remove(200, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByInvalidNegativeIndex_ThrowsException() {
        ConverterSet set = new ConverterSet(standardConverters);
        set.remove(-1, null);
    }
}