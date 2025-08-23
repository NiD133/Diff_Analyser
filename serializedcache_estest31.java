package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that method calls on a SerializedCache instance will throw a
     * NullPointerException if the underlying delegate cache has not been provided (is null).
     * The SerializedCache is a decorator and requires a concrete Cache instance to function.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDelegateCacheIsNull() {
        // Arrange: Create a SerializedCache with a null delegate.
        // The constructor allows a null delegate, but subsequent method calls should fail.
        Cache serializedCache = new SerializedCache(null);

        // Act & Assert: Calling getId() on the decorator should attempt to access the
        // null delegate, resulting in a NullPointerException. The @Test annotation
        // handles the assertion.
        serializedCache.getId();
    }
}