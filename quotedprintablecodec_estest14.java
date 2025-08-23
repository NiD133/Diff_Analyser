package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding a string containing only printable ASCII characters
     * in non-strict mode results in the original, unchanged string.
     *
     * <p>In non-strict mode, Quoted-Printable encoding should not modify
     * characters that are considered printable (most ASCII symbols and alphanumerics),
     * as they do not require escaping.</p>
     */
    @Test
    public void testEncodeWithPrintableCharsInNonStrictModeReturnsUnchangedString() throws EncoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(false); // Use non-strict mode
        final String originalString = "HKPw\"0YG.> 2u"; // This string contains only printable characters

        // Act
        final String encodedString = codec.encode(originalString);

        // Assert
        assertEquals("Encoding a string of printable characters in non-strict mode should not change the string.",
                originalString, encodedString);
    }
}