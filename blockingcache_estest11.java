package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

    /**
     * The BlockingCache should act as a decorator and propagate exceptions from its delegate.
     * This test verifies that if the delegate cache is improperly configured (e.g., with a null ID),
     * the BlockingCache will throw the corresponding exception when methods like getId() are called.
     */
    @Test
    public void shouldThrowCacheExceptionWhenGettingIdIfDelegateHasNullId() {
        // Arrange: Create a delegate cache with a null ID, which is an invalid state.
        Cache delegateWithNullId = new PerpetualCache(null);
        Cache blockingCache = new BlockingCache(delegateWithNullId);

        // Act & Assert: Verify that calling getId() on the decorator propagates the exception from the delegate.
        try {
            blockingCache.getId();
            fail("Expected a CacheException to be thrown because the delegate cache has a null ID.");
        } catch (CacheException e) {
            // This is the expected behavior.
            assertEquals("Cache instances require an ID.", e.getMessage());
        }
    }
}