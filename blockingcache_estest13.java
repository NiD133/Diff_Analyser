package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that methods delegating to the underlying cache will throw a
     * NullPointerException if the delegate cache is null.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDelegateCacheIsNull() {
        // Arrange: Create a BlockingCache with a null delegate cache.
        // This is an invalid state, as BlockingCache requires a real cache to function.
        BlockingCache blockingCache = new BlockingCache(null);

        // Act: Attempt to call a method that delegates to the underlying cache.
        // The @Test(expected=...) annotation will assert that an NPE is thrown.
        blockingCache.getSize();
    }
}