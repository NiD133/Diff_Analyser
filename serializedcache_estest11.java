package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link SerializedCache}.
 * This focuses on improving the understandability of a specific test case for the equals method.
 */
public class SerializedCacheTest {

    /**
     * Verifies that the equals() method delegates the comparison to the underlying cache instance.
     * <p>
     * A SerializedCache should be considered equal to its delegate because the equals check is
     * passed through to the delegate. In this test, the delegate is compared to itself,
     * resulting in true.
     */
    @Test
    public void equals_shouldDelegateToUnderlyingCache_returningTrueWhenComparedToDelegate() {
        // Arrange
        Cache delegateCache = new PerpetualCache("delegate-cache");
        Cache serializedCache = new SerializedCache(delegateCache);

        // Act
        // The call serializedCache.equals(delegateCache) internally becomes delegateCache.equals(delegateCache).
        boolean areEqual = serializedCache.equals(delegateCache);

        // Assert
        assertTrue("A SerializedCache should be considered equal to its delegate instance.", areEqual);
    }

    /**
     * Verifies that the equals() method correctly returns false when the delegate comparison is false.
     */
    @Test
    public void equals_shouldDelegateToUnderlyingCache_returningFalseWhenComparedToOtherObjects() {
        // Arrange
        Cache delegateCache = new PerpetualCache("delegate-cache");
        Cache serializedCache = new SerializedCache(delegateCache);
        Cache anotherCache = new PerpetualCache("another-cache");

        // Act
        boolean areEqual = serializedCache.equals(anotherCache);

        // Assert
        assertFalse("A SerializedCache should not be equal to a different cache instance.", areEqual);
    }
}