package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that calling clear() on a SerializedCache with a null delegate
     * throws a NullPointerException. This ensures the decorator handles
     * invalid initial states gracefully.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenClearIsCalledAndDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate cache.
        // This is an invalid state that the method should not be able to handle.
        Cache serializedCache = new SerializedCache(null);

        // Act: Attempt to clear the cache. This should fail because it will
        // try to call clear() on the null delegate.
        serializedCache.clear();

        // Assert: The test is expected to throw a NullPointerException,
        // which is declared in the @Test annotation.
    }
}