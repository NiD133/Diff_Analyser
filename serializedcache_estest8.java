package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test class focuses on the ID-related behavior of the SerializedCache decorator.
 * The original test name `SerializedCache_ESTestTest8` is kept for context, but in a
 * real-world scenario, this test would be part of a comprehensive `SerializedCacheTest` class.
 */
public class SerializedCache_ESTestTest8 {

    /**
     * Verifies that SerializedCache correctly delegates the getId() call to its
     * underlying (delegate) cache. This test specifically checks the scenario where the
     * delegate cache's ID is null.
     */
    @Test
    public void shouldReturnNullForIdWhenDelegateIdIsNull() {
        // Arrange: Create a delegate cache with a null ID and wrap it in a SerializedCache.
        Cache delegateCache = new PerpetualCache(null);
        Cache serializedCache = new SerializedCache(delegateCache);

        // Act: Get the ID from the SerializedCache decorator.
        String actualId = serializedCache.getId();

        // Assert: The returned ID should be null, matching the delegate cache's ID.
        assertNull("The ID from the decorator should be null, as it is delegated from the wrapped cache.", actualId);
    }
}