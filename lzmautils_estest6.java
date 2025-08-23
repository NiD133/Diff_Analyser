package org.apache.commons.compress.compressors.lzma;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that getUncompressedFileName returns an empty string when the input is an empty string.
     * This is the expected behavior, as an empty string has no compression suffix to remove.
     */
    @Test
    public void getUncompressedFileName_shouldReturnEmptyString_whenGivenEmptyString() {
        // Arrange
        final String inputFilename = "";

        // Act
        final String result = LZMAUtils.getUncompressedFileName(inputFilename);

        // Assert
        assertEquals("The method should return the input string unmodified.", "", result);
    }
}