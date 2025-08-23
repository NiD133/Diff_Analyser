package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for the static caching behavior of LZMA availability in {@link LZMAUtils}.
 */
class LZMAUtilsTest {

    /**
     * The default value for caching is true, but we need to control it for these tests.
     * This method ensures caching is disabled before each test in this class.
     */
    @BeforeEach
    void disableCaching() {
        LZMAUtils.setCacheLZMAAvailablity(false);
    }

    /**
     * Resets the LZMA availability cache to its default state after each test.
     * This prevents side effects between tests that modify this static state.
     */
    @AfterEach
    void resetCaching() {
        LZMAUtils.setCacheLZMAAvailablity(true);
    }

    @Test
    @DisplayName("Enabling caching should re-evaluate and cache the LZMA availability status")
    void enablingCachingShouldReEvaluateAndCacheAvailability() {
        // Arrange: Verify that caching is initially off.
        // This is set in the @BeforeEach method.
        assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability(),
            "Precondition failed: Caching should be disabled before the test runs.");

        // Act: Turn caching on. This is the action under test.
        LZMAUtils.setCacheLZMAAvailablity(true);

        // Assert: The availability status should now be evaluated and cached.
        // Since the XZ for Java library is on the test classpath, availability should be 'CACHED_AVAILABLE'.
        assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability(),
            "After enabling caching, the availability status should be re-evaluated and cached as available.");
    }
}