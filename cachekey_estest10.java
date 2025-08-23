package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies the reflexive property of the equals method, ensuring that
     * a CacheKey instance is always equal to itself.
     */
    @Test
    public void shouldBeEqualToItself() {
        // Arrange
        CacheKey cacheKey = new CacheKey();

        // Act & Assert
        // According to the Java contract for Object.equals(), an object must be equal to itself.
        // Using assertEquals is more idiomatic for this check than assertTrue(cacheKey.equals(cacheKey)).
        assertEquals("A CacheKey instance should be equal to itself.", cacheKey, cacheKey);
    }
}