/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for SegmentConstantPoolArrayCache which caches array lookups to improve performance
 * when searching for string values and their positions in arrays.
 */
class SegmentConstantPoolArrayCacheTest {

    private SegmentConstantPoolArrayCache cache;

    @BeforeEach
    void setUp() {
        cache = new SegmentConstantPoolArrayCache();
    }

    @Test
    void shouldFindSingleOccurrenceInSimpleArray() {
        // Given: A simple array with unique elements
        String[] simpleArray = {"Zero", "One", "Two", "Three", "Four"};
        
        // When: Searching for an element that appears once
        List<Integer> foundIndexes = cache.indexesForArrayKey(simpleArray, "Three");
        
        // Then: Should find exactly one occurrence at the correct index
        assertEquals(1, foundIndexes.size(), "Should find exactly one occurrence");
        assertEquals(3, foundIndexes.get(0).intValue(), "Should find 'Three' at index 3");
    }

    @Test
    void shouldFindMultipleOccurrencesInSingleArray() {
        // Given: An array with duplicate elements
        String[] arrayWithDuplicates = {"Zero", "Duplicate", "Two", "Duplicate", "Duplicate"};
        
        // When: Searching for an element that appears multiple times
        List<Integer> foundIndexes = cache.indexesForArrayKey(arrayWithDuplicates, "Duplicate");
        
        // Then: Should find all occurrences in correct order
        assertEquals(3, foundIndexes.size(), "Should find three occurrences of 'Duplicate'");
        assertEquals(1, foundIndexes.get(0).intValue(), "First occurrence should be at index 1");
        assertEquals(3, foundIndexes.get(1).intValue(), "Second occurrence should be at index 3");
        assertEquals(4, foundIndexes.get(2).intValue(), "Third occurrence should be at index 4");
    }

    @Test
    void shouldHandleMultipleArraysAndCacheCorrectly() {
        // Given: Two different arrays with overlapping content
        String[] firstArray = {"Zero", "Shared", "Two", "Shared", "Shared"};
        String[] secondArray = {"Shared", "One", "Shared", "Shared", "Shared"};

        // When: Performing initial searches to populate cache
        List<Integer> firstArraySharedIndexes = cache.indexesForArrayKey(firstArray, "Shared");
        List<Integer> secondArraySharedIndexes = cache.indexesForArrayKey(secondArray, "Shared");

        // Then: Verify initial search results
        assertSharedElementsInFirstArray(firstArraySharedIndexes);
        assertSharedElementsInSecondArray(secondArraySharedIndexes);

        // When: Searching for different element in first array (tests cache usage)
        List<Integer> firstArrayTwoIndexes = cache.indexesForArrayKey(firstArray, "Two");
        
        // Then: Should find the unique element
        assertEquals(1, firstArrayTwoIndexes.size(), "Should find exactly one occurrence of 'Two'");
        assertEquals(2, firstArrayTwoIndexes.get(0).intValue(), "Should find 'Two' at index 2");

        // When: Re-searching for shared elements (tests cache retrieval)
        List<Integer> cachedFirstArraySharedIndexes = cache.indexesForArrayKey(firstArray, "Shared");
        List<Integer> cachedSecondArraySharedIndexes = cache.indexesForArrayKey(secondArray, "Shared");

        // Then: Results should be consistent with initial search
        assertSharedElementsInFirstArray(cachedFirstArraySharedIndexes);
        assertSharedElementsInSecondArray(cachedSecondArraySharedIndexes);

        // When: Searching for non-existent element
        List<Integer> notFoundIndexes = cache.indexesForArrayKey(firstArray, "NonExistent");
        
        // Then: Should return empty list
        assertTrue(notFoundIndexes.isEmpty(), "Should return empty list for non-existent element");
    }

    /**
     * Verifies that "Shared" elements in the first test array are found at the correct positions.
     */
    private void assertSharedElementsInFirstArray(List<Integer> indexes) {
        assertEquals(3, indexes.size(), "First array should have 3 occurrences of 'Shared'");
        assertEquals(1, indexes.get(0).intValue(), "First 'Shared' should be at index 1");
        assertEquals(3, indexes.get(1).intValue(), "Second 'Shared' should be at index 3");
        assertEquals(4, indexes.get(2).intValue(), "Third 'Shared' should be at index 4");
    }

    /**
     * Verifies that "Shared" elements in the second test array are found at the correct positions.
     */
    private void assertSharedElementsInSecondArray(List<Integer> indexes) {
        assertEquals(4, indexes.size(), "Second array should have 4 occurrences of 'Shared'");
        assertEquals(0, indexes.get(0).intValue(), "First 'Shared' should be at index 0");
        assertEquals(2, indexes.get(1).intValue(), "Second 'Shared' should be at index 2");
        assertEquals(3, indexes.get(2).intValue(), "Third 'Shared' should be at index 3");
        assertEquals(4, indexes.get(3).intValue(), "Fourth 'Shared' should be at index 4");
    }
}