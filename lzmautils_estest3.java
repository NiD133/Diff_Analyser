package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that {@link LZMAUtils#isCompressedFileName(String)} correctly identifies
     * a filename with the ".lzma" extension as a compressed file.
     */
    @Test
    public void isCompressedFileNameShouldReturnTrueForLzmaExtension() {
        // Arrange: A filename with a standard .lzma extension.
        final String lzmaFilename = "archive.lzma";

        // Act: Check if the filename is identified as compressed.
        final boolean isCompressed = LZMAUtils.isCompressedFileName(lzmaFilename);

        // Assert: The result should be true.
        assertTrue("Filename with .lzma extension should be identified as compressed.", isCompressed);
    }
}