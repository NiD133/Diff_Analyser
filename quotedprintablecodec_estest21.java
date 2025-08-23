package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a string that contains only printable ASCII characters
     * (and thus is not actually Quoted-Printable encoded) returns the original string.
     * This is effectively a "no-op" decoding scenario.
     */
    @Test
    public void decodeUnencodedStringShouldReturnOriginalString() throws Exception {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String originalString = "UTF-8"; // A simple string with no characters that need decoding.

        // Act
        final String decodedString = codec.decode(originalString, "UTF-8");

        // Assert
        assertEquals("The decoded string should be identical to the original.", originalString, decodedString);
    }
}