package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the build() method creates a Cache instance
     * with the ID provided to the builder's constructor.
     */
    @Test
    public void shouldBuildCacheWithCorrectId() {
        // Arrange
        String expectedId = "test-cache-id";
        CacheBuilder cacheBuilder = new CacheBuilder(expectedId);

        // Act
        Cache resultCache = cacheBuilder.build();

        // Assert
        assertEquals("The built cache should have the ID assigned at construction.",
                expectedId, resultCache.getId());
    }
}