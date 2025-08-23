package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the utility methods in {@link LZMAUtils}.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that the isCompressedFileName() method correctly identifies a file name
     * as uncompressed when it does not end with a standard LZMA suffix (e.g., ".lzma").
     */
    @Test
    public void isCompressedFileNameShouldReturnFalseForFileNameWithoutLzmaSuffix() {
        // Arrange: Define a file name that is clearly not an LZMA compressed file.
        final String uncompressedFileName = "archive.txt";

        // Act: Call the method under test.
        final boolean result = LZMAUtils.isCompressedFileName(uncompressedFileName);

        // Assert: The method should return false.
        assertFalse("A file name without a .lzma suffix should not be identified as compressed.", result);
    }
}