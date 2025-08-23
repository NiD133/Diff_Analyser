package org.apache.ibatis.cache;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the equals() method correctly returns false
     * when a CacheKey object is compared with null. This adheres to the
     * general contract of Object.equals().
     */
    @Test
    public void shouldReturnFalseWhenComparingWithNull() {
        // Arrange: Create a new CacheKey instance.
        CacheKey cacheKey = new CacheKey();

        // Act: Compare the instance to null.
        boolean isEqual = cacheKey.equals(null);

        // Assert: The result should be false.
        assertFalse("A CacheKey instance must not be equal to null.", isEqual);
    }
}