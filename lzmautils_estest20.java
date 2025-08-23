package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the LZMAUtils class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that isCompressedFilename() returns false for a filename
     * that does not have a recognized LZMA compression suffix (e.g., ".lzma").
     */
    @Test
    public void isCompressedFilenameShouldReturnFalseForUncompressedFilename() {
        // Arrange: A filename without a standard LZMA suffix.
        final String uncompressedFilename = "archive.txt";

        // Act: Check if the filename is identified as compressed.
        final boolean result = LZMAUtils.isCompressedFilename(uncompressedFilename);

        // Assert: The method should return false.
        assertFalse("Filename without .lzma suffix should not be considered compressed.", result);
    }
}