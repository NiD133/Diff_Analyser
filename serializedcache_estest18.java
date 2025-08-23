package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link SerializedCache} decorator.
 */
public class SerializedCacheTest {

    /**
     * Verifies that calling hashCode() on a SerializedCache delegates the call
     * to the underlying cache. If the delegate cache (a PerpetualCache in this test)
     * was instantiated with a null ID, it is expected to throw a RuntimeException.
     */
    @Test
    public void hashCodeShouldThrowExceptionWhenDelegateCacheHasNoId() {
        // Arrange: Create a delegate cache with a null ID, which is an invalid state for PerpetualCache.
        Cache delegateCache = new PerpetualCache(null);
        SerializedCache serializedCache = new SerializedCache(delegateCache);

        try {
            // Act: Calling hashCode on the decorator, which should trigger the exception from the delegate.
            serializedCache.hashCode();
            fail("A RuntimeException was expected because the delegate cache has a null ID.");
        } catch (RuntimeException e) {
            // Assert: Verify the exception is the one we expect from PerpetualCache.
            assertEquals("Cache instances require an ID.", e.getMessage());
        }
    }
}