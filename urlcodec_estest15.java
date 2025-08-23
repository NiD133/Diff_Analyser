package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the decode(byte[]) method correctly handles a null input
     * by returning null, as specified by the contract for many codecs.
     */
    @Test
    public void decodeByteArrayShouldReturnNullForNullInput() throws DecoderException {
        // Arrange
        URLCodec urlCodec = new URLCodec(); // Use the default constructor for simplicity.
        byte[] nullInput = null;

        // Act
        byte[] result = urlCodec.decode(nullInput);

        // Assert
        assertNull("Decoding a null byte array should result in null.", result);
    }
}