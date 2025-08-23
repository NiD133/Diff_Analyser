package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Contains tests for RFC1522Codec implementations, focusing on the BCodec.
 * This class demonstrates improvements in test code understandability.
 */
public class BCodecTest {

    /**
     * Tests that the BCodec's encodeText method correctly encodes a string that itself
     * contains characters used in the RFC 1522 syntax ('=' and '?').
     *
     * <p>The test ensures that such a string is treated as literal text and Base64 encoded,
     * rather than being misinterpreted. This confirms the codec's robustness when handling
     * potentially ambiguous input.</p>
     */
    @Test(timeout = 4000)
    public void testEncodeTextWithRfc1522SyntaxCharacters() throws EncoderException {
        // Arrange: Set up the test objects and data.
        final BCodec bCodec = new BCodec(StandardCharsets.UTF_8);
        
        // This input string resembles an RFC 1522 encoded string. The purpose of this
        // test is to verify that the codec encodes it as a literal string, without
        // attempting to interpret its structure.
        final String textToEncode = "=?=?=?UTF-8?Q?Q?=";

        // This is the expected result: the Base64 encoding of the UTF-8 bytes of the
        // input string, wrapped in the standard RFC 1522 "B" encoding syntax.
        final String expectedEncodedText = "=?UTF-8?B?PT89Pz0/VVRGLTg/UT9RPz0=?=";

        // Act: Execute the method under test.
        final String actualEncodedText = bCodec.encodeText(textToEncode, StandardCharsets.UTF_8);

        // Assert: Verify the outcome.
        assertEquals(expectedEncodedText, actualEncodedText);
    }
}