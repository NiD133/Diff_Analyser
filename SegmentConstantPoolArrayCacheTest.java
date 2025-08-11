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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
@DisplayName("SegmentConstantPoolArrayCache")
class SegmentConstantPoolArrayCacheTest {

    private SegmentConstantPoolArrayCache arrayCache;

    @BeforeEach
    void setUp() {
        arrayCache = new SegmentConstantPoolArrayCache();
    }

    @Test
    @DisplayName("Should find the single index of a unique element")
    void shouldFindSingleIndexForUniqueElement() {
        // Arrange
        final String[] arrayWithUniqueElements = { "Zero", "One", "Two", "Three", "Four" };
        final String keyToFind = "Three";
        final List<Integer> expectedIndices = List.of(3);

        // Act
        final List<Integer> actualIndices = arrayCache.indexesForArrayKey(arrayWithUniqueElements, keyToFind);

        // Assert
        assertEquals(expectedIndices, actualIndices);
    }

    @Test
    @DisplayName("Should find all indices of a duplicated element")
    void shouldFindAllIndicesForDuplicateElement() {
        // Arrange
        final String[] arrayWithDuplicates = { "Zero", "OneThreeFour", "Two", "OneThreeFour", "OneThreeFour" };
        final String keyToFind = "OneThreeFour";
        final List<Integer> expectedIndices = List.of(1, 3, 4);

        // Act
        final List<Integer> actualIndices = arrayCache.indexesForArrayKey(arrayWithDuplicates, keyToFind);

        // Assert
        assertEquals(expectedIndices, actualIndices);
    }

    @Test
    @DisplayName("Should return an empty list when an element is not found")
    void shouldReturnEmptyListForNotFoundElement() {
        // Arrange
        final String[] array = { "Zero", "One", "Two" };
        final String keyToFind = "NotFound";

        // Act
        final List<Integer> actualIndices = arrayCache.indexesForArrayKey(array, keyToFind);

        // Assert
        assertTrue(actualIndices.isEmpty());
    }

    @Test
    @DisplayName("Should cache lookups and handle multiple arrays independently")
    void shouldCacheLookupsForMultipleArraysIndependently() {
        // Arrange: Two distinct array instances with some overlapping values.
        final String[] arrayOne = { "Zero", "Shared", "Two", "Shared", "Shared" };
        final String[] arrayTwo = { "Shared", "One", "Shared", "Shared", "Shared" };

        // Act & Assert: First lookup on arrayOne. This should build its cache.
        final List<Integer> sharedIndicesInArrayOne = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        assertEquals(List.of(1, 3, 4), sharedIndicesInArrayOne, "First lookup for 'Shared' in arrayOne should be correct.");

        // Act & Assert: First lookup on arrayTwo. This should build its cache, independent of arrayOne.
        final List<Integer> sharedIndicesInArrayTwo = arrayCache.indexesForArrayKey(arrayTwo, "Shared");
        assertEquals(List.of(0, 2, 3, 4), sharedIndicesInArrayTwo, "First lookup for 'Shared' in arrayTwo should be correct.");

        // Act & Assert: Second lookup on arrayOne with a different key, using the existing cache.
        final List<Integer> twoIndicesInArrayOne = arrayCache.indexesForArrayKey(arrayOne, "Two");
        assertEquals(List.of(2), twoIndicesInArrayOne, "Cached lookup for 'Two' in arrayOne should be correct.");

        // Act & Assert: Re-query the first key on arrayOne to ensure the cache is stable.
        final List<Integer> sharedIndicesInArrayOneAgain = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        assertEquals(sharedIndicesInArrayOne, sharedIndicesInArrayOneAgain, "Second lookup for 'Shared' in arrayOne should return the same cached result.");

        // Act & Assert: Re-query arrayTwo to ensure its cache was unaffected by lookups on arrayOne.
        final List<Integer> sharedIndicesInArrayTwoAgain = arrayCache.indexesForArrayKey(arrayTwo, "Shared");
        assertEquals(sharedIndicesInArrayTwo, sharedIndicesInArrayTwoAgain, "Second lookup for 'Shared' in arrayTwo should return the same cached result.");
    }
}