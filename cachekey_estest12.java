package org.apache.ibatis.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the special NULL_CACHE_KEY instance is immutable.
     * An attempt to modify it by calling updateAll() should result in a RuntimeException.
     */
    @Test
    public void shouldThrowExceptionWhenAttemptingToUpdateNullCacheKey() {
        // Arrange
        Object[] anyObjects = new Object[] { "any-object" };
        String expectedErrorMessage = "Not allowed to update a null cache key instance.";

        // Act & Assert
        try {
            // Attempt to call updateAll on the immutable NULL_CACHE_KEY
            CacheKey.NULL_CACHE_KEY.updateAll(anyObjects);
            fail("Expected a RuntimeException because NULL_CACHE_KEY cannot be modified.");
        } catch (RuntimeException e) {
            // Verify that the exception has the expected message
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}