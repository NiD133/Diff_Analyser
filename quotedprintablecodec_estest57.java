package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;

/**
 * Tests for the static methods of the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the static {@code encodeQuotedPrintable} method correctly encodes
     * an array of bytes containing non-printable characters.
     */
    @Test
    public void encodeQuotedPrintableWithUnsafeBytesShouldReturnCorrectlyEncodedArray() {
        // Arrange
        // The input contains a non-printable byte (-56, which is 0xC8 in hex) and several
        // null bytes (0x00). Both are considered "unsafe" and require encoding.
        final byte[] unsafeBytes = new byte[]{0, 0, 0, 0, 0, 0, (byte) -56, 0, 0};

        // According to RFC 1521, non-printable bytes must be encoded in the "=HH" format,
        // where HH is the two-digit hexadecimal representation of the byte.
        // Therefore, the null byte (0x00) becomes "=00" and 0xC8 becomes "=C8".
        final String expectedEncodedString = "=00=00=00=00=00=00=C8=00=00";
        final byte[] expectedBytes = expectedEncodedString.getBytes(StandardCharsets.US_ASCII);

        // Act
        // We pass 'null' for the BitSet of printable characters, which causes the method
        // to use its default set.
        final byte[] actualEncodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, unsafeBytes);

        // Assert
        // Verify that the actual encoded output matches the expected byte-for-byte.
        assertArrayEquals(expectedBytes, actualEncodedBytes);
    }
}