package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that BlockingCache correctly delegates the getId() call
     * to the underlying (decorated) cache instance.
     */
    @Test
    public void shouldReturnIdOfDecoratedCache() {
        // Arrange
        final String expectedId = "user-cache";
        Cache decoratedCache = new PerpetualCache(expectedId);
        Cache blockingCache = new BlockingCache(decoratedCache);

        // Act
        String actualId = blockingCache.getId();

        // Assert
        assertEquals("The decorator should delegate getId() to the underlying cache.", expectedId, actualId);
    }
}