package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the SerializedCache decorator.
 * This class focuses on verifying the decorator's behavior, such as delegation.
 */
public class SerializedCacheTest {

    /**
     * Verifies that the hashCode() method of SerializedCache correctly
     * delegates the call to the underlying (decorated) cache instance.
     * The hash code of the decorator should be the same as the hash code
     * of the cache it wraps.
     */
    @Test
    public void shouldDelegateHashCodeToDecoratedCache() {
        // Arrange: Create a delegate cache and wrap it with SerializedCache.
        Cache delegateCache = new PerpetualCache("test-delegate-cache");
        Cache serializedCache = new SerializedCache(delegateCache);

        // Act: Get the hash code from both the delegate and the decorator.
        int expectedHashCode = delegateCache.hashCode();
        int actualHashCode = serializedCache.hashCode();

        // Assert: The hash codes should be identical, confirming delegation.
        assertEquals(expectedHashCode, actualHashCode);
    }
}