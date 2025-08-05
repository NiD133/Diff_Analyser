/*
 *  Copyright 2001-2009 Stephen Colebourne
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

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.convert.CalendarConverter;
import org.joda.time.convert.Converter;
import org.joda.time.convert.ConverterSet;
import org.joda.time.convert.LongConverter;
import org.joda.time.convert.NullConverter;
import org.joda.time.convert.ReadableInstantConverter;
import org.joda.time.convert.ReadableIntervalConverter;
import org.joda.time.convert.StringConverter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ConverterSet_ESTest_scaffolding {

    // --- Test Cases for the 'add' method ---

    @Test
    public void add_toEmptySet_returnsNewSetWithConverter() {
        // Arrange
        Converter[] initialConverters = new Converter[0];
        ConverterSet emptySet = new ConverterSet(initialConverters);
        Converter converterToAdd = ReadableInstantConverter.INSTANCE;

        // Act
        ConverterSet newSet = emptySet.add(converterToAdd, null);

        // Assert
        assertNotSame("A new ConverterSet instance should be returned", emptySet, newSet);
        assertEquals("The new set should have one converter", 1, newSet.size());
    }

    @Test
    public void add_whenConverterIsAlreadyPresent_returnsSameSetInstance() {
        // Arrange
        Converter existingConverter = StringConverter.INSTANCE;
        Converter[] initialConverters = new Converter[]{existingConverter};
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act
        ConverterSet resultSet = initialSet.add(existingConverter, null);

        // Assert
        assertSame("Should return the same instance if the converter already exists", initialSet, resultSet);
    }

    @Test
    public void add_whenReplacingConverterForSameType_returnsNewSet() {
        // Arrange
        Converter[] initialConverters = new Converter[]{ReadableInstantConverter.INSTANCE};
        ConverterSet initialSet = new ConverterSet(initialConverters);
        // A new instance for a type that is already handled
        Converter newConverterForSameType = new ReadableInstantConverter();

        // Act
        ConverterSet newSet = initialSet.add(newConverterForSameType, null);

        // Assert
        assertNotSame("A new set should be returned when a converter is replaced", initialSet, newSet);
        assertEquals(1, newSet.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void add_withNullConverter_throwsNullPointerException() {
        // Arrange
        Converter[] converters = new Converter[]{LongConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);
        
        // Act
        converterSet.add(null, null);
    }

    // --- Test Cases for the 'remove' method ---

    @Test
    public void removeByObject_whenConverterExists_returnsNewSetWithoutConverter() {
        // Arrange
        Converter converterToRemove = StringConverter.INSTANCE;
        Converter[] initialConverters = new Converter[]{converterToRemove};
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act
        ConverterSet newSet = initialSet.remove(converterToRemove, null);

        // Assert
        assertNotSame("A new set should be returned after removing a converter", initialSet, newSet);
        assertEquals("The new set should be empty", 0, newSet.size());
    }

    @Test
    public void removeByObject_whenConverterIsNotPresent_returnsSameSetInstance() {
        // Arrange
        Converter[] initialConverters = new Converter[0];
        ConverterSet emptySet = new ConverterSet(initialConverters);
        Converter converterToRemove = ReadableInstantConverter.INSTANCE;

        // Act
        ConverterSet resultSet = emptySet.remove(converterToRemove, null);

        // Assert
        assertSame("Should return the same instance if the converter to remove is not found", emptySet, resultSet);
    }

    @Test
    public void removeByIndex_whenConverterExists_returnsNewSetWithoutConverter() {
        // Arrange
        Converter[] initialConverters = new Converter[]{StringConverter.INSTANCE, LongConverter.INSTANCE};
        ConverterSet initialSet = new ConverterSet(initialConverters);

        // Act
        ConverterSet newSet = initialSet.remove(0, null);

        // Assert
        assertNotSame("A new set should be returned after removing a converter", initialSet, newSet);
        assertEquals("The new set should have one less converter", 1, newSet.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_withIndexOutOfBounds_throwsException() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);
        int invalidIndex = 1;

        // Act
        converterSet.remove(invalidIndex, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndex_withNegativeIndex_throwsException() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);
        int invalidIndex = -1;

        // Act
        converterSet.remove(invalidIndex, null);
    }

    // --- Test Cases for the 'select' method ---

    @Test
    public void select_whenConverterForTypeExists_returnsConverter() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE, LongConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);

        // Act
        Converter result = converterSet.select(Long.class);

        // Assert
        assertNotNull("Should find a converter for Long", result);
        assertSame("Should return the LongConverter instance", LongConverter.INSTANCE, result);
    }

    @Test
    public void select_whenNoConverterForTypeExists_returnsNull() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE, LongConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);

        // Act
        Converter result = converterSet.select(ReadableIntervalConverter.class);

        // Assert
        assertNull("Should return null if no suitable converter is found", result);
    }

    @Test
    public void select_withNullType_returnsNull() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);

        // Act
        Converter result = converterSet.select(null);

        // Assert
        assertNull("Selecting for a null type should return null", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void select_whenSetContainsNullConverter_throwsNullPointerException() {
        // Arrange
        Converter[] convertersWithNull = new Converter[] { null };
        ConverterSet converterSet = new ConverterSet(convertersWithNull);

        // Act
        // The select method may fail if it doesn't handle null entries in the converter array.
        converterSet.select(String.class);
    }

    // --- Test Cases for 'size' and 'copyInto' methods ---

    @Test
    public void size_returnsCorrectNumberOfConverters() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE, NullConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);

        // Act
        int size = converterSet.size();

        // Assert
        assertEquals(2, size);
    }
    
    @Test
    public void size_onEmptySet_returnsZero() {
        // Arrange
        Converter[] converters = new Converter[0];
        ConverterSet converterSet = new ConverterSet(converters);

        // Act
        int size = converterSet.size();

        // Assert
        assertEquals(0, size);
    }

    @Test
    public void copyInto_withSufficientlySizedArray_copiesAllConverters() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE, CalendarConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);
        Converter[] destinationArray = new Converter[2];

        // Act
        converterSet.copyInto(destinationArray);

        // Assert
        assertArrayEquals("The destination array should contain the same converters", converters, destinationArray);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void copyInto_withInsufficientlySizedArray_throwsArrayIndexOutOfBoundsException() {
        // Arrange
        Converter[] converters = new Converter[]{StringConverter.INSTANCE, CalendarConverter.INSTANCE};
        ConverterSet converterSet = new ConverterSet(converters);
        Converter[] destinationArray = new Converter[1]; // Array is too small

        // Act
        converterSet.copyInto(destinationArray);
    }
}