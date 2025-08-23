package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link BlockingCache} decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that the getId() method of BlockingCache correctly delegates the call
     * to the underlying (decorated) cache instance.
     * <p>
     * This test specifically checks the case where the decorated cache is initialized
     * with a null ID.
     */
    @Test
    public void shouldReturnNullIdWhenDecoratedCacheHasNullId() {
        // Arrange: Create a cache with a null ID and wrap it with a BlockingCache.
        // The PerpetualCache serves as a simple, concrete implementation for the decorated cache.
        Cache decoratedCache = new PerpetualCache(null);
        Cache blockingCache = new BlockingCache(decoratedCache);

        // Act: Retrieve the ID from the BlockingCache.
        String actualId = blockingCache.getId();

        // Assert: The returned ID should be null, as the call is delegated to the
        // underlying cache which was initialized with a null ID.
        assertNull("The ID should be null as it's delegated from the decorated cache.", actualId);
    }
}