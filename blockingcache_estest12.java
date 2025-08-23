package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Test suite for {@link BlockingCache}.
 */
public class BlockingCacheTest {

    /**
     * Verifies that attempting to put an object with a null key throws a NullPointerException.
     *
     * The BlockingCache uses an internal ConcurrentHashMap to manage locks.
     * ConcurrentHashMap does not permit null keys. The putObject method attempts to
     * release a lock using the key in a 'finally' block, which results in an NPE
     * when the key is null.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenPuttingNullKey() {
        // Arrange
        // A real delegate cache is used to ensure the test reflects a realistic scenario.
        Cache delegate = new PerpetualCache("test-delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegate);
        Object key = null;
        Object value = "any-value";

        // Act
        // This call is expected to throw a NullPointerException.
        blockingCache.putObject(key, value);

        // Assert
        // The exception is verified by the 'expected' attribute of the @Test annotation.
    }
}