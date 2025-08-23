package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the special {@link CacheKey#NULL_CACHE_KEY} instance.
 * This key is designed to be an immutable singleton.
 */
@DisplayName("Null Cache Key")
class NullCacheKeyTest {

    @Test
    @DisplayName("should be immutable and throw CacheException on updateAll")
    void shouldThrowExceptionOnUpdateAll() {
        // Arrange: The NULL_CACHE_KEY is a special, immutable instance.
        CacheKey nullCacheKey = CacheKey.NULL_CACHE_KEY;
        Object[] anyObjects = new Object[]{"any-object"};

        // Act & Assert: Any attempt to update it should throw a CacheException.
        CacheException thrown = assertThrows(CacheException.class,
            () -> nullCacheKey.updateAll(anyObjects));

        // Assert: Verify the exception message for correctness.
        assertEquals("Not allowed to update a NullCacheKey instance.", thrown.getMessage());
    }
}