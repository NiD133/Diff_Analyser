package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the static utility methods in {@link LZMAUtils}.
 */
class LZMAUtilsTest {

    /**
     * Verifies that LZMA availability is checked and cached when the LZMAUtils class
     * is initialized.
     *
     * <p>In a standard test environment (non-OSGi), the static initializer in
     * {@link LZMAUtils} should detect that the required LZMA libraries are present
     * and cache this "available" status for future calls.</p>
     */
    @Test
    @DisplayName("Should detect and cache LZMA availability on class initialization")
    void shouldDetectAndCacheLzmaAvailabilityOnStartup() {
        // This test relies on the static initializer of LZMAUtils.
        // We assume the XZ for Java library is on the classpath, making LZMA available.

        assertAll("Default LZMA Availability State",
            () -> {
                // Check the internal cache state to confirm availability was cached.
                // This is the primary assertion about the caching mechanism itself.
                assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability(),
                    "The availability status should be cached as AVAILABLE.");
            },
            () -> {
                // Check the public-facing method, which should now use the cached result.
                // This confirms the cached value is correctly interpreted.
                assertTrue(LZMAUtils.isLZMACompressionAvailable(),
                    "isLZMACompressionAvailable() should return true, reflecting the cached state.");
            }
        );
    }
}