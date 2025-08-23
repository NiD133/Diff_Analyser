package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link QuotedPrintableCodec} class, focusing on decoding functionality.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the decode(Object) method correctly decodes a string
     * containing a Quoted-Printable encoded space character ('=20').
     */
    @Test
    public void decodeObjectShouldCorrectlyDecodeEncodedSpace() throws DecoderException {
        // Arrange
        // The input string contains "=20", which is the Quoted-Printable representation of a space.
        final String encodedString = "Invalid URL encoding: not a valid digit (radix 16):=20";
        final String expectedDecodedString = "Invalid URL encoding: not a valid digit (radix 16): ";
        
        // The 'strict' parameter does not affect this specific decoding case,
        // but we use it to match the original test's setup.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);

        // Act
        // The test targets the generic decode(Object) method overload.
        final Object result = codec.decode((Object) encodedString);

        // Assert
        assertEquals("The decoded string should have the encoded space converted back to a space character.",
                expectedDecodedString, result);
    }
}