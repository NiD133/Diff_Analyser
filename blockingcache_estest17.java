package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

    /**
     * Verifies that BlockingCache propagates a NullPointerException when an underlying
     * cache in the decorator chain is misconfigured with a null delegate.
     */
    @Test(expected = NullPointerException.class)
    public void getObjectShouldPropagateNpeFromMisconfiguredDelegate() {
        // Arrange: Create a chain of cache decorators where an inner cache
        // is improperly initialized with a null delegate. This simulates a
        // configuration error.
        // The chain is: BlockingCache -> TransactionalCache -> FifoCache -> SynchronizedCache -> null
        Cache misconfiguredDelegate = new SynchronizedCache(null);
        Cache cacheWithFifo = new FifoCache(misconfiguredDelegate);
        Cache cacheWithTransactions = new TransactionalCache(cacheWithFifo);
        Cache blockingCache = new BlockingCache(cacheWithTransactions);

        Object anyKey = new Object();

        // Act: Attempt to get an object. The call will propagate down the decorator
        // chain until SynchronizedCache throws a NullPointerException upon accessing
        // its null delegate.
        blockingCache.getObject(anyKey);

        // Assert: The @Test(expected) annotation verifies that the NullPointerException
        // is thrown and not swallowed by any of the decorators.
    }
}