package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the build() method creates a Cache instance
     * with the same ID that was provided to the CacheBuilder constructor.
     * This test uses an empty string as an edge case for the ID.
     */
    @Test
    public void shouldBuildCacheWithIdProvidedInConstructor() {
        // Arrange
        String expectedId = "";
        CacheBuilder builder = new CacheBuilder(expectedId);

        // Act
        Cache resultCache = builder.build();

        // Assert
        assertNotNull("The built cache should not be null.", resultCache);
        assertEquals("The cache ID should match the one provided to the builder.",
                     expectedId, resultCache.getId());
    }
}