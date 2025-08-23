package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for BlockingCache to ensure its core functionalities work as expected.
 */
public class BlockingCacheTest {

    /**
     * Verifies that a newly created BlockingCache instance has a default timeout of 0.
     * A timeout of 0 indicates that a thread will wait indefinitely for a lock to be released.
     */
    @Test
    public void shouldHaveDefaultTimeoutOfZeroOnInitialization() {
        // Arrange: Create a BlockingCache with a standard delegate cache.
        // The specific type of delegate is not important for this test.
        Cache delegateCache = new PerpetualCache("default-cache");
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act: Retrieve the timeout value from the new instance.
        long timeout = blockingCache.getTimeout();

        // Assert: The default timeout should be 0L.
        assertEquals("Default timeout should be 0", 0L, timeout);
    }
}