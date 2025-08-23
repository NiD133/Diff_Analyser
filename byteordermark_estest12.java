package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the matches() method correctly returns false when the input array
     * is shorter than the Byte Order Mark's byte sequence.
     */
    @Test
    public void matches_shouldReturnFalse_whenInputIsShorterThanBom() {
        // Arrange
        // The UTF-8 BOM is 3 bytes long (0xEF, 0xBB, 0xBF).
        final ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;
        // Create an input array that is shorter than the 3-byte BOM.
        final int[] inputBytesShorterThanBom = { 0xEF, 0xBB };

        // Act
        final boolean isMatch = utf8Bom.matches(inputBytesShorterThanBom);

        // Assert
        assertFalse("A BOM should not match an array that is shorter than the BOM itself.", isMatch);
    }
}