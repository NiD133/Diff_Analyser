package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link CacheBuilder}.
 * Note: This class is a human-readable replacement for the auto-generated CacheBuilder_ESTestTest13.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the build() method correctly assigns the specified ID to the created cache,
     * even when other properties on the builder are configured.
     */
    @Test
    public void shouldBuildCacheWithCorrectId() {
        // Arrange
        String expectedId = ""; // The ID for the cache. The original test used an empty string.
        CacheBuilder cacheBuilder = new CacheBuilder(expectedId);

        // Set another property to ensure it doesn't interfere with the ID assignment.
        cacheBuilder.clearInterval(1L);

        // Act
        Cache resultCache = cacheBuilder.build();

        // Assert
        assertNotNull("The built cache should not be null", resultCache);
        assertEquals("The cache ID should match the one provided to the builder",
                expectedId, resultCache.getId());
    }
}