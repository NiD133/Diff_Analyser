package org.apache.ibatis.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the update count is correctly incremented, even when the
     * update object is null.
     */
    @Test
    public void shouldIncrementUpdateCountWhenUpdatingWithNull() {
        // Arrange: Create a new CacheKey.
        // Its initial update count should be zero.
        CacheKey cacheKey = new CacheKey();
        assertEquals("A new CacheKey should have an update count of 0.", 0, cacheKey.getUpdateCount());

        // Act: Update the key with a null object.
        cacheKey.update(null);

        // Assert: The update count should now be 1.
        assertEquals("The update count should be 1 after a single update.", 1, cacheKey.getUpdateCount());
    }
}