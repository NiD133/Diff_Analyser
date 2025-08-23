package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that a newly created SerializedCache, which wraps an empty cache,
     * correctly reports its size as zero.
     */
    @Test
    public void shouldReturnZeroSizeForNewCache() {
        // Arrange: Create a new SerializedCache wrapping an empty delegate cache.
        Cache delegateCache = new PerpetualCache("test-cache");
        SerializedCache serializedCache = new SerializedCache(delegateCache);

        // Act: Get the size of the newly created cache.
        int size = serializedCache.getSize();

        // Assert: The size should be 0, as the cache is empty.
        assertEquals(0, size);
    }
}