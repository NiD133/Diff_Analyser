package org.apache.ibatis.cache.decorators;

import static org.junit.Assert.assertEquals;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Tests for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that the getId() method correctly delegates the call
     * to the underlying cache instance.
     */
    @Test
    public void shouldReturnIdOfDelegateCache() {
        // Arrange
        String expectedId = "test-cache-id";
        Cache delegateCache = new PerpetualCache(expectedId);
        Cache serializedCache = new SerializedCache(delegateCache);

        // Act
        String actualId = serializedCache.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }
}