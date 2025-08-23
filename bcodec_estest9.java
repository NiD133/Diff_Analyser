package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Provides tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that {@link BCodec#decode(String)} returns null when the input is null.
     * This is a standard, expected behavior for decoders.
     */
    @Test
    public void decodeNullStringShouldReturnNull() throws DecoderException {
        // Arrange
        final BCodec codec = new BCodec();

        // Act
        final String result = codec.decode(null);

        // Assert
        assertNull("Decoding a null input should return null.", result);
    }
}