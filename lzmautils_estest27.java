package org.apache.commons.compress.compressors.lzma;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that getUncompressedFileName() returns the original filename
     * when the input does not have a recognized LZMA suffix.
     */
    @Test
    public void getUncompressedFileNameShouldReturnInputWhenNoLzmaSuffixIsPresent() {
        // Arrange: A filename without a .lzma or -lzma suffix
        final String inputFileName = "archive.txt";

        // Act: Call the method to get the uncompressed filename
        final String uncompressedFileName = LZMAUtils.getUncompressedFileName(inputFileName);

        // Assert: The returned filename should be identical to the input
        assertEquals(inputFileName, uncompressedFileName);
    }
}