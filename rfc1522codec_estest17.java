package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the RFC1522Codec class, focusing on decoding error handling.
 */
public class RFC1522CodecTest {

    /**
     * Tests that decodeText throws a DecoderException when the input string is malformed
     * because it is missing the required charset token.
     */
    @Test
    public void decodeTextWithMissingCharsetShouldThrowException() {
        // Arrange
        BCodec codec = new BCodec();
        // An RFC 1522 encoded word must have the format "=?charset?encoding?encoded-text?=".
        // This input is malformed because the 'charset' part between the first two '?' is empty.
        String malformedEncodedWord = "=?^-?=";

        // Act & Assert
        try {
            codec.decodeText(malformedEncodedWord);
            fail("Expected DecoderException to be thrown due to missing charset.");
        } catch (DecoderException e) {
            // This is the expected outcome.
            String expectedMessage = "RFC 1522 violation: charset token not found";
            assertEquals(expectedMessage, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // The decodeText method also declares this checked exception.
            // We fail the test if it's thrown, as it's not expected for this specific input.
            fail("Caught an unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }
}