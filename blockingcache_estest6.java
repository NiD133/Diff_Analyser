package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BlockingCache} decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that the getSize() method correctly delegates the call
     * to the underlying cache and returns its size.
     */
    @Test
    public void shouldDelegateGetSizeToUnderlyingCache() {
        // Arrange: Create a delegate cache with one entry.
        Cache delegateCache = new PerpetualCache("delegate-cache");
        delegateCache.putObject("key1", "value1");

        // Arrange: Decorate the delegate cache with BlockingCache.
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act: Get the size from the BlockingCache.
        int actualSize = blockingCache.getSize();

        // Assert: The size should be 1, matching the delegate cache.
        assertEquals(1, actualSize);
    }
}