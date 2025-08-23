package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a null byte array returns null, as per the codec contract.
     */
    @Test
    public void decodeByteArrayShouldReturnNullForNullInput() throws DecoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final byte[] nullInput = null;

        // Act
        final byte[] result = codec.decode(nullInput);

        // Assert
        assertNull("Decoding a null byte array should result in null.", result);
    }
}