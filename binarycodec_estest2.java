package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that toByteArray returns a correctly sized, zeroed byte array
     * when the input string contains no '1' characters.
     */
    @Test
    public void toByteArray_whenInputContainsNoOnes_returnsZeroedBytes() {
        // Arrange: Set up the test objects and data.
        BinaryCodec binaryCodec = new BinaryCodec();
        
        // The toByteArray() method processes strings in 8-character chunks from right to left.
        // Any character that is not '1' is treated as a '0'.
        // An input string of length 12 results in a 1-byte array (12 / 8 = 1).
        String inputWithNoOnes = "\"S|Pn_%?u{!|"; // Original 12-character input
        byte[] expectedBytes = {0};

        // Act: Call the method under test.
        byte[] actualBytes = binaryCodec.toByteArray(inputWithNoOnes);

        // Assert: Verify the result.
        assertArrayEquals("The byte array should contain a single zero", expectedBytes, actualBytes);
    }
}