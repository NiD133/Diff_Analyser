package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that getUncompressedFilename returns the original filename
     * when the input string does not have a recognized ".lzma" suffix.
     * The method should not modify filenames it doesn't recognize.
     */
    @Test
    public void getUncompressedFilenameShouldReturnOriginalWhenNoLzmaSuffixIsPresent() {
        // Arrange: A filename without a standard LZMA suffix.
        final String inputFilename = "archive.txt";

        // Act: Call the method under test.
        // Note: getUncompressedFilename() is deprecated in favor of getUncompressedFileName().
        // This test verifies the behavior of the legacy method.
        final String resultFilename = LZMAUtils.getUncompressedFilename(inputFilename);

        // Assert: The returned filename should be identical to the input.
        assertEquals("The original filename should be returned for non-lzma files.",
                inputFilename, resultFilename);
    }
}