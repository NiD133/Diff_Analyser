package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the LZMAUtils class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that the matches() method correctly identifies that an empty byte array
     * (or any buffer checked with a length of zero) does not contain the
     * LZMA magic header.
     */
    @Test
    public void matchesShouldReturnFalseForZeroLengthInput() {
        // Arrange: Create an empty byte array to represent the file signature.
        final byte[] emptySignature = new byte[0];

        // Act: Check if the first 0 bytes match the LZMA signature.
        final boolean isMatch = LZMAUtils.matches(emptySignature, 0);

        // Assert: The result should be false, as a zero-length buffer cannot
        // contain the required 3-byte magic number.
        assertFalse("A zero-length signature should not match.", isMatch);
    }
}