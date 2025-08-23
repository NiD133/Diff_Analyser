package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the caching behavior of the {@link LZMAUtils} class.
 */
public class LZMAUtilsCachingTest {

    /**
     * Verifies that {@link LZMAUtils#setCacheLZMAAvailablity(boolean)} correctly
     * enables and disables the caching of LZMA availability status.
     */
    @Test
    public void shouldUpdateCacheStateWhenTogglingAvailability() {
        // Arrange: The initial state of the cache is not relevant for this test,
        // as we will explicitly set it.

        // Act & Assert: Part 1 - Disable caching
        LZMAUtils.setCacheLZMAAvailablity(false);
        assertEquals("Disabling caching should set the state to DONT_CACHE.",
                LZMAUtils.CachedAvailability.DONT_CACHE,
                LZMAUtils.getCachedLZMAAvailability());

        // Act & Assert: Part 2 - Enable caching
        LZMAUtils.setCacheLZMAAvailablity(true);
        // When caching is enabled, the state should be updated to reflect availability.
        // It will be either CACHED_AVAILABLE or CACHED_UNAVAILABLE, but not DONT_CACHE.
        assertNotEquals("Enabling caching should update the state away from DONT_CACHE.",
                LZMAUtils.CachedAvailability.DONT_CACHE,
                LZMAUtils.getCachedLZMAAvailability());
    }
}