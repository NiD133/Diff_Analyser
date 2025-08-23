package org.apache.ibatis.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test suite verifies the behavior of the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Tests that a newly created CacheKey has an initial update count of zero.
     * This confirms the correct initialization of the object's state.
     */
    @Test
    public void shouldInitializeUpdateCountToZero() {
        // Arrange: Create a new, empty CacheKey instance.
        CacheKey cacheKey = new CacheKey();

        // Act: Retrieve the initial update count.
        int initialUpdateCount = cacheKey.getUpdateCount();

        // Assert: The update count should be zero.
        assertEquals(0, initialUpdateCount);
    }
}