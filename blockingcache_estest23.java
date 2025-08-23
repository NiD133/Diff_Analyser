package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BlockingCache} decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that a newly created BlockingCache has a default timeout of 0.
     */
    @Test
    public void shouldHaveZeroTimeoutByDefault() {
        // Arrange
        // A BlockingCache requires a delegate cache. A simple PerpetualCache is used here.
        Cache delegateCache = new PerpetualCache("default-cache");
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act
        long timeout = blockingCache.getTimeout();

        // Assert
        assertEquals("A new BlockingCache should have a default timeout of 0", 0L, timeout);
    }
}