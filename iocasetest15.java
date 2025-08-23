package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for the scratch buffer methods in {@link IOUtils}.
 *
 * Note: The original test was incorrectly placed in a class named 'IOCaseTestTest15'
 * but was testing methods from 'IOUtils'. This class has been renamed for clarity.
 */
class IOUtilsTest {

    /**
     * Tests that the scratch byte and char arrays provided by IOUtils are independent
     * and are initially filled with zeros. IOUtils uses thread-local buffers for performance,
     * and this test verifies that the buffer for bytes and the buffer for chars do not
     * interfere with each other.
     */
    @Test
    void getScratchArrays_areIndependentAndInitiallyZeroed() {
        // Arrange: No specific setup needed.

        // Act & Assert: Verify the initial state of the byte array
        final byte[] byteArray = IOUtils.getScratchByteArray();
        final int byteArrayLength = byteArray.length;
        assertArrayEquals(new byte[byteArrayLength], byteArray,
            "The initial scratch byte array should be zeroed.");

        // Act: Modify the byte array to test for independence
        Arrays.fill(byteArray, (byte) 1);

        // Act & Assert: Verify the char array is unaffected
        final char[] charArray = IOUtils.getScratchCharArray();
        final int charArrayLength = charArray.length;
        assertArrayEquals(new char[charArrayLength], charArray,
            "The scratch char array should be zeroed and independent of the byte array.");
    }
}