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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * This class is a JUnit test for ConverterSet.
 */
public class TestConverterSet {

    // A helper class to create stub converters cleanly.
    private static class TestConverter implements Converter {
        private final Class<?> iType;

        TestConverter(Class<?> type) {
            this.iType = type;
        }

        @Override
        public Class<?> getSupportedType() {
            return iType;
        }
    }

    // Descriptive constants for the test converters.
    private static final Converter BOOLEAN_CONVERTER = new TestConverter(Boolean.class);
    private static final Converter CHARACTER_CONVERTER = new TestConverter(Character.class);
    private static final Converter BYTE_CONVERTER = new TestConverter(Byte.class);
    private static final Converter SHORT_CONVERTER = new TestConverter(Short.class);
    private static final Converter ANOTHER_SHORT_CONVERTER = new TestConverter(Short.class);
    private static final Converter INTEGER_CONVERTER = new TestConverter(Integer.class);
    private static final Converter CALENDAR_CONVERTER = new TestConverter(Calendar.class);

    private ConverterSet initialSet;

    @Before
    public void setUp() {
        // This setup is run before each test.
        Converter[] initialConverters = {
            BOOLEAN_CONVERTER, CHARACTER_CONVERTER, BYTE_CONVERTER, SHORT_CONVERTER
        };
        initialSet = new ConverterSet(initialConverters);
    }

    @Test
    public void testClassAndConstructorArePackagePrivate() throws Exception {
        // Ensures the class and its constructor are not publicly accessible,
        // which is a deliberate design choice.
        Class<?> cls = ConverterSet.class;
        assertFalse("Class should be package-private", Modifier.isPublic(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructors()[0];
        assertEquals("Should have exactly one constructor", 1, cls.getDeclaredConstructors().length);
        assertFalse("Constructor should be package-private", Modifier.isPublic(constructor.getModifiers()));
    }

    //-----------------------------------------------------------------------
    // Test `select(Class)`
    //-----------------------------------------------------------------------

    @Test
    public void select_shouldReturnCorrectConverterForSupportedType() {
        assertSame(BOOLEAN_CONVERTER, initialSet.select(Boolean.class));
        assertSame(CHARACTER_CONVERTER, initialSet.select(Character.class));
        assertSame(BYTE_CONVERTER, initialSet.select(Byte.class));
        assertSame(SHORT_CONVERTER, initialSet.select(Short.class));
    }

    @Test
    public void select_shouldReturnNullForUnsupportedType() {
        assertNull(initialSet.select(Integer.class));
        assertNull(initialSet.select(String.class));
    }

    @Test
    public void select_shouldReturnConverterForSubType() {
        ConverterSet set = new ConverterSet(new Converter[]{CALENDAR_CONVERTER});
        // When asking for a subclass (GregorianCalendar), the converter for the
        // superclass (Calendar) should be returned.
        Converter result = set.select(GregorianCalendar.class);
        assertSame(CALENDAR_CONVERTER, result);
    }

    @Test
    public void select_shouldNotModifyOriginalSet() {
        // The select method should be purely a query and not alter the set's state.
        int originalSize = initialSet.size();
        initialSet.select(Boolean.class);
        initialSet.select(Integer.class);
        initialSet.select(DateTime.class);
        initialSet.select(null);
        assertEquals("The set size should not change after calling select", originalSize, initialSet.size());
    }

    //-----------------------------------------------------------------------
    // Test `add(Converter, ...)`
    //-----------------------------------------------------------------------

    @Test
    public void add_whenAddingNewConverter_returnsNewSetWithConverter() {
        // Act
        ConverterSet resultSet = initialSet.add(INTEGER_CONVERTER, null);

        // Assert
        assertNotSame("A new ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals("Original set size should not change", 4, initialSet.size());
        assertEquals("New set should have one more converter", 5, resultSet.size());
        assertSame("New set should contain the added converter", INTEGER_CONVERTER, resultSet.select(Integer.class));
    }

    @Test
    public void add_whenAddingExistingConverterInstance_returnsSameSet() {
        // Act: try to add a converter that is already in the set
        ConverterSet resultSet = initialSet.add(SHORT_CONVERTER, null);

        // Assert
        assertSame("The same ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals(4, resultSet.size());
    }

    @Test
    public void add_whenAddingConverterForExistingType_replacesOldConverter() {
        // Act: Add a different converter instance for an already supported type (Short.class)
        ConverterSet resultSet = initialSet.add(ANOTHER_SHORT_CONVERTER, null);

        // Assert
        assertNotSame("A new ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals("Set size should remain the same", 4, resultSet.size());
        assertSame("The new converter should replace the old one", ANOTHER_SHORT_CONVERTER, resultSet.select(Short.class));
    }

    //-----------------------------------------------------------------------
    // Test `remove(Converter, ...)`
    //-----------------------------------------------------------------------

    @Test
    public void removeByConverter_whenRemovingExistingConverter_returnsNewSetWithoutConverter() {
        // Arrange
        assertSame("Sanity check: converter should be present before removal", BYTE_CONVERTER, initialSet.select(Byte.class));

        // Act
        ConverterSet resultSet = initialSet.remove(BYTE_CONVERTER, null);

        // Assert
        assertNotSame("A new ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals("Original set size should not change", 4, initialSet.size());
        assertEquals("New set should have one less converter", 3, resultSet.size());
        assertNull("Converter should be absent in the new set", resultSet.select(Byte.class));
    }

    @Test
    public void removeByConverter_whenRemovingNonExistentConverter_returnsSameSet() {
        // Act
        ConverterSet resultSet = initialSet.remove(INTEGER_CONVERTER, null);

        // Assert
        assertSame("The same ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals(4, resultSet.size());
    }

    //-----------------------------------------------------------------------
    // Test `remove(int, ...)`
    //-----------------------------------------------------------------------

    @Test
    public void removeByIndex_whenIndexIsValid_returnsNewSetWithoutConverter() {
        // Arrange: The 3rd converter in the initial array is BYTE_CONVERTER (index 2)
        int indexToRemove = 2;
        assertSame("Sanity check: converter should be present before removal", BYTE_CONVERTER, initialSet.select(Byte.class));

        // Act
        ConverterSet resultSet = initialSet.remove(indexToRemove, null);

        // Assert
        assertNotSame("A new ConverterSet instance should be returned", initialSet, resultSet);
        assertEquals("New set should have one less converter", 3, resultSet.size());
        assertNull("Converter should be absent in the new set", resultSet.select(Byte.class));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_whenIndexIsNegative_throwsException() {
        initialSet.remove(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_whenIndexIsTooLarge_throwsException() {
        initialSet.remove(initialSet.size(), null); // size() is an invalid index
    }
}