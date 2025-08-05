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

import java.util.List;

import org.junit.jupiter.api.Test;

class SegmentConstantPoolArrayCacheTest {

    @Test
    void testMultipleArraysWithMultipleHits() {
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        // Test arrays with overlapping values
        final String[] arrayOne = { "Zero", "Shared", "Two", "Shared", "Shared" };
        final String[] arrayTwo = { "Shared", "One", "Shared", "Shared", "Shared" };

        // Prime the cache with initial lookups
        arrayCache.indexesForArrayKey(arrayOne, "Shared");
        arrayCache.indexesForArrayKey(arrayTwo, "Shared");

        // Verify cache returns correct values for subsequent lookups
        List<Integer> listOne = arrayCache.indexesForArrayKey(arrayOne, "Two");
        List<Integer> listTwo = arrayCache.indexesForArrayKey(arrayTwo, "Shared");

        // Validate arrayOne results for "Two"
        assertEquals(1, listOne.size(), "Should find one occurrence of 'Two'");
        assertEquals(2, listOne.get(0), "'Two' should be at index 2");

        // Validate arrayTwo results for "Shared"
        final int expectedArrayTwoHits = 4;
        assertEquals(expectedArrayTwoHits, listTwo.size(), "Should find four occurrences of 'Shared'");
        assertEquals(List.of(0, 2, 3, 4), listTwo, "Incorrect indices for 'Shared' in arrayTwo");

        // Verify cache returns updated results when switching keys in same array
        listOne = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        assertEquals(3, listOne.size(), "Should find three occurrences of 'Shared'");
        assertEquals(List.of(1, 3, 4), listOne, "Incorrect indices for 'Shared' in arrayOne");

        // Verify non-existent key returns empty list
        final List<Integer> notFoundList = arrayCache.indexesForArrayKey(arrayOne, "Not found");
        assertEquals(0, notFoundList.size(), "Should return empty list for missing key");
    }

    @Test
    void testSingleArrayWithMultipleHits() {
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        final String[] array = { "Zero", "OneThreeFour", "Two", "OneThreeFour", "OneThreeFour" };
        
        final List<Integer> indices = arrayCache.indexesForArrayKey(array, "OneThreeFour");
        
        assertEquals(3, indices.size(), "Should find three occurrences");
        assertEquals(List.of(1, 3, 4), indices, "Incorrect indices for duplicate values");
    }

    @Test
    void testSingleArrayWithUniqueValues() {
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        final String[] array = { "Zero", "One", "Two", "Three", "Four" };
        
        final List<Integer> indices = arrayCache.indexesForArrayKey(array, "Three");
        
        assertEquals(1, indices.size(), "Should find single occurrence");
        assertEquals(3, indices.get(0), "Should find 'Three' at index 3");
    }
}