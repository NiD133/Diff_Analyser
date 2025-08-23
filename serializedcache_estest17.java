package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SerializedCache}.
 */
public class SerializedCacheTest {

    /**
     * This test verifies an edge case where a cache decorator (BlockingCache) is used
     * as a key for another decorator (SerializedCache) that wraps it.
     *
     * The `SerializedCache` attempts to serialize the key. The process of serializing the
     * `BlockingCache` instance interferes with its internal locking mechanism, leading to
     * an `IllegalStateException`. This test ensures that this specific failure mode is
     * handled as expected.
     */
    @Test
    public void shouldThrowIllegalStateExceptionWhenKeyIsTheBlockingCacheInstanceItself() {
        // Arrange: Create a SerializedCache that wraps a BlockingCache.
        final String cacheId = "test-cache";
        Cache perpetualCache = new PerpetualCache(cacheId);
        Cache blockingCache = new BlockingCache(perpetualCache);
        SerializedCache serializedCache = new SerializedCache(blockingCache);
        String valueToCache = "some-value";

        // Act & Assert: Attempt to use the blockingCache instance as a key.
        try {
            serializedCache.putObject(blockingCache, valueToCache);
            fail("Expected an IllegalStateException because serializing the BlockingCache key interferes with its lock.");
        } catch (IllegalStateException e) {
            // Verify that the exception is the one thrown by BlockingCache due to a locking issue.
            assertEquals("Detected an attempt at releasing unacquired lock. This should never happen.", e.getMessage());
        }
    }
}