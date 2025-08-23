package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link CacheBuilder} class.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the ID provided to the CacheBuilder is correctly assigned
     * to the final Cache instance after it is built.
     */
    @Test
    public void buildShouldCreateCacheWithCorrectId() {
        // Arrange: Create a CacheBuilder with a specific ID.
        // The original test used an empty string, which is a valid edge case.
        String expectedId = "";
        CacheBuilder cacheBuilder = new CacheBuilder(expectedId);

        // Act: Configure a property (like size) and then build the cache.
        Cache builtCache = cacheBuilder.size(1).build();

        // Assert: The ID of the built cache must match the one provided during construction.
        assertEquals(expectedId, builtCache.getId());
    }
}