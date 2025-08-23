package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that when caching is disabled, isLZMACompressionAvailable()
     * performs a direct check for LZMA availability. In the context of this
     * test run, the underlying check is expected to return false.
     */
    @Test
    public void isLzmaAvailableShouldReturnUnderlyingResultWhenCacheIsDisabled() {
        // Arrange: Ensure that the availability check is not cached.
        // This forces LZMAUtils to perform a real check for the required classes.
        LZMAUtils.setCacheLZMAAvailablity(false);

        // Act: Check for LZMA compression availability.
        final boolean isAvailable = LZMAUtils.isLZMACompressionAvailable();

        // Assert: Verify that LZMA compression is reported as unavailable.
        // This is the expected outcome when the necessary LZMA library (XZ for Java)
        // is not present in the test environment.
        assertFalse("LZMA compression should be unavailable when caching is disabled and dependencies are missing.", isAvailable);
    }
}