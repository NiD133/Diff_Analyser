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
     * Verifies that getSize() on a newly created BlockingCache returns 0.
     * The BlockingCache should delegate the getSize() call to its underlying cache,
     * which is empty upon creation.
     */
    @Test
    public void shouldReturnZeroSizeForNewEmptyCache() {
        // Arrange
        // A PerpetualCache is a simple, in-memory cache implementation suitable for testing.
        Cache delegateCache = new PerpetualCache("test-cache");
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act
        int size = blockingCache.getSize();

        // Assert
        assertEquals("A new, empty cache should have a size of 0.", 0, size);
    }
}