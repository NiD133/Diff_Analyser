package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    /**
     * Tests that the equals sign (=), which is the escape character in Quoted-Printable encoding,
     * is itself encoded when it appears in the input string. According to RFC 1521, the '='
     * character (ASCII 61) must be represented by its hexadecimal value "=3D".
     */
    @Test
    @DisplayName("Equals sign '=' in input string should be encoded to '=3D'")
    void testEqualsSignIsEncodedAsHex() throws EncoderException {
        // Arrange
        final String stringWithEqualsSign = "This is a example of a quoted=printable text file. There is no tt";
        final String expectedEncodedString = "This is a example of a quoted=3Dprintable text file. There is no tt";

        // The encoding of the '=' character is a fundamental rule and does not depend on the 'strict' mode.
        // Therefore, we can use the default codec for this test.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        final String actualEncodedString = codec.encode(stringWithEqualsSign);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString, "The equals sign should be encoded as '=3D'.");
    }
}