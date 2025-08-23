package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that isCompressedFileName() returns true for a filename
     * ending with the standard ".lzma" suffix.
     */
    @Test
    public void isCompressedFileNameShouldReturnTrueForLzmaSuffix() {
        // Arrange: Define a filename that should be recognized as compressed.
        final String compressedFileName = "archive.lzma";

        // Act: Call the method under test.
        final boolean result = LZMAUtils.isCompressedFileName(compressedFileName);

        // Assert: Verify that the method returned true.
        assertTrue("Filename with .lzma suffix should be detected as compressed", result);
    }
}