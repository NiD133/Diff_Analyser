package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that removeObject returns null when the specified key does not exist in the cache.
     */
    @Test
    public void shouldReturnNullWhenRemovingNonExistentObject() {
        // Arrange
        Cache delegateCache = new PerpetualCache("test-cache");
        Cache serializedCache = new SerializedCache(delegateCache);
        final String nonExistentKey = "non-existent-key";

        // Act
        Object removedObject = serializedCache.removeObject(nonExistentKey);

        // Assert
        assertNull("Removing a non-existent key should return null.", removedObject);
    }
}