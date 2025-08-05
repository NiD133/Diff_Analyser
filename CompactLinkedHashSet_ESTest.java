/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.Spliterator;

/**
 * This test suite contains refactored tests for {@link CompactLinkedHashSet}.
 * The original tests were auto-generated and have been improved for clarity,
 * maintainability, and to better reflect real-world usage of the class.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void create_fromEmptyCollection_shouldCreateEmptySet() {
        // Arrange
        Collection<String> emptyCollection = Collections.emptyList();

        // Act
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(emptyCollection);

        // Assert
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void create_fromCollection_shouldContainAllElementsInOrder() {
        // Arrange
        Collection<String> elements = Arrays.asList("first", "second", "third");

        // Act
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(elements);

        // Assert
        assertEquals(3, set.size());
        assertArrayEquals(new Object[]{"first", "second", "third"}, set.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void create_withNullCollection_shouldThrowException() {
        CompactLinkedHashSet.create((Collection<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void create_withNullVarArgs_shouldThrowException() {
        CompactLinkedHashSet.create((String[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithExpectedSize_withNegativeSize_shouldThrowException() {
        CompactLinkedHashSet.createWithExpectedSize(-1);
    }

    @Test
    public void clear_onNonEmptySet_shouldMakeItEmpty() {
        // Arrange
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(10);
        set.add(1);
        set.add(2);
        assertFalse(set.isEmpty());

        // Act
        set.clear();

        // Assert
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains(1));
    }

    @Test
    public void toArray_whenSetIsEmpty_shouldReturnEmptyObjectArray() {
        // Arrange
        CompactLinkedHashSet<String> emptySet = CompactLinkedHashSet.create();

        // Act
        Object[] result = emptySet.toArray();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void toArray_onNonEmptySet_shouldReturnArrayWithAllElementsInInsertionOrder() {
        // Arrange
        Collection<String> elements = Arrays.asList("first", "second", "third");
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(elements);

        // Act
        Object[] result = set.toArray();

        // Assert
        assertArrayEquals(new Object[]{"first", "second", "third"}, result);
    }

    @Test
    public void toArray_whenSetIsEmptyAndGivenArrayIsEmpty_shouldReturnSameArray() {
        // Arrange
        CompactLinkedHashSet<Integer> emptySet = CompactLinkedHashSet.create();
        Integer[] emptyArray = new Integer[0];

        // Act
        Integer[] result = emptySet.toArray(emptyArray);

        // Assert
        assertSame("For an empty set, toArray should return the provided empty array instance", emptyArray, result);
    }

    @Test
    public void toArray_whenSetIsEmptyAndGivenArrayIsLarger_shouldReturnSameArrayWithNullTerminator() {
        // Arrange
        CompactLinkedHashSet<String> emptySet = CompactLinkedHashSet.create();
        String[] array = new String[] {"a", "b", "c"};

        // Act
        String[] result = emptySet.toArray(array);

        // Assert
        assertSame(array, result);
        assertNull("The element at index 0 should be set to null to mark the end", result[0]);
        assertEquals("The element at index 1 should be unchanged", "b", result[1]);
    }

    @Test
    public void spliterator_whenSetIsEmpty_shouldReturnEmptySpliterator() {
        // Arrange
        CompactLinkedHashSet<Integer> emptySet = CompactLinkedHashSet.create();

        // Act
        Spliterator<Integer> spliterator = emptySet.spliterator();

        // Assert
        assertNotNull(spliterator);
        assertEquals(0, spliterator.estimateSize());
        assertTrue(spliterator.hasCharacteristics(Spliterator.DISTINCT));
        assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED));
    }

    @Test
    public void retainAll_withEmptyCollection_shouldClearTheSet() {
        // Arrange
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        set.add("a");
        set.add("b");

        // Act
        boolean changed = set.retainAll(Collections.emptySet());

        // Assert
        assertTrue("retainAll should return true as the set was modified", changed);
        assertTrue("The set should be empty after retaining nothing", set.isEmpty());
    }

    @Test
    public void convertToHashFloodingResistantImplementation_onNonEmptySet_shouldReturnEquivalentSet() {
        // Arrange
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        set.add("a");
        set.add(null);
        set.add("b");

        // Act
        Set<String> resistantSet = set.convertToHashFloodingResistantImplementation();

        // Assert
        assertEquals("The returned set should have the same size", set.size(), resistantSet.size());
        assertTrue("The returned set should contain all original elements", resistantSet.containsAll(set));
    }

    // --- Tests for internal (package-private) methods ---
    // These tests verify boundary conditions of internal helpers.

    @Test
    public void firstEntryIndex_whenSetIsEmpty_shouldReturnEndpoint() {
        // Arrange
        CompactLinkedHashSet<Object> emptySet = CompactLinkedHashSet.create();
        // From the implementation, ENDPOINT is -2, indicating no entries.
        final int ENDPOINT = -2;

        // Act
        int firstIndex = emptySet.firstEntryIndex();

        // Assert
        assertEquals(ENDPOINT, firstIndex);
    }

    @Test
    public void firstEntryIndex_whenSetContainsOneElement_shouldReturnZero() {
        // Arrange
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(Collections.singleton(123));

        // Act
        int firstIndex = set.firstEntryIndex();

        // Assert
        assertEquals(0, firstIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void init_withNegativeSize_shouldThrowException() {
        // Arrange
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();

        // Act
        set.init(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void allocArrays_whenAlreadyAllocated_shouldThrowException() {
        // Arrange
        // create() calls init() which calls allocArrays().
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singleton("a"));

        // Act: Call internal method a second time.
        set.allocArrays();
    }

    @Test(expected = NegativeArraySizeException.class)
    public void resizeEntries_withNegativeCapacity_shouldThrowException() {
        // Arrange
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singletonList("a"));

        // Act
        set.resizeEntries(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void moveLastEntry_withInvalidDestinationIndex_shouldThrowException() {
        // Arrange
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(Collections.singleton("a"));

        // Act: Call internal method with an out-of-bounds index.
        set.moveLastEntry(1, 32); // Valid entry index is 0.
    }
}