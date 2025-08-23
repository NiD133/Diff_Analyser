package org.apache.ibatis.cache;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the CacheKey class.
 */
public class CacheKeyTest {

    /**
     * Verifies that calling updateAll() correctly increments the internal update count
     * by the number of elements in the provided array.
     */
    @Test
    public void shouldIncrementUpdateCountByArraySizeWhenUpdatingWithArray() {
        // Arrange: Create a CacheKey and an array of objects to add.
        CacheKey cacheKey = new CacheKey();
        Object[] objectsToAdd = new Object[7];
        int expectedCount = objectsToAdd.length;

        // Act: Update the cache key with all objects from the array.
        cacheKey.updateAll(objectsToAdd);

        // Assert: The update count should match the number of objects added.
        assertEquals(expectedCount, cacheKey.getUpdateCount());
    }
}