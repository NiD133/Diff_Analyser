package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link LZMAUtils#matches(byte[], int)} method.
 */
@DisplayName("LZMAUtils.matches method")
class LZMAUtilsTest {

    // The 3-byte magic signature for LZMA files.
    private static final byte[] LZMA_SIGNATURE = {(byte) 0x5D, 0, 0};

    @Test
    @DisplayName("should return true for a valid and exact LZMA signature")
    void matchesWithValidAndExactSignature() {
        assertTrue(LZMAUtils.matches(LZMA_SIGNATURE, 3),
            "A 3-byte array containing the exact LZMA signature should match.");
    }

    @Test
    @DisplayName("should return true for a buffer that starts with the LZMA signature")
    void matchesWithBufferStartingWithValidSignature() {
        byte[] dataWithTrailingBytes = {(byte) 0x5D, 0, 0, 'a', 'b', 'c'};
        assertTrue(LZMAUtils.matches(dataWithTrailingBytes, dataWithTrailingBytes.length),
            "A buffer that is longer than the signature but starts with it should match.");
    }

    @Test
    @DisplayName("should return false if the provided length is shorter than the signature")
    void matchesWithLengthShorterThanSignature() {
        assertFalse(LZMAUtils.matches(LZMA_SIGNATURE, 2),
            "Should not match if the length to check is less than the signature's actual length.");
    }

    @Test
    @DisplayName("should return false for a corrupted or invalid signature")
    void matchesWithCorruptedSignature() {
        final byte[] corruptedSignature = {(byte) 0x5D, 0, '0'}; // Last byte is incorrect
        assertFalse(LZMAUtils.matches(corruptedSignature, 3),
            "A corrupted signature should not match.");
    }

    @Test
    @DisplayName("should return false for a completely different signature")
    void matchesWithDifferentSignature() {
        final byte[] otherSignature = {'N', 'O', 'T', '-', 'L', 'Z', 'M', 'A'};
        assertFalse(LZMAUtils.matches(otherSignature, 3),
            "A signature for a different file type should not match.");
    }
}