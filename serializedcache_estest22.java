package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for {@link SerializedCache}.
 */
public class SerializedCacheTest {

    /**
     * Verifies that SerializedCache correctly propagates exceptions thrown by its underlying delegate cache.
     * In this case, a NullPointerException is triggered deep in the decorator chain and is expected
     * to bubble up.
     */
    @Test(expected = NullPointerException.class)
    public void getObjectShouldPropagateNPEFromDelegateWhenKeyIsNull() {
        // Arrange: Create a decorator chain where the innermost delegate is null.
        // This setup is designed to cause a NullPointerException when the chain is accessed.
        Cache synchronizedCacheWithNullDelegate = new SynchronizedCache(null);
        Cache softCache = new SoftCache(synchronizedCacheWithNullDelegate);
        SerializedCache serializedCache = new SerializedCache(softCache);

        // Act: Attempt to retrieve a null key. This call is passed down the decorator chain
        // until SynchronizedCache tries to call a method on its null delegate,
        // which triggers the expected NullPointerException.
        serializedCache.getObject(null);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected = ...) annotation.
    }
}