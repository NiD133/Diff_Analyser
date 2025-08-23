package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the BCodec class.
 */
public class BCodecTest {

    /**
     * Tests that decoding a null byte array returns null, which is the expected behavior
     * for lenient decoding of invalid input.
     */
    @Test
    public void doDecodingWithNullInputShouldReturnNull() {
        // Arrange
        BCodec codec = new BCodec();

        // Act
        byte[] result = codec.doDecoding(null);

        // Assert
        assertNull("Decoding a null byte array should result in null.", result);
    }
}