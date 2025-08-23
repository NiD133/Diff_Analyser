package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that encoding a null byte array returns an empty byte array
     * rather than throwing a NullPointerException.
     */
    @Test
    public void encode_givenNullInput_returnsEmptyByteArray() {
        // Arrange
        BinaryCodec codec = new BinaryCodec();
        byte[] nullInput = null;
        byte[] expectedOutput = new byte[0];

        // Act
        byte[] actualOutput = codec.encode(nullInput);

        // Assert
        assertNotNull("The encoded result should not be null.", actualOutput);
        assertArrayEquals("Encoding a null array should produce an empty byte array.", expectedOutput, actualOutput);
    }
}