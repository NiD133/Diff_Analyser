package org.apache.ibatis.cache.decorators;

import static org.junit.Assert.assertEquals;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that the timeout value can be set and retrieved correctly.
     */
    @Test
    public void shouldSetAndGetTimeout() {
        // Arrange
        // A simple delegate cache is sufficient for testing the timeout functionality.
        // The complex chain of decorators from the original test is not necessary here.
        Cache delegate = new PerpetualCache("test-delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegate);
        
        // A negative timeout is a valid, if unusual, value to test.
        final long expectedTimeout = -2805L;

        // Act
        blockingCache.setTimeout(expectedTimeout);
        long actualTimeout = blockingCache.getTimeout();

        // Assert
        assertEquals("The retrieved timeout should match the value that was set.",
                expectedTimeout, actualTimeout);
    }
}