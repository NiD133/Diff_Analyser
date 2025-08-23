package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that the clear() operation does not have the unintended side effect
     * of modifying the cache's ID. The ID is delegated to the underlying cache
     * and should remain constant.
     */
    @Test
    public void clear_shouldNotChangeCacheId() {
        // Arrange: Create a SerializedCache that wraps a PerpetualCache with a specific ID.
        String expectedId = "user-cache";
        Cache delegateCache = new PerpetualCache(expectedId);
        Cache serializedCache = new SerializedCache(delegateCache);

        // Sanity check to ensure the ID is correct before the main action.
        assertEquals("Precondition failed: Initial cache ID is incorrect.",
                expectedId, serializedCache.getId());

        // Act: Clear the cache. This operation should be delegated to the underlying cache.
        serializedCache.clear();

        // Assert: Verify that the cache's ID remains unchanged after being cleared.
        assertEquals("The cache ID should not be affected by the clear operation.",
                expectedId, serializedCache.getId());
    }
}