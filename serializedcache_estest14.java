package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test suite for the SerializedCache decorator.
 * Note: The original test class name "SerializedCache_ESTestTest14" suggests it was
 * auto-generated. This class provides a more readable, manually crafted version.
 */
public class SerializedCacheTest {

    /**
     * Verifies that SerializedCache correctly propagates exceptions from its delegate cache.
     *
     * This test ensures that if the underlying cache (the "delegate") is misconfigured
     * and throws a NullPointerException during a 'removeObject' operation, the
     * SerializedCache wrapper does not suppress or alter this exception. This confirms
     * the decorator's transparency with respect to runtime exceptions from its delegate.
     */
    @Test
    public void removeObjectShouldPropagateNPEFromMisconfiguredDelegate() {
        // Arrange: Create a cache decorator chain where the innermost cache is null.
        // This setup is designed to trigger a NullPointerException within the delegate.
        Cache misconfiguredDelegate = new SynchronizedCache(null);
        Cache serializedCache = new SerializedCache(misconfiguredDelegate);
        Object anyKey = "any-key";

        // Act & Assert: Expect a NullPointerException when calling removeObject.
        try {
            serializedCache.removeObject(anyKey);
            // If this line is reached, the test fails because no exception was thrown.
            fail("Expected a NullPointerException to be thrown by the delegate cache.");
        } catch (NullPointerException e) {
            // Success: The expected exception was caught.
            // This confirms that SerializedCache correctly passed the exception through.
        }
    }
}