package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the BlockingCache decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that if an item is already in the underlying cache,
     * BlockingCache returns it directly without any blocking. This is the
     * standard cache-hit scenario.
     */
    @Test
    public void shouldReturnItemWhenKeyExistsInDelegateCache() {
        // Arrange: Set up the components for the test.
        // We use a simple PerpetualCache as the underlying cache.
        Cache delegateCache = new PerpetualCache("delegate-cache");
        BlockingCache blockingCache = new BlockingCache(delegateCache);

        // Add an object to the delegate cache directly.
        String key = "user:123";
        String expectedValue = "John Doe";
        delegateCache.putObject(key, expectedValue);

        // Act: Call the method under test.
        Object retrievedValue = blockingCache.getObject(key);

        // Assert: Verify that the correct value was returned.
        assertEquals(expectedValue, retrievedValue);
    }
}