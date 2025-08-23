package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link BlockingCache} decorator.
 */
@DisplayName("BlockingCache Test")
class BlockingCacheTest {

    private Cache blockingCache;
    private static final String DEFAULT_CACHE_ID = "test-cache";

    @BeforeEach
    void setUp() {
        // Arrange: Create a real delegate cache and wrap it with BlockingCache.
        // This represents a more realistic usage scenario.
        Cache delegate = new PerpetualCache(DEFAULT_CACHE_ID);
        blockingCache = new BlockingCache(delegate);
    }

    /**
     * Verifies that calling putObject directly throws an IllegalStateException.
     *
     * The BlockingCache is designed to release a lock during a putObject operation.
     * This lock is meant to be acquired by a preceding getObject call when a cache
     * miss occurs. Calling putObject without acquiring a lock first results in an
     * attempt to release a non-existent lock, which is an illegal state.
     */
    @Test
    @DisplayName("putObject should throw IllegalStateException if lock was not acquired")
    void putObjectShouldThrowExceptionIfLockNotAcquired() {
        // Arrange
        Object key = "any-key";
        Object value = "any-value";

        // Act & Assert
        // Expect an IllegalStateException because we are calling putObject without
        // a preceding getObject call that would have acquired the lock.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            blockingCache.putObject(key, value);
        });

        // Verify the exception message to ensure it's the one we expect.
        assertEquals("Detected an attempt at releasing unacquired lock. This should never happen.", exception.getMessage());
    }
}