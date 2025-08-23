package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for {@link SerializedCache}.
 */
public class SerializedCacheTest {

    /**
     * Verifies that {@link SerializedCache#getObject(Object)} propagates a NullPointerException
     * if the underlying delegate cache throws it. This ensures that the decorator correctly
     * handles and passes through exceptions from the cache it wraps.
     */
    @Test(expected = NullPointerException.class)
    public void getObjectShouldPropagateNPEWhenDelegateThrowsNPE() {
        // Arrange
        // Create a delegate cache that is guaranteed to throw a NullPointerException when used.
        // A TransactionalCache initialized with a null delegate serves this purpose.
        Cache failingDelegate = new TransactionalCache(null);
        Cache serializedCache = new SerializedCache(failingDelegate);
        Object anyKey = "some-key";

        // Act
        // Attempt to retrieve an object. This call is expected to fail by throwing an
        // exception because the delegate will throw a NullPointerException.
        serializedCache.getObject(anyKey);

        // Assert
        // The test succeeds if a NullPointerException is thrown, as specified by the
        // @Test(expected = ...) annotation. No further assertions are needed.
    }
}