package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for BlockingCache.
 */
public class BlockingCacheTest {

    /**
     * Verifies that the getId() method of BlockingCache correctly delegates the call
     * to the underlying (decorated) cache instance and returns its ID.
     */
    @Test
    public void shouldReturnIdOfDecoratedCache() {
        // Arrange
        String expectedId = "user-cache";
        Cache delegateCache = new PerpetualCache(expectedId);
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act
        String actualId = blockingCache.getId();

        // Assert
        assertEquals("The blocking cache should return the ID of its delegate.", expectedId, actualId);
    }
}