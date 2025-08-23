package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

    /**
     * This test verifies that {@link BlockingCache#removeObject(Object)} correctly
     * propagates exceptions thrown by the key's {@code hashCode()} method.
     *
     * The test works by:
     * 1. Using a {@link PerpetualCache} instance with a null ID as the key. The
     *    {@code hashCode()} method of this class is designed to throw a
     *    {@code CacheException} if its ID is null.
     * 2. Calling {@code removeObject} on the {@code BlockingCache} with this special key.
     * 3. Internally, {@code BlockingCache} uses a {@code ConcurrentHashMap} to manage locks.
     *    Map operations, such as remove, invoke the key's {@code hashCode()} method.
     * 4. This interaction is expected to trigger the exception from the key, which should
     *    be propagated to the caller.
     */
    @Test
    public void removeObjectShouldPropagateExceptionFromKeyHashCode() {
        // Arrange
        // A dummy delegate cache is required by the BlockingCache constructor.
        // Its behavior is not relevant for this test.
        Cache delegate = new PerpetualCache("delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegate);

        // Create a key whose hashCode() method is known to throw an exception.
        // A PerpetualCache instance with a null ID serves this purpose perfectly.
        Object keyWithFailingHashCode = new PerpetualCache(null);

        // Act & Assert
        // We expect a CacheException because blockingCache.removeObject() will internally
        // call hashCode() on the key, which is designed to fail.
        CacheException thrown = assertThrows(CacheException.class, () -> {
            blockingCache.removeObject(keyWithFailingHashCode);
        });

        // Verify that the exception message is the one expected from PerpetualCache.
        assertEquals("Cache instances require an ID.", thrown.getMessage());
    }
}