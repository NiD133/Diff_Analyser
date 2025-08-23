package org.apache.commons.compress.compressors.lzma;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that isLZMACompressionAvailable() correctly returns false
     * when the underlying LZMA implementation (from the optional XZ for Java library)
     * is not present on the classpath.
     *
     * <p>This test relies on the build environment being configured without the
     * optional LZMA dependency during this specific test run.</p>
     */
    @Test
    public void isLzmaCompressionAvailableShouldReturnFalseWhenDependenciesAreMissing() {
        // Act: Check for LZMA availability.
        boolean isAvailable = LZMAUtils.isLZMACompressionAvailable();

        // Assert: The result should be false, as the required classes are not on the classpath.
        assertFalse("LZMA should be reported as unavailable when its library is missing.", isAvailable);
    }
}