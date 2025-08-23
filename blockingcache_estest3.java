package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for BlockingCache.
 *
 * Note: The original test 'test02' was likely auto-generated.
 * It has been rewritten to clearly test a specific behavior in an understandable way.
 */
public class BlockingCacheTest {

    /**
     * Verifies that BlockingCache correctly delegates the getId() call
     * to the underlying cache instance it decorates.
     */
    @Test
    public void shouldDelegateGetIdToDecoratedCache() {
        // Arrange
        String expectedId = "user-cache";
        Cache delegateCache = new PerpetualCache(expectedId);
        Cache blockingCache = new BlockingCache(delegateCache);

        // Act
        String actualId = blockingCache.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }
}