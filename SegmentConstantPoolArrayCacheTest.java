package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SegmentConstantPoolArrayCacheTest {

    @Test
    void testCacheWithMultipleArraysAndMultipleHits() {
        // Initialize the cache
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();

        // Define test arrays
        final String[] firstArray = { "Zero", "Shared", "Two", "Shared", "Shared" };
        final String[] secondArray = { "Shared", "One", "Shared", "Shared", "Shared" };

        // Test caching behavior for the first array
        List<Integer> firstArraySharedIndexes = arrayCache.indexesForArrayKey(firstArray, "Shared");
        List<Integer> secondArraySharedIndexes = arrayCache.indexesForArrayKey(secondArray, "Shared");

        // Ensure caching by querying again
        firstArraySharedIndexes = arrayCache.indexesForArrayKey(firstArray, "Two");
        secondArraySharedIndexes = arrayCache.indexesForArrayKey(secondArray, "Shared");

        // Validate the results for the first array
        assertEquals(1, firstArraySharedIndexes.size());
        assertEquals(2, firstArraySharedIndexes.get(0).intValue());

        // Re-query for "Shared" in the first array and validate
        firstArraySharedIndexes = arrayCache.indexesForArrayKey(firstArray, "Shared");
        assertEquals(3, firstArraySharedIndexes.size());
        assertEquals(1, firstArraySharedIndexes.get(0).intValue());
        assertEquals(3, firstArraySharedIndexes.get(1).intValue());
        assertEquals(4, firstArraySharedIndexes.get(2).intValue());

        // Validate the results for the second array
        assertEquals(4, secondArraySharedIndexes.size());
        assertEquals(0, secondArraySharedIndexes.get(0).intValue());
        assertEquals(2, secondArraySharedIndexes.get(1).intValue());
        assertEquals(3, secondArraySharedIndexes.get(2).intValue());
        assertEquals(4, secondArraySharedIndexes.get(3).intValue());

        // Test for a non-existent element
        final List<Integer> nonExistentIndexes = arrayCache.indexesForArrayKey(firstArray, "Not found");
        assertEquals(0, nonExistentIndexes.size());
    }

    @Test
    void testCacheWithSingleArrayAndMultipleHits() {
        // Initialize the cache
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();

        // Define a test array
        final String[] array = { "Zero", "OneThreeFour", "Two", "OneThreeFour", "OneThreeFour" };

        // Query for "OneThreeFour" and validate
        final List<Integer> indexes = arrayCache.indexesForArrayKey(array, "OneThreeFour");
        assertEquals(3, indexes.size());
        assertEquals(1, indexes.get(0).intValue());
        assertEquals(3, indexes.get(1).intValue());
        assertEquals(4, indexes.get(2).intValue());
    }

    @Test
    void testCacheWithSingleArrayAndSingleHit() {
        // Initialize the cache
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();

        // Define a test array
        final String[] array = { "Zero", "One", "Two", "Three", "Four" };

        // Query for "Three" and validate
        final List<Integer> indexes = arrayCache.indexesForArrayKey(array, "Three");
        assertEquals(1, indexes.size());
        assertEquals(3, indexes.get(0).intValue());
    }
}