/*
 * Test suite for SegmentConstantPoolArrayCache
 * Tests the caching functionality for string arrays and their index lookups
 */

package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class SegmentConstantPoolArrayCache_ESTest extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    // Test Data Setup Methods
    private String[] createEmptyStringArray(int size) {
        return new String[size];
    }
    
    private String[] createStringArrayWithValue(int size, int index, String value) {
        String[] array = new String[size];
        if (index < size) {
            array[index] = value;
        }
        return array;
    }

    // Tests for arrayIsCached() method
    
    @Test(timeout = 4000)
    public void testArrayIsCached_WhenArrayHasDifferentSizeThanCached_ReturnsFalse() {
        // Given: A cache with an array of size 5 cached under a size 4 array key
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] keyArray = createEmptyStringArray(4);
        String[] cachedArray = createEmptyStringArray(5);
        
        // Manually set up the cache with mismatched array sizes
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> knownArrays = 
            new IdentityHashMap<>();
        cache.knownArrays = knownArrays;
        SegmentConstantPoolArrayCache.CachedArray cachedArrayWrapper = 
            cache.new CachedArray(cachedArray);
        knownArrays.put(keyArray, cachedArrayWrapper);
        
        // When: Checking if the key array is cached
        boolean result = cache.arrayIsCached(keyArray);
        
        // Then: Should return false due to size mismatch
        assertFalse("Array should not be considered cached when sizes don't match", result);
        assertEquals("Cached array should have size 5", 5, cachedArrayWrapper.lastKnownSize());
    }

    @Test(timeout = 4000)
    public void testArrayIsCached_WhenArrayIsProperlyCached_ReturnsTrue() {
        // Given: A cache and an array that gets properly cached
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(8);
        
        // When: We cache the array and then check if it's cached
        cache.indexesForArrayKey(testArray, "test-key"); // This caches the array
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(testArray);
        boolean result = cache.arrayIsCached(cachedArray.primaryArray);
        
        // Then: Should return true
        assertTrue("Array should be considered cached when properly stored", result);
    }

    @Test(timeout = 4000)
    public void testArrayIsCached_WithNullKnownArrays_ThrowsNullPointerException() {
        // Given: A cache with null knownArrays
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(4);
        cache.knownArrays = null;
        
        // When/Then: Should throw NullPointerException
        try {
            cache.arrayIsCached(testArray);
            fail("Expected NullPointerException when knownArrays is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Tests for CachedArray functionality
    
    @Test(timeout = 4000)
    public void testCachedArray_CacheIndexes_WorksCorrectly() {
        // Given: A cached array
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(8);
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(testArray);
        
        // When: Caching indexes (this happens automatically in constructor)
        cachedArray.cacheIndexes();
        
        // Then: Should maintain correct size
        assertEquals("Cached array should maintain correct size", 8, cachedArray.lastKnownSize());
    }

    @Test(timeout = 4000)
    public void testCachedArray_IndexesForKey_WithNonExistentKey_ReturnsEmptyList() {
        // Given: A cached array with null elements
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(2);
        cache.lastArray = testArray;
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(testArray);
        
        // When: Looking for a key that doesn't exist
        List<Integer> result = cachedArray.indexesForKey("non-existent-key");
        
        // Then: Should return empty list
        assertTrue("Should return empty list for non-existent key", result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCachedArray_IndexesForKey_WithExistingKey_ReturnsCorrectIndexes() {
        // Given: A cached array with a specific value at index 1
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createStringArrayWithValue(8, 1, "test-value");
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(testArray);
        
        // When: Looking for the existing key
        List<Integer> result = cachedArray.indexesForKey("test-value");
        
        // Then: Should return list with index 1
        assertEquals("Should return exactly one index", 1, result.size());
        assertTrue("Should contain index 1", result.contains(1));
    }

    @Test(timeout = 4000)
    public void testCachedArray_LastKnownSize_ReturnsCorrectSize() {
        // Given: A cached array of size 8
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(8);
        SegmentConstantPoolArrayCache.CachedArray cachedArray = cache.new CachedArray(testArray);
        
        // When: Getting the last known size
        int size = cachedArray.lastKnownSize();
        
        // Then: Should return 8
        assertEquals("Should return correct array size", 8, size);
    }

    // Tests for indexesForArrayKey() method
    
    @Test(timeout = 4000)
    public void testIndexesForArrayKey_WithNullArray_ThrowsNullPointerException() {
        // Given: A cache
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        
        // When/Then: Should throw NullPointerException for null array
        try {
            cache.indexesForArrayKey(null, "test-key");
            fail("Expected NullPointerException for null array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKey_WithCachedArrayAndNullKey_ReturnsEmptyList() {
        // Given: A cache with a cached array and null key search
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(2);
        cache.lastArray = testArray;
        cache.cacheArray(testArray);
        cache.lastIndexes = new LinkedList<>();
        
        // When: Searching for null key
        List<Integer> result = cache.indexesForArrayKey(testArray, null);
        
        // Then: Should return empty list
        assertTrue("Should return empty list for null key", result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKey_WithUncachedArray_CachesAndReturnsIndexes() {
        // Given: An uncached array with null elements
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(9);
        
        // When: Searching for null (which exists at all indexes since array is empty)
        List<Integer> result = cache.indexesForArrayKey(testArray, null);
        
        // Then: Should cache the array and return all indexes
        assertNotNull("Should return a list", result);
        assertEquals("Should return all 9 indexes for null values", 9, result.size());
        
        // Verify subsequent search returns empty for different key
        List<Integer> emptyResult = cache.indexesForArrayKey(testArray, "different-key");
        assertTrue("Should return empty list for non-existent key", emptyResult.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKey_WithSameArrayTwice_ReturnsConsistentResults() {
        // Given: A cache and an array
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(1);
        
        // When: Searching twice for the same key
        List<Integer> firstResult = cache.indexesForArrayKey(testArray, "test-key");
        List<Integer> secondResult = cache.indexesForArrayKey(testArray, "test-key");
        
        // Then: Both results should be empty (key doesn't exist)
        assertTrue("First search should return empty list", firstResult.isEmpty());
        assertTrue("Second search should return empty list", secondResult.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIndexesForArrayKey_WithMismatchedCachedArray_ReturnsCorrectIndexes() {
        // Given: A cache with a mismatched cached array setup
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] keyArray = createEmptyStringArray(1);
        String[] cachedArray = createEmptyStringArray(0);
        
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> knownArrays = 
            cache.knownArrays;
        SegmentConstantPoolArrayCache.CachedArray cachedArrayWrapper = 
            cache.new CachedArray(cachedArray);
        knownArrays.put(keyArray, cachedArrayWrapper);
        
        // When: Searching in the key array (which has null at index 0)
        List<Integer> result = cache.indexesForArrayKey(keyArray, null);
        
        // Then: Should return the index where null is found
        assertNotNull("Should return a list", result);
        assertEquals("Should return exactly one index", 1, result.size());
        assertEquals("Cached array wrapper should have size 0", 0, cachedArrayWrapper.lastKnownSize());
    }

    // Tests for cacheArray() method
    
    @Test(timeout = 4000)
    public void testCacheArray_WithNullArray_ThrowsNullPointerException() {
        // Given: A cache
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        
        // When/Then: Should throw NullPointerException for null array
        try {
            cache.cacheArray(null);
            fail("Expected NullPointerException for null array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testCacheArray_WithAlreadyCachedArray_ThrowsIllegalArgumentException() {
        // Given: A cache with an already cached array
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(8);
        cache.cacheArray(testArray);
        
        // When/Then: Should throw IllegalArgumentException when caching again
        try {
            cache.cacheArray(testArray);
            fail("Expected IllegalArgumentException for already cached array");
        } catch (IllegalArgumentException e) {
            assertEquals("Should have specific error message", 
                        "Trying to cache an array that already exists", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCacheArray_WithValidArray_CachesSuccessfully() {
        // Given: A cache and a valid array
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = createEmptyStringArray(2);
        cache.lastArray = testArray;
        
        // When: Caching the array
        cache.cacheArray(testArray);
        
        // Then: Array should be cached (verified by subsequent operations not throwing)
        cache.lastArray = testArray;
        List<Integer> result = cache.indexesForArrayKey(testArray, null);
        assertNotNull("Cached array should be accessible", result);
    }
}