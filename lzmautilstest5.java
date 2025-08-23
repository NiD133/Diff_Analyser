package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link LZMAUtils#isCompressedFilename(String)} method.
 */
@DisplayName("LZMAUtils.isCompressedFilename")
class LZMAUtilsTest {

    /**
     * Tests that the method correctly identifies valid LZMA filenames.
     * The method should return true for filenames ending with ".lzma" or "-lzma".
     */
    @DisplayName("should return true for valid LZMA filenames")
    @ParameterizedTest
    @ValueSource(strings = {
        "test.lzma",
        "test-lzma"
    })
    @SuppressWarnings("deprecation")
    void shouldIdentifyCompressedFilenames(final String validFilename) {
        assertTrue(LZMAUtils.isCompressedFilename(validFilename), "Failed for new API with filename: " + validFilename);
        assertTrue(LZMAUtils.isCompressedFileName(validFilename), "Failed for deprecated API with filename: " + validFilename);
    }

    /**
     * Tests that the method correctly rejects invalid or malformed filenames.
     * This includes empty strings, filenames that are only the suffix, filenames with the wrong suffix,
     * or filenames where the suffix is not at the very end.
     */
    @DisplayName("should return false for invalid or malformed filenames")
    @ParameterizedTest
    @ValueSource(strings = {
        "",               // Empty string
        ".lzma",          // Suffix only
        "test.lzma.gz",   // Not the final suffix
        "test.lzma ",     // Trailing space
        "test.lzma\n",    // Trailing newline
        "unrelated.gz",   // Wrong suffix
        "lzmafile"        // Suffix is not separated
    })
    @SuppressWarnings("deprecation")
    void shouldRejectInvalidFilenames(final String invalidFilename) {
        assertFalse(LZMAUtils.isCompressedFilename(invalidFilename), "Failed for new API with filename: " + invalidFilename);
        assertFalse(LZMAUtils.isCompressedFileName(invalidFilename), "Failed for deprecated API with filename: " + invalidFilename);
    }
}