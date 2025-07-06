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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This class is a JUnit test for ConverterSet.
 *
 * @author Stephen Colebourne
 */
@RunWith(JUnit4.class)
public class TestConverterSet {

    private Converter[] converters;
    private Converter[] duplicateConverters;

    @Before
    public void setUp() {
        converters = new Converter[] {
                getConverter(Boolean.class),
                getConverter(Character.class),
                getConverter(Byte.class),
                getConverter(Short.class),
        };

        duplicateConverters = new Converter[] {
                getConverter(Boolean.class),
                getConverter(Character.class),
                getConverter(Byte.class),
                getConverter(Short.class),
                getConverter(Integer.class)
        };
    }

    @Test
    public void testClass() throws Exception {
        // Test class access modifier
        Class<?> cls = ConverterSet.class;
        assertNotPublic(cls);
        assertNotProtected(cls);
        assertNotPrivate(cls);

        // Test constructor access modifier
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor<?> con = cls.getDeclaredConstructors()[0];
        assertNotPublic(con);
        assertNotProtected(con);
        assertNotPrivate(con);
    }

    @Test
    public void testBigHashtable() {
        // Test large number of select operations
        ConverterSet set = new ConverterSet(converters);
        selectMultipleTypes(set);
        assertEquals(4, set.size());
    }

    @Test
    public void testAddNullRemoved1() {
        // Test adding a new converter
        ConverterSet set = new ConverterSet(converters);
        Converter[] removed = new Converter[1];
        ConverterSet result = set.add(getConverter(Integer.class), removed);
        assertEquals(4, set.size());
        assertEquals(5, result.size());
    }

    @Test
    public void testAddNullRemoved2() {
        // Test adding a duplicate converter
        ConverterSet set = new ConverterSet(converters);
        Converter[] removed = new Converter[1];
        ConverterSet result = set.add(getConverter(Short.class), removed);
        assertSame(set, result);
    }

    @Test
    public void testAddNullRemoved3() {
        // Test adding another instance of the same converter
        ConverterSet set = new ConverterSet(converters);
        Converter[] removed = new Converter[1];
        ConverterSet result = set.add(getConverter(Short.class), removed);
        assertNotSame(set, result);
        assertEquals(4, set.size());
        assertEquals(4, result.size());
    }

    @Test
    public void testRemoveNullRemoved1() {
        // Test removing a converter
        ConverterSet set = new ConverterSet(converters);
        Converter[] removed = new Converter[1];
        ConverterSet result = set.remove(getConverter(Byte.class), removed);
        assertEquals(4, set.size());
        assertEquals(3, result.size());
    }

    @Test
    public void testRemoveNullRemoved2() {
        // Test removing a non-existent converter
        ConverterSet set = new ConverterSet(converters);
        Converter[] removed = new Converter[1];
        ConverterSet result = set.remove(getConverter(Integer.class), removed);
        assertSame(set, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveBadIndex1() {
        // Test removing with invalid index
        ConverterSet set = new ConverterSet(converters);
        set.remove(200, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveBadIndex2() {
        // Test removing with invalid index
        ConverterSet set = new ConverterSet(converters);
        set.remove(-1, null);
    }

    private Converter getConverter(Class<?> type) {
        return new Converter() {
            @Override
            public Class<?> getSupportedType() {
                return type;
            }
        };
    }

    private void selectMultipleTypes(ConverterSet set) {
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
    }

    private void assertNotPublic(Class<?> cls) {
        assertEquals(false, Modifier.isPublic(cls.getModifiers()));
    }

    private void assertNotPublic(Constructor<?> con) {
        assertEquals(false, Modifier.isPublic(con.getModifiers()));
    }

    private void assertNotProtected(Class<?> cls) {
        assertEquals(false, Modifier.isProtected(cls.getModifiers()));
    }

    private void assertNotProtected(Constructor<?> con) {
        assertEquals(false, Modifier.isProtected(con.getModifiers()));
    }

    private void assertNotPrivate(Class<?> cls) {
        assertEquals(false, Modifier.isPrivate(cls.getModifiers()));
    }

    private void assertNotPrivate(Constructor<?> con) {
        assertEquals(false, Modifier.isPrivate(con.getModifiers()));
    }
}