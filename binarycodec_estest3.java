package org.apache.commons.codec.binary;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link BinaryCodec} class.
 *
 * Note: The original test was for a private static helper method, which is not a recommended practice.
 * This refactored test verifies the same logic (handling of null input) through a public static method,
 * {@code toAsciiBytes(byte[])}, which provides a more robust and meaningful test of the class's contract.
 */
public class BinaryCodecTest {

    /**
     * Tests that toAsciiBytes returns an empty byte array when the input is null.
     */
    @Test
    public void toAsciiBytes_withNullInput_returnsEmptyByteArray() {
        // Arrange: Define the expected output for a null input.
        byte[] expectedOutput = new byte[0];

        // Act: Call the method under test with a null input.
        byte[] actualOutput = BinaryCodec.toAsciiBytes(null);

        // Assert: Verify that the actual output is an empty byte array.
        assertArrayEquals("A null input should result in an empty byte array.", expectedOutput, actualOutput);
    }
}