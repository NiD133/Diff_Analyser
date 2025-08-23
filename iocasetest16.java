package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the scratch buffer functionality in {@link IOUtils}.
 *
 * Note: The original test was incorrectly located in a test class for {@code IOCase}.
 * This class has been renamed and focused to accurately reflect the code under test.
 */
public class IOUtilsScratchBufferTest {

    @Test
    @DisplayName("Scratch buffers should be zeroed on retrieval and independent of each other")
    void scratchBuffersAreZeroedAndIndependent() {
        // This test verifies two key properties of IOUtils' scratch buffers:
        // 1. A new scratch buffer is always cleared (filled with zeros).
        // 2. The byte scratch buffer and char scratch buffer are independent.

        // --- Part 1: Verify the byte scratch buffer is zeroed ---

        // Act: Get the byte scratch buffer.
        final byte[] byteArray = IOUtils.getScratchByteArrayWriteOnly();

        // Assert: The retrieved byte array should be filled with zeros.
        final byte[] expectedZeroBytes = new byte[byteArray.length];
        assertArrayEquals(expectedZeroBytes, byteArray, "The initial scratch byte array should be zeroed.");

        // --- Part 2: Verify buffer independence ---

        // Arrange: Modify the byte array to simulate its use.
        Arrays.fill(byteArray, (byte) 1);

        // Act: Get the char scratch buffer.
        final char[] charArray = IOUtils.getScratchCharArray();

        // Assert: The char array should be zeroed, unaffected by changes to the byte array.
        final char[] expectedZeroChars = new char[charArray.length];
        assertArrayEquals(expectedZeroChars, charArray, "The scratch char array should be independent and zeroed.");
    }
}