package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that the getCompressedFileName method correctly appends the ".lzma"
     * suffix to a given filename.
     */
    @Test
    public void getCompressedFileNameShouldAppendLzmaSuffix() {
        // Arrange
        final String inputFilename = "somefile";
        final String expectedCompressedFilename = "somefile.lzma";

        // Act
        // The original test targeted the deprecated `getCompressedFilename` method.
        // This improved test validates its non-deprecated replacement, `getCompressedFileName`.
        final String actualCompressedFilename = LZMAUtils.getCompressedFileName(inputFilename);

        // Assert
        assertEquals("The compressed filename should have the .lzma suffix.",
                     expectedCompressedFilename, actualCompressedFilename);
    }
}