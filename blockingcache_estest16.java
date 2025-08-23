package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Test suite for the {@link BlockingCache} decorator.
 */
public class BlockingCacheTest {

    /**
     * Verifies that getObject throws a NullPointerException when the key is null.
     * This is because the underlying lock mechanism (a ConcurrentHashMap) does not permit null keys.
     */
    @Test(expected = NullPointerException.class)
    public void getObjectWithNullKeyShouldThrowNPE() {
        // Given: A BlockingCache with a valid delegate cache.
        Cache delegate = new PerpetualCache("test-delegate");
        Cache blockingCache = new BlockingCache(delegate);

        // When: getObject is called with a null key.
        blockingCache.getObject(null);

        // Then: A NullPointerException is expected, as declared in the @Test annotation.
    }
}