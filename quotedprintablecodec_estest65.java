package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link QuotedPrintableCodec} class, focusing on decoding behavior.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when the input string
     * ends with an incomplete escape sequence (a lone '=' character). According to
     * RFC 1521, an '=' must be followed by two hexadecimal digits.
     */
    @Test
    public void decodeShouldThrowExceptionForStringEndingWithIncompleteEscapeSequence() {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // This input is invalid because it ends with the escape character '=',
        // which is not followed by the required two hexadecimal digits.
        String invalidQuotedPrintableString = "|ZA, Gnd<#@c1f|=";

        try {
            // Act: Attempt to decode the invalid string.
            // A valid charset is used to demonstrate that the error is in the input
            // string's format, not the charset.
            codec.decode(invalidQuotedPrintableString, "UTF-8");
            fail("Expected a DecoderException to be thrown due to the incomplete escape sequence.");
        } catch (DecoderException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            assertEquals("Invalid quoted-printable encoding", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // This exception would indicate a problem with the test setup (e.g., an invalid charset),
            // not the logic under test.
            fail("Test configuration error: UTF-8 encoding should be supported.");
        }
    }
}