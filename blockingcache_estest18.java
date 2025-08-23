package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Tests the behavior of BlockingCache when interacting with other cache decorators.
 */
public class BlockingCacheInteractionTest {

    /**
     * Verifies that getObject throws a ClassCastException if the underlying cache
     * contains an object of an unexpected type.
     *
     * This test simulates a specific edge case:
     * 1. A cache stack is created: BlockingCache -> SoftCache -> PerpetualCache.
     * 2. An entry is put directly into the innermost PerpetualCache, bypassing the
     *    SoftCache wrapper. The SoftCache is designed to wrap all values in a
     *    SoftReference.
     * 3. When getObject() is called on the top-level BlockingCache, it delegates
     *    down the chain.
     * 4. The SoftCache retrieves the raw value (a String) and tries to cast it to a
     *    SoftReference, which fails and correctly throws a ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void getObjectShouldThrowClassCastExceptionWhenDelegateCacheContainsWrongType() {
        // Arrange: Create a decorated cache stack.
        Cache perpetualCache = new PerpetualCache("test-delegate-cache");
        Cache softCache = new SoftCache(perpetualCache);
        Cache blockingCache = new BlockingCache(softCache);

        String key = "test-key";
        String value = "test-value";

        // Directly manipulate the innermost cache, bypassing the SoftCache decorator.
        // This puts a raw String into the cache, whereas SoftCache expects a SoftReference.
        perpetualCache.putObject(key, value);

        // Act & Assert: Attempting to get the object through the full cache stack.
        // This is expected to fail inside SoftCache when it tries to cast the String
        // to a SoftReference.
        blockingCache.getObject(key);
    }
}