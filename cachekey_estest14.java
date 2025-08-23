package org.apache.ibatis.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that a newly created, empty CacheKey returns the expected default hash code.
     * This ensures that the initial state of a CacheKey is consistent and predictable.
     */
    @Test
    public void shouldReturnDefaultHashCodeForNewEmptyCacheKey() {
        // Arrange: Create a new, empty CacheKey instance.
        // According to the source, the default hashcode is initialized to 17.
        CacheKey emptyCacheKey = new CacheKey();
        int expectedHashCode = 17;

        // Act: Retrieve the hash code from the new instance.
        int actualHashCode = emptyCacheKey.hashCode();

        // Assert: Verify that the actual hash code matches the expected default value.
        assertEquals(expectedHashCode, actualHashCode);
    }
}