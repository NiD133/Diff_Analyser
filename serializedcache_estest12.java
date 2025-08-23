package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that comparing a SerializedCache instance with the cache it decorates
     * returns false.
     * <p>
     * This behavior is expected because the {@link SerializedCache} class (like other
     * decorators in this package) inherits the default {@code equals()} implementation
     * from {@link Object}, which checks for reference equality (i.e., if they are the
     * same object in memory).
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingDecoratorWithItsDelegate() {
        // Arrange
        Cache delegateCache = new PerpetualCache("test-cache-id");
        SerializedCache serializedCache = new SerializedCache(delegateCache);

        // Act
        // Compare the decorator instance with the delegate instance it wraps.
        boolean areEqual = serializedCache.equals(delegateCache);

        // Assert
        // The decorator and its delegate are different objects, so they should not be equal.
        assertFalse("A SerializedCache decorator should not be equal to its delegate instance.", areEqual);
    }
}