package org.apache.ibatis.cache;

import org.junit.Test;

/**
 * Test suite for the {@link CacheKey} class.
 */
public class CacheKeyTest {

    /**
     * Verifies that the CacheKey constructor throws a NullPointerException
     * when initialized with a null array.
     *
     * The constructor delegates to the updateAll() method, which is expected
     * to fail when attempting to iterate over a null array.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenConstructedWithNullArray() {
        // When: Attempting to create a CacheKey with a null object array.
        // The cast to (Object[]) is necessary to resolve ambiguity between
        // CacheKey(Object[]) and a potential future CacheKey(Object) constructor.
        new CacheKey((Object[]) null);

        // Then: A NullPointerException is expected, as declared by the @Test annotation.
    }
}