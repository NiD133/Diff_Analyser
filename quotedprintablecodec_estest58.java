package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertArrayEquals;

/**
 * This test class contains improved tests for the QuotedPrintableCodec.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the strict encoding mode correctly escapes non-printable characters (like null bytes)
     * and whitespace characters (like TAB). In strict mode, both are expected to be encoded
     * into their "=HH" hexadecimal representation.
     */
    @Test
    public void encodeQuotedPrintableInStrictModeShouldEscapeNonPrintableAndWhitespaceChars() {
        // Arrange
        // An input byte array containing non-printable null bytes and a TAB character.
        final byte[] plainBytes = {0, 0, 0, 0, 0, 0, '\t', 0, 0};

        // In strict Quoted-Printable encoding, non-printable characters and special whitespace
        // like TAB are escaped with an '=' followed by their two-digit hex value.
        // 0x00 -> "=00"
        // 0x09 (TAB) -> "=09"
        final byte[] expectedEncodedBytes = "=00=00=00=00=00=00=09=00=00".getBytes(StandardCharsets.US_ASCII);

        // Act
        // Encode the byte array using the strict Quoted-Printable rules.
        // Passing null for the BitSet uses the default set of printable characters.
        final byte[] actualEncodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, plainBytes, true);

        // Assert
        // Verify that the actual encoded output matches the expected byte sequence exactly.
        assertArrayEquals(expectedEncodedBytes, actualEncodedBytes);
    }
}