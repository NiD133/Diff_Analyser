package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that the matches() method correctly identifies a byte array
     * that begins with the standard LZMA magic header.
     */
    @Test
    public void matchesShouldReturnTrueForValidLzmaHeader() {
        // Arrange: The LZMA magic header is the byte sequence { 0x5D, 0, 0 }.
        // We create a sample byte array that simulates the beginning of an LZMA file.
        byte[] signature = new byte[] { (byte) 0x5D, 0, 0, 's', 'o', 'm', 'e', '-', 'd', 'a', 't', 'a' };

        // Act: Check if the beginning of the byte array matches the LZMA signature.
        // The method only requires the first 3 bytes for a positive match.
        boolean isMatch = LZMAUtils.matches(signature, signature.length);

        // Assert
        assertTrue("The signature should be identified as a valid LZMA header.", isMatch);
    }
}