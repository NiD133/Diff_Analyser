package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the matches() method correctly returns false when the provided
     * byte array does not start with the Byte Order Mark.
     */
    @Test
    public void matchesShouldReturnFalseForNonMatchingByteArray() {
        // Arrange: The UTF-16BE BOM is defined as 0xFE, 0xFF.
        // We will test it against an array of zeros, which should not match.
        final ByteOrderMark utf16beBom = ByteOrderMark.UTF_16BE;
        final int[] nonMatchingBytes = new int[8]; // Initialized to all zeros

        // Act: Check if the byte array matches the BOM.
        final boolean isMatch = utf16beBom.matches(nonMatchingBytes);

        // Assert: The result should be false.
        assertFalse("Expected matches() to return false for a non-matching array.", isMatch);
    }
}