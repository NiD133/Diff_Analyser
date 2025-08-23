package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that the timeout value can be set and then retrieved correctly.
     */
    @Test
    public void shouldSetAndGetTimeout() {
        // Arrange
        // A simple delegate cache is sufficient for testing the timeout property.
        Cache delegateCache = new PerpetualCache("test-delegate");
        BlockingCache blockingCache = new BlockingCache(delegateCache);
        long expectedTimeout = 1L;

        // Act
        blockingCache.setTimeout(expectedTimeout);
        long actualTimeout = blockingCache.getTimeout();

        // Assert
        assertEquals("The retrieved timeout should match the value that was set.", expectedTimeout, actualTimeout);
    }
}