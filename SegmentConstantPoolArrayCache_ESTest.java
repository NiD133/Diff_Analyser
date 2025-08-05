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
 * Unless required by applicable law in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.harmony.unpack200;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    private SegmentConstantPoolArrayCache cache;

    @Before
    public void setUp() {
        cache = new SegmentConstantPoolArrayCache();
    }

    @Test
    public void indexesForArrayKey_shouldReturnAllMatchingIndexesForValue() {
        // Arrange
        final String[] array = {"Foo", "Bar", "Baz", "Bar", "Qux"};

        // Act
        final List<Integer> indexes = cache.indexesForArrayKey(array, "Bar");

        // Assert
        assertNotNull(indexes);
        assertEquals(2, indexes.size());
        assertTrue(indexes.containsAll(Arrays.asList(1, 3)));
    }

    @Test
    public void indexesForArrayKey_shouldReturnAllIndexesOfNullKey() {
        // Arrange
        final String[] array = {"A", null, "C", null, "E"};

        // Act
        final List<Integer> indexes = cache.indexesForArrayKey(array, null);

        // Assert
        assertNotNull(indexes);
        assertEquals(2, indexes.size());
        assertTrue(indexes.containsAll(Arrays.asList(1, 3)));
    }

    @Test
    public void indexesForArrayKey_shouldReturnEmptyListWhenKeyNotFound() {
        // Arrange
        final String[] array = {"A", "B", "C"};

        // Act
        final List<Integer> indexes = cache.indexesForArrayKey(array, "Z");

        // Assert
        assertNotNull(indexes);
        assertTrue(indexes.isEmpty());
    }

    @Test
    public void indexesForArrayKey_shouldCacheArrayForFutureLookups() {
        // Arrange
        final String[] array = {"A", "B", "C"};
        assertFalse("Array should not be cached initially", cache.arrayIsCached(array));

        // Act: Perform a lookup, which should trigger caching.
        cache.indexesForArrayKey(array, "A");

        // Assert
        assertTrue("Array should be cached after a lookup", cache.arrayIsCached(array));
    }

    @Test
    public void indexesForArrayKey_shouldUseCacheOnSubsequentCalls() {
        // Arrange
        final String[] array = {"A", "B", "A"};

        // Act: First call caches the array and returns indexes for "A"
        final List<Integer> firstCallIndexes = cache.indexesForArrayKey(array, "A");

        // Assert: Second call for a different key should use the same cache
        final List<Integer> secondCallIndexes = cache.indexesForArrayKey(array, "B");

        assertEquals(Arrays.asList(0, 2), firstCallIndexes);
        assertEquals(Collections.singletonList(1), secondCallIndexes);
    }

    @Test
    public void indexesForArrayKey_shouldRecacheWhenCachedArraySizeIsStale() {
        // This is a white-box test to verify that a stale cache entry (size mismatch) is detected and refreshed.
        // Arrange
        final String[] arrayToQuery = {"A", null}; // size 2
        final String[] staleArray = {"B"};         // size 1

        // Manually insert a stale entry into the internal cache map.
        // The key is arrayToQuery, but the value is a CachedArray for staleArray, which has a different size.
        final SegmentConstantPoolArrayCache.CachedArray staleCachedArray = cache.new CachedArray(staleArray);
        cache.knownArrays.put(arrayToQuery, staleCachedArray);

        // Act: When we request indexes, the cache should detect the size mismatch (2 vs 1),
        // discard the stale entry, re-cache arrayToQuery, and return the correct indexes.
        final List<Integer> indexes = cache.indexesForArrayKey(arrayToQuery, null);

        // Assert
        assertNotNull(indexes);
        assertEquals(1, indexes.size());
        assertEquals(Integer.valueOf(1), indexes.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void indexesForArrayKey_shouldThrowNPEWhenArrayIsNull() {
        cache.indexesForArrayKey(null, "some-key");
    }

    @Test
    public void cacheArray_shouldThrowExceptionWhenArrayIsAlreadyCached() {
        // Arrange
        final String[] array = {"A", "B"};
        cache.cacheArray(array); // First time is OK

        // Act & Assert: Caching the same array instance again should fail
        try {
            cache.cacheArray(array);
            fail("Expected an IllegalArgumentException for caching a duplicate array");
        } catch (final IllegalArgumentException e) {
            assertEquals("Trying to cache an array that already exists", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void cacheArray_shouldThrowNPEWhenArrayIsNull() {
        cache.cacheArray(null);
    }

    @Test
    public void arrayIsCached_shouldReturnFalseForUncachedArray() {
        assertFalse(cache.arrayIsCached(new String[]{"test"}));
    }

    @Test
    public void arrayIsCached_shouldReturnFalseWhenCachedArrayIsForDifferentInstance() {
        // This is a white-box test to verify an internal consistency check.
        // We manually create a state where the cache map points to a CachedArray
        // that belongs to a different array instance.
        // Arrange
        final String[] arrayToQuery = new String[4];
        final String[] differentArray = new String[5];

        // Manually insert an entry where the key is arrayToQuery, but the value is a CachedArray for differentArray.
        final SegmentConstantPoolArrayCache.CachedArray cachedArrayForDifferentInstance = cache.new CachedArray(differentArray);
        cache.knownArrays.put(arrayToQuery, cachedArrayForDifferentInstance);

        // Act & Assert: When we ask if arrayToQuery is cached, it should find the entry but realize
        // the cached array instance doesn't match, returning false.
        assertFalse(cache.arrayIsCached(arrayToQuery));
    }
}