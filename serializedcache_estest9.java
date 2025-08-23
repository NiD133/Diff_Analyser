package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that getId() correctly delegates the call to the underlying cache
     * and returns its ID.
     */
    @Test
    public void getIdShouldReturnIdOfDecoratedCache() {
        // Arrange
        final String expectedId = "user-cache";
        Cache delegateCache = new PerpetualCache(expectedId);
        SerializedCache serializedCache = new SerializedCache(delegateCache);

        // Act
        String actualId = serializedCache.getId();

        // Assert
        assertEquals("The cache ID should be delegated from the underlying cache.", expectedId, actualId);
    }
}