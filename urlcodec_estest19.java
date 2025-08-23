package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that decoding an empty string results in an empty string,
     * ensuring the codec handles this edge case gracefully.
     */
    @Test
    public void decodeEmptyStringShouldReturnEmptyString() throws DecoderException {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String emptyInput = "";

        // Act
        String decodedString = urlCodec.decode(emptyInput);

        // Assert
        assertNotNull("The decoded string should not be null.", decodedString);
        assertEquals("Decoding an empty string should result in an empty string.", emptyInput, decodedString);
    }
}