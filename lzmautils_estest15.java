package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that the matches() method correctly returns false for a byte array
     * that does not contain the LZMA header signature.
     */
    @Test
    public void matchesShouldReturnFalseForNonLzmaSignature() {
        // Arrange: Create a byte array that does not start with the LZMA magic bytes.
        // An all-zero array is a simple example of an invalid signature.
        byte[] nonLzmaSignature = new byte[4];
        int lengthToCheck = nonLzmaSignature.length;

        // Act: Call the matches method with the non-matching signature.
        boolean isMatch = LZMAUtils.matches(nonLzmaSignature, lengthToCheck);

        // Assert: Verify that the method correctly identifies the signature as non-matching.
        assertFalse("An all-zero signature should not be identified as a valid LZMA header.", isMatch);
    }
}