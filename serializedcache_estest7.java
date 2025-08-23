package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    @Test
    public void shouldCountEntryWhenPuttingNullValue() {
        // Arrange: Create a delegate cache and wrap it with the SerializedCache.
        Cache delegateCache = new PerpetualCache("test-delegate");
        SerializedCache serializedCache = new SerializedCache(delegateCache);
        String key = "some-key";

        // Act: Put an object with a null value into the cache.
        serializedCache.putObject(key, null);
        int cacheSize = serializedCache.getSize();

        // Assert: Verify that the cache size is 1, confirming the entry was added.
        assertEquals(1, cacheSize);
    }
}