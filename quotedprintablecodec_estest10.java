package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the QuotedPrintableCodec class, focusing on understandability.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding a string containing only printable characters
     * (as defined by RFC 1521) results in the original, unchanged string,
     * even when using the strict codec.
     */
    @Test
    public void testEncodeStringWithOnlyPrintableCharsReturnsOriginalStringInStrictMode() {
        // Arrange
        // The 'strict=true' constructor enables encoding that adheres to all rules of RFC 1521.
        // For a short string of printable characters, no encoding should occur.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(true);
        final String plainText = "5-W&+N>@GrPC$hDz.$";
        final Charset charset = Charset.defaultCharset();

        // Act
        final String encodedText = codec.encode(plainText, charset);

        // Assert
        // The encoded string should be identical to the original because all its characters
        // are considered "printable" and do not require Quoted-Printable encoding.
        assertEquals(plainText, encodedText);
    }
}