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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for SegmentConstantPoolArrayCache that focus on:
 * - returning all indices for a given key
 * - preserving index order
 * - returning an empty list when the key is missing
 * - reusing cached results across multiple arrays and queries
 */
class SegmentConstantPoolArrayCacheTest {

    private static final String KEY_SHARED = "Shared";
    private static final String KEY_NOT_FOUND = "Not found";

    private static List<Integer> indices(final Integer... positions) {
        return Arrays.asList(positions);
    }

    @Test
    void testIndexesForArrayKey_singleArray_singleHit() {
        final SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        final String[] array = { "Zero", "One", "Two", "Three", "Four" };

        final List<Integer> result = cache.indexesForArrayKey(array, "Three");

        assertEquals(indices(3), result, "Expected exactly one hit at index 3");
    }

    @Test
    void testIndexesForArrayKey_singleArray_multipleHits() {
        final SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        final String[] array = { "Zero", "OneThreeFour", "Two", "OneThreeFour", "OneThreeFour" };

        final List<Integer> result = cache.indexesForArrayKey(array, "OneThreeFour");

        assertEquals(indices(1, 3, 4), result, "Expected three hits at indices 1, 3, 4 in ascending order");
    }

    @Test
    void testIndexesForArrayKey_multipleArrays_cacheReuseAndOrder() {
        final SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        final String[] arrayOne = { "Zero", KEY_SHARED, "Two", KEY_SHARED, KEY_SHARED };
        final String[] arrayTwo = { KEY_SHARED, "One", KEY_SHARED, KEY_SHARED, KEY_SHARED };

        // Warm up the cache with initial queries on both arrays
        final List<Integer> sharedInOneFirstPass = cache.indexesForArrayKey(arrayOne, KEY_SHARED);
        final List<Integer> sharedInTwoFirstPass = cache.indexesForArrayKey(arrayTwo, KEY_SHARED);
        assertEquals(indices(1, 3, 4), sharedInOneFirstPass, "Warm-up: indices for 'Shared' in arrayOne");
        assertEquals(indices(0, 2, 3, 4), sharedInTwoFirstPass, "Warm-up: indices for 'Shared' in arrayTwo");

        // Access a different key on arrayOne to ensure cache works across keys
        final List<Integer> twoInOne = cache.indexesForArrayKey(arrayOne, "Two");
        assertEquals(indices(2), twoInOne, "Expected a single hit for 'Two' at index 2");

        // Re-query to ensure we read from cache and ordering is preserved
        final List<Integer> sharedInOne = cache.indexesForArrayKey(arrayOne, KEY_SHARED);
        final List<Integer> sharedInTwo = cache.indexesForArrayKey(arrayTwo, KEY_SHARED);

        assertEquals(indices(1, 3, 4), sharedInOne, "Expected three hits for 'Shared' in arrayOne at 1, 3, 4");
        assertEquals(indices(0, 2, 3, 4), sharedInTwo, "Expected four hits for 'Shared' in arrayTwo at 0, 2, 3, 4");

        // Missing key should yield an empty list
        final List<Integer> notFound = cache.indexesForArrayKey(arrayOne, KEY_NOT_FOUND);
        assertTrue(notFound.isEmpty(), "Expected no hits when key is not present");
    }
}