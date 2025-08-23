package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Tests for the {@link SerializedCache} decorator.
 * This suite focuses on how SerializedCache behaves when interacting with its delegate cache.
 */
public class SerializedCacheTest {

    /**
     * Verifies that method calls are delegated to the underlying cache.
     * If the delegate is null, a NullPointerException is expected.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate.
        Cache nullDelegate = null;
        SerializedCache serializedCache = new SerializedCache(nullDelegate);

        // Act: Call a method that delegates to the underlying cache.
        // This should trigger a NullPointerException.
        serializedCache.getSize();
    }
}