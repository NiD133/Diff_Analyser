package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Tests for BlockingCache to ensure it handles various scenarios correctly.
 */
public class BlockingCacheTest {

    /**
     * The removeObject method should not accept a null key, as the underlying
     * collections do not support it. This test verifies that attempting to do so
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenRemovingNullKey() {
        // Arrange
        // Use a real cache implementation as the delegate for a realistic setup.
        Cache delegate = new PerpetualCache("test-delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegate);

        // Act
        // Attempting to remove an object with a null key should fail fast.
        blockingCache.removeObject(null);

        // Assert
        // The test expects a NullPointerException, as declared in the @Test annotation.
    }
}