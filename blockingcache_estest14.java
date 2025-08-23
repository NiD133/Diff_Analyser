package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

    /**
     * This test verifies that if a delegate cache throws an exception,
     * the BlockingCache correctly propagates that exception to the caller.
     */
    @Test
    public void shouldPropagateExceptionWhenDelegateFails() {
        // Arrange: Create a delegate cache that is guaranteed to fail.
        // A FifoCache initialized with a null delegate will throw a NullPointerException
        // when any of its methods are called, serving as a "failing" delegate.
        Cache failingDelegate = new FifoCache(null);
        BlockingCache blockingCache = new BlockingCache(failingDelegate);

        // Act & Assert: Verify that calling getSize() on the BlockingCache
        // propagates the expected NullPointerException from the failing delegate.
        assertThrows(NullPointerException.class, blockingCache::getSize);
    }
}