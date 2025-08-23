package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CacheKey} class.
 */
class CacheKeyTest {

    @Test
    void cloneOfNullCacheKeyShouldBeEqualToOriginal() throws CloneNotSupportedException {
        // Arrange: Get the special, static NULL_CACHE_KEY instance.
        CacheKey originalNullKey = CacheKey.NULL_CACHE_KEY;

        // Act: Create a clone of the key.
        CacheKey clonedNullKey = originalNullKey.clone();

        // Assert: The clone should be equal to the original, and their hash codes must match.
        // This verifies the contract between equals() and hashCode() for this special case.
        assertEquals(originalNullKey, clonedNullKey);
        assertEquals(originalNullKey.hashCode(), clonedNullKey.hashCode());
    }
}