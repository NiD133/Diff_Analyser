package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for {@link SerializedCache}.
 * This test focuses on how the cache decorator handles exceptions from the underlying cache chain.
 */
public class SerializedCacheTest {

    /**
     * Verifies that calling getSize() on a SerializedCache throws a NullPointerException
     * if the underlying cache delegate is improperly configured (i.e., null).
     * This ensures that exceptions from the delegate chain are propagated correctly.
     */
    @Test(expected = NullPointerException.class)
    public void getSizeShouldThrowNullPointerExceptionWhenUnderlyingCacheIsNull() {
        // Arrange: Create a chain of cache decorators with a null cache at the end.
        // This simulates an improper configuration where the base cache is missing.
        Cache softCacheWithNullDelegate = new SoftCache(null);
        Cache synchronizedCache = new SynchronizedCache(softCacheWithNullDelegate);
        Cache serializedCache = new SerializedCache(synchronizedCache);

        // Act: Attempt to get the size. This call will be delegated down the chain
        // until it reaches the null reference, which will cause a NullPointerException.
        serializedCache.getSize();

        // Assert: The test expects a NullPointerException to be thrown, which is
        // declared by the @Test(expected=...) annotation.
    }
}