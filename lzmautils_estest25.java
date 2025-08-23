package org.apache.commons.compress.compressors.lzma;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the LZMAUtils class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that getCompressedFileName() correctly appends the ".lzma" suffix
     * to a given filename.
     */
    @Test
    public void getCompressedFileNameShouldAppendLzmaSuffix() {
        // Arrange: Define a sample input filename and the expected output.
        final String inputFileName = "archive.tar";
        final String expectedFileName = "archive.tar.lzma";

        // Act: Call the method under test.
        final String actualFileName = LZMAUtils.getCompressedFileName(inputFileName);

        // Assert: Verify that the actual output matches the expected output.
        assertEquals("The '.lzma' suffix should be appended to the filename.",
                expectedFileName, actualFileName);
    }
}