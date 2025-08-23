package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that getUncompressedFileName returns an empty string when the input is an empty string.
     * This verifies the handling of an edge case.
     */
    @Test
    public void getUncompressedFileNameShouldReturnEmptyStringForEmptyInput() {
        // Arrange
        final String emptyFileName = "";
        final String expectedResult = "";

        // Act
        final String actualResult = LZMAUtils.getUncompressedFileName(emptyFileName);

        // Assert
        assertEquals("An empty file name should remain empty.", expectedResult, actualResult);
    }
}