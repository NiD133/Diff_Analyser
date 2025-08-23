package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the RFC1522Codec class, focusing on decoding behavior.
 */
public class RFC1522CodecTest {

    /**
     * Tests that the decodeText method correctly throws a DecoderException
     * when presented with a malformed encoded word that is missing the
     * required encoding token ('B' or 'Q').
     */
    @Test
    public void decodeTextShouldThrowDecoderExceptionWhenInputIsMissingEncodingToken() {
        // Arrange: Create a codec instance to test the decoding logic.
        // BCodec is a concrete implementation of RFC1522Codec.
        BCodec bCodec = new BCodec();

        // An RFC 1522 encoded-word must have the form: =?charset?encoding?encoded-text?=
        // The string below is malformed because it is missing the 'encoding' part.
        String malformedEncodedWord = "=?=?SRq9'.C?=";

        // Act & Assert: Verify that decoding the malformed string throws a DecoderException
        // with a specific, informative message.
        try {
            bCodec.decodeText(malformedEncodedWord);
            fail("Expected a DecoderException to be thrown for malformed input.");
        } catch (DecoderException e) {
            // This is the expected outcome. Now, verify the exception message is correct.
            String expectedMessage = "RFC 1522 violation: encoding token not found";
            assertEquals(expectedMessage, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // The decodeText method can also throw this, but it's not expected for this test case.
            // Failing here makes the test more precise about what it's testing.
            fail("Caught an unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }
}