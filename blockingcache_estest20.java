package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that operations on BlockingCache fail when it is constructed with a null delegate.
     * The decorator pattern requires a valid underlying object to delegate calls to.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDelegateIsNull() {
        // Arrange: Create a BlockingCache with a null delegate cache.
        Cache nullDelegate = null;
        BlockingCache blockingCache = new BlockingCache(nullDelegate);

        // Act: Attempt to call a method that delegates to the underlying cache.
        blockingCache.clear();

        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
    }
}