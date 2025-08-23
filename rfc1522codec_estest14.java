package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link BCodec} class, a concrete implementation of {@link RFC1522Codec}.
 */
public class BCodecTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that attempting to decode a 'Q' (Quoted-Printable) encoded string
     * with a BCodec (which handles 'B' or Base64 encoding) results in a
     * DecoderException. The codec should reject strings with a mismatched
     * encoding identifier.
     */
    @Test
    public void bCodecShouldThrowExceptionWhenDecodingMismatchedQEncoding() throws Exception {
        // Arrange
        final BCodec bCodec = new BCodec();
        // An RFC 1522 encoded-word with 'Q' encoding, which BCodec does not support.
        // Format: =?charset?encoding?encoded-text?=
        final String qEncodedText = "=?UTF-8?Q?Q?=";

        // Assert: Configure the expected exception and its message
        thrown.expect(DecoderException.class);
        thrown.expectMessage("This codec cannot decode Q encoded content");

        // Act: This call is expected to throw the configured exception
        bCodec.decodeText(qEncodedText);
    }
}