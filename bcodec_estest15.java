package org.apache.commons.codec.net;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BCodecTest {

    @Test
    public void strictDecodingWithInvalidLengthInputShouldThrowException() {
        // Arrange
        // A BCodec instance configured for STRICT decoding, which enforces valid Base64 rules.
        final BCodec bCodec = new BCodec(Charset.defaultCharset(), CodecPolicy.STRICT);

        // A single valid Base64 character ('-') is an invalid sequence.
        // Base64 decoding requires character groups of four. A single character at the
        // end of the data is not a possible encoding.
        final byte[] invalidBase64Bytes = new byte[]{'-'};

        // Act & Assert
        try {
            bCodec.doDecoding(invalidBase64Bytes);
            fail("Expected an IllegalArgumentException for an incomplete Base64 sequence in STRICT mode.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Strict decoding: Last encoded character (before the paddings if any) " +
                    "is a valid base 64 alphabet but not a possible encoding. " +
                    "Decoding requires at least two trailing 6-bit characters to create bytes.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}