package org.apache.ibatis.mapping;

import org.apache.ibatis.cache.CacheException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link CacheBuilder}.
 */
public class CacheBuilderTest {

    @Test
    public void buildShouldThrowCacheExceptionWhenIdIsNull() {
        // Arrange: Create a CacheBuilder with a null ID.
        // The ID is required by standard decorators, like LoggingCache,
        // to instantiate their dependencies (e.g., a logger).
        CacheBuilder cacheBuilder = new CacheBuilder(null);

        // Act & Assert
        try {
            cacheBuilder.build();
            fail("Should have thrown CacheException because the cache ID is null.");
        } catch (CacheException e) {
            // The build fails because a decorator (LoggingCache) cannot be created
            // without an ID to associate with its logger.
            String expectedMessageFragment = "Error building standard cache decorators";
            assertTrue(
                "Exception message should indicate an error during decorator creation. Got: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}