package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SerializedCache} to ensure it handles various caching scenarios correctly.
 */
class SerializedCacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        // A SerializedCache decorates another cache, in this case, a simple PerpetualCache.
        cache = new SerializedCache(new PerpetualCache("default-cache"));
    }

    @Test
    void shouldStoreAndRetrieveNullValue() {
        // Arrange
        Integer key = 1;

        // Act: Store a null value in the cache.
        cache.putObject(key, null);
        Object retrievedValue = cache.getObject(key);

        // Assert: The retrieved value should be null.
        assertNull(retrievedValue, "Cache should correctly handle storing and retrieving null values.");
    }

    @Test
    void shouldReturnNullForNonExistentKey() {
        // Arrange
        Integer nonExistentKey = 999;

        // Act
        Object retrievedValue = cache.getObject(nonExistentKey);

        // Assert
        assertNull(retrievedValue, "Cache should return null for a key that does not exist.");
    }
}