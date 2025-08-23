package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
// The class name is simplified for clarity, assuming it's part of a larger test suite.
// public class QuotedPrintableCodecTest {
public class QuotedPrintableCodec_ESTestTest12 extends QuotedPrintableCodec_ESTest_scaffolding { // Retaining original class for context

    /**
     * Tests that encoding a string containing only printable ASCII characters
     * in non-strict mode results in the original, unchanged string.
     */
    @Test
    public void encodeStringWithPrintableAsciiShouldNotChangeInput() throws UnsupportedEncodingException {
        // Arrange
        // The 'false' argument creates the codec in non-strict mode.
        QuotedPrintableCodec codec = new QuotedPrintableCodec(false);
        String plainText = "This is a simple string with numbers 123 and symbols like ./-_";
        String expectedEncodedText = plainText;

        // Act
        String actualEncodedText = codec.encode(plainText, "UTF-8");

        // Assert
        assertEquals("Encoding a string of printable characters should not alter it.",
                expectedEncodedText, actualEncodedText);
    }
}