package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the RFC1522Codec class, focusing on decoding malformed inputs.
 */
public class RFC1522CodecTest {

    /**
     * Tests that decodeText() throws a DecoderException when presented with an
     * encoded word that is missing the required charset definition.
     *
     * According to RFC 1522, an encoded word must have the format:
     * "=?charset?encoding?encoded-text?="
     */
    @Test
    public void decodeTextShouldThrowExceptionForMissingCharset() {
        // Arrange
        BCodec codec = new BCodec();
        // This input is a malformed encoded-word, as it lacks a charset.
        final String encodedWordWithMissingCharset = "=??-?=";
        final String expectedErrorMessage = "RFC 1522 violation: charset not specified";

        // Act & Assert
        try {
            codec.decodeText(encodedWordWithMissingCharset);
            fail("Expected a DecoderException to be thrown for a missing charset.");
        } catch (final DecoderException e) {
            // Verify that the exception message correctly identifies the problem.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}