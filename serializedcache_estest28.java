package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that a NullPointerException is thrown when {@code putObject} is called
     * on a SerializedCache that was initialized with a null delegate.
     *
     * The SerializedCache decorator relies on an underlying cache instance (the delegate)
     * to perform storage operations. If this delegate is null, any attempt to use it
     * should result in a NullPointerException, indicating an improper initialization.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnPutObjectWhenDelegateIsNull() {
        // Arrange: Create a SerializedCache with a null delegate cache.
        Cache serializedCache = new SerializedCache(null);

        // Act & Assert: Attempting to add an entry should throw a NullPointerException
        // because the internal delegate is null.
        serializedCache.putObject("someKey", "someValue");
    }
}