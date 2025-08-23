package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that getObject() returns null when the requested key does not exist in the cache.
     * This ensures the cache correctly handles cache misses.
     */
    @Test
    public void shouldReturnNullWhenGettingObjectForNonExistentKey() {
        // Arrange: Create a delegate cache and wrap it with the SerializedCache decorator.
        Cache delegateCache = new PerpetualCache("test-delegate");
        SerializedCache serializedCache = new SerializedCache(delegateCache);
        final String nonExistentKey = "some-key-that-does-not-exist";

        // Act: Attempt to retrieve an object using a key that is not in the cache.
        Object retrievedObject = serializedCache.getObject(nonExistentKey);

        // Assert: The returned object should be null, indicating a cache miss.
        assertNull("Expected a null result for a non-existent key.", retrievedObject);
    }
}