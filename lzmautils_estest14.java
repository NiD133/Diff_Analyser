package org.apache.commons.compress.compressors.lzma;

import org.apache.commons.compress.compressors.lzma.LZMAUtils.CachedAvailability;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests the caching mechanism for LZMA availability in {@link LZMAUtils}.
 *
 * This class focuses on verifying the behavior of the
 * {@link LZMAUtils#setCacheLZMAAvailablity(boolean)} method.
 */
public class LZMAUtilsCachingTest {

    /**
     * Resets the cache state after each test to ensure test isolation.
     * Since the cache availability is a static field, failing to reset it
     * could cause subsequent tests to behave unpredictably.
     */
    @After
    public void tearDown() {
        // Set caching to false, which resets the state to DONT_CACHE.
        LZMAUtils.setCacheLZMAAvailablity(false);
    }

    @Test
    public void shouldEnableLzmaAvailabilityCaching() {
        // Arrange: The @After method ensures the cache is already disabled.
        // We can assert this precondition for clarity.
        assertEquals("Precondition: Caching should be disabled.",
                CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

        // Act: Enable caching and then trigger the availability check, which populates the cache.
        LZMAUtils.setCacheLZMAAvailablity(true);
        LZMAUtils.isLZMACompressionAvailable(); // This call is necessary to populate the cache.

        // Assert: The cache state should now be populated (either AVAILABLE or UNAVAILABLE).
        CachedAvailability currentState = LZMAUtils.getCachedLZMAAvailability();
        assertThat("After enabling, the cache state should be populated.",
                currentState, is(not(CachedAvailability.DONT_CACHE)));
        assertTrue("The cache state must be either CACHED_AVAILABLE or CACHED_UNAVAILABLE.",
                currentState == CachedAvailability.CACHED_AVAILABLE ||
                currentState == CachedAvailability.CACHED_UNAVAILABLE);
    }

    @Test
    public void shouldDisableLzmaAvailabilityCaching() {
        // Arrange: Start with caching enabled and populated to test the disabling functionality.
        LZMAUtils.setCacheLZMAAvailablity(true);
        LZMAUtils.isLZMACompressionAvailable(); // Populate the cache.
        assertThat("Precondition: Caching should be enabled and populated.",
                LZMAUtils.getCachedLZMAAvailability(), is(not(CachedAvailability.DONT_CACHE)));

        // Act: Disable caching.
        LZMAUtils.setCacheLZMAAvailablity(false);

        // Assert: The cache state should be reset to DONT_CACHE.
        assertEquals("After disabling, the cache state should be reset to DONT_CACHE.",
                CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
    }
}