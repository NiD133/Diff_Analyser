package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;

/**
 * Contains tests for the byte array encoding functionality of {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecEncodingTest {

    @Test
    public void encodeShouldCorrectlyEncodeArrayOfNullBytes() {
        // Arrange
        // A Quoted-Printable codec in non-strict mode.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(false);
        
        // An array of 11 null bytes. Null bytes are non-printable characters
        // that must be encoded.
        final byte[] inputBytes = new byte[11]; // Initialized to all zeros
        
        // According to the Quoted-Printable specification (RFC 1521), each null
        // byte (0x00) must be encoded as "=00".
        final String expectedEncodedString = "=00=00=00=00=00=00=00=00=00=00=00";
        final byte[] expectedEncodedBytes = expectedEncodedString.getBytes(StandardCharsets.US_ASCII);

        // Act
        final byte[] actualEncodedBytes = codec.encode(inputBytes);

        // Assert
        // This assertion verifies the actual content of the encoded array, which is a much
        // stronger guarantee of correctness than the original test's assertNotSame.
        assertArrayEquals(
            "An array of 11 null bytes should be encoded to 11 repetitions of '=00'",
            expectedEncodedBytes,
            actualEncodedBytes
        );
    }
}