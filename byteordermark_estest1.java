package org.apache.commons.io;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the matches() method correctly returns false for an input
     * that does not start with the expected BOM bytes.
     *
     * <p>This test specifically checks that the UTF-16BE BOM (0xFE, 0xFF) does not
     * match an input array starting with the single integer value of the Unicode
     * BOM character (0xFEFF). This ensures the method compares individual byte
     * values rather than a combined integer.</p>
     */
    @Test
    public void testMatchesReturnsFalseForMismatchedPrefix() {
        // Arrange
        // The UTF-16BE BOM is defined by the sequence of two integers: 0xFE, 0xFF.
        final ByteOrderMark utf16beBom = ByteOrderMark.UTF_16BE;

        // Create an input array that starts with the single Unicode BOM character (0xFEFF).
        // This is different from the first integer (0xFE) of the UTF-16BE BOM.
        final int[] nonMatchingBytes = {(int) ByteOrderMark.UTF_BOM, 0x11, 0x22};

        // Act
        final boolean isMatch = utf16beBom.matches(nonMatchingBytes);

        // Assert
        assertFalse("Expected BOM not to match an incorrect byte prefix", isMatch);
    }
}