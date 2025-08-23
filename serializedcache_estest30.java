package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that calling equals() on a SerializedCache initialized with a null delegate
     * throws a NullPointerException. This behavior is expected because the equals() method
     * delegates the call to the underlying cache instance.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeOnEqualsWhenDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate cache.
        Cache serializedCache = new SerializedCache(null);

        // Act & Assert: Calling equals() on this instance must throw a NullPointerException.
        // The argument to equals() does not matter in this scenario.
        serializedCache.equals(new Object());
    }
}