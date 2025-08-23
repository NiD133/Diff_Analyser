package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for {@link BlockingCache}.
 */
// The original test class name `BlockingCache_ESTestTest4` and scaffolding are omitted for clarity.
// A more conventional name would be `BlockingCacheTest`.
public class BlockingCacheTest {

    /**
     * Verifies that removing a key after a cache miss correctly returns null.
     * <p>
     * A call to {@link BlockingCache#getObject(Object)} on a non-existent key
     * acquires a lock for that key. This test ensures that a subsequent call to
     * {@link BlockingCache#removeObject(Object)} for the same key successfully
     * releases the lock and returns null, as the item was never in the cache.
     */
    @Test
    public void shouldReturnNullWhenRemovingKeyThatCausedCacheMiss() {
        // Arrange
        final String cacheKey = "non-existent-key";
        Cache delegateCache = new PerpetualCache("test-delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Act
        // 1. Trigger a cache miss. This will return null but, more importantly,
        //    acquire a lock on `cacheKey` within the BlockingCache.
        blockingCache.getObject(cacheKey);

        // 2. Attempt to remove the object for the now-locked key.
        Object removedObject = blockingCache.removeObject(cacheKey);

        // Assert
        // The remove operation should succeed and return null because the key
        // was never associated with a value in the delegate cache.
        assertNull("Removing a non-existent key should return null.", removedObject);
    }
}