package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    /**
     * Verifies that enabling the 'blocking' option on the CacheBuilder
     * results in a cache decorated with BlockingCache. It also confirms
     * that the decorator is created with its default timeout value.
     */
    @Test
    public void shouldBuildBlockingCacheWithDefaultTimeoutWhenBlockingIsEnabled() {
        // Arrange: Create a CacheBuilder and enable the blocking option.
        final String cacheId = "test-blocking-cache";
        CacheBuilder cacheBuilder = new CacheBuilder(cacheId)
                .blocking(true);

        // Act: Build the cache instance.
        Cache cache = cacheBuilder.build();

        // Assert: Verify the cache is a BlockingCache and has the correct default timeout.
        assertTrue("The built cache should be an instance of BlockingCache.",
                cache instanceof BlockingCache);

        BlockingCache blockingCache = (BlockingCache) cache;
        assertEquals("A BlockingCache decorator should have a default timeout of 0.",
                0L, blockingCache.getTimeout());
    }
}