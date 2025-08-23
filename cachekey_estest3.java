package org.apache.ibatis.cache;

import org.junit.Test;

/**
 * Unit tests for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that calling updateAll() with a null array argument
     * results in a NullPointerException. This is the expected behavior
     * as the method iterates over the input array without a preceding null check.
     */
    @Test(expected = NullPointerException.class)
    public void updateAllShouldThrowNullPointerExceptionWhenArrayIsNull() {
        // Arrange: Create a new CacheKey instance.
        CacheKey cacheKey = new CacheKey();

        // Act: Call the method under test with a null argument.
        // The test will pass only if this line throws a NullPointerException.
        cacheKey.updateAll(null);

        // Assert: The assertion is handled by the @Test(expected) annotation.
    }
}