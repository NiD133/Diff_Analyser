package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the static utility methods in {@link LZMAUtils}.
 */
@DisplayName("LZMAUtils caching behavior")
class LZMAUtilsTest {

    /**
     * Restores the default caching behavior after each test to ensure test isolation.
     * LZMAUtils uses a static field for caching, which can be modified by tests.
     */
    @AfterEach
    void restoreDefaultCachingBehavior() {
        LZMAUtils.setCacheLZMAAvailablity(true);
    }

    @Test
    @DisplayName("should correctly check for LZMA availability when caching is disabled")
    void lzmaAvailabilityCheckSucceedsWhenCachingIsDisabled() {
        // Arrange: Disable the availability cache, which is on by default.
        LZMAUtils.setCacheLZMAAvailablity(false);

        // Act & Assert:
        // 1. Verify the internal cache state reflects that caching is turned off.
        assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability(),
                "The cache status should be DONT_CACHE after being disabled.");

        // 2. Verify the availability check still functions correctly without the cache.
        assertTrue(LZMAUtils.isLZMACompressionAvailable(),
                "isLZMACompressionAvailable() should return true even when caching is off.");
    }
}