package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

/**
 * Test suite for the SerializedCache decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that putObject propagates a NullPointerException if the underlying
     * delegate cache throws it.
     */
    @Test(expected = NullPointerException.class)
    public void putObjectShouldPropagateNpeFromDelegate() {
        // Arrange: Create a delegate chain that is guaranteed to throw a NullPointerException.
        // A SynchronizedCache initialized with a null delegate will throw an NPE upon use.
        Cache misconfiguredDelegate = new SynchronizedCache(null);
        Cache serializedCache = new SerializedCache(misconfiguredDelegate);

        // Act: Attempt to put an object into the cache.
        // This call is expected to fail and throw a NullPointerException.
        serializedCache.putObject("anyKey", "anyValue");
    }
}