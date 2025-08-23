package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;

/**
 * This class contains an improved version of a test case for the QuotedPrintableCodec.
 * The original test was auto-generated and lacked clarity.
 */
public class QuotedPrintableCodecImprovedTest {

    /**
     * Tests that the static {@code encodeQuotedPrintable} method, when used in strict mode,
     * correctly encodes an array of zero-bytes and applies soft line breaks as per RFC 1521.
     *
     * <p>This test verifies two key behaviors:
     * <ul>
     *   <li>Passing a {@code null} BitSet for printable characters falls back to the default behavior.</li>
     *   <li>The {@code strict=true} parameter correctly enables line wrapping for output longer than the
     *       safe line length (73 characters).</li>
     * </ul>
     * </p>
     */
    @Test
    public void encodeQuotedPrintableWithStrictTrueShouldWrapLongLines() {
        // Arrange
        // An array of 38 zero-bytes. The byte 0x00 is not a printable ASCII character
        // and must be escaped as "=00".
        final byte[] inputBytes = new byte[38];

        // The encoded form of 38 zero-bytes is 38 * 3 = 114 characters ("=00" for each byte).
        // In strict mode, lines are wrapped to a maximum of 76 characters, with a soft
        // line break ("=\r\n") inserted before the limit is exceeded.
        //
        // Calculation:
        // - Each "=00" is 3 bytes.
        // - The safe line length is 73 characters. 73 / 3 = 24.33.
        // - So, 24 encoded bytes (24 * 3 = 72 characters) fit on the first line.
        // - A soft line break "=\r\n" (3 bytes) is added.
        // - The remaining 38 - 24 = 14 bytes are encoded on the second line (14 * 3 = 42 characters).
        // - Total length = 72 (line 1) + 3 (break) + 42 (line 2) = 117 bytes.
        final StringBuilder expectedEncodedString = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            expectedEncodedString.append("=00");
        }
        expectedEncodedString.append("=\r\n"); // Soft line break
        for (int i = 0; i < 14; i++) {
            expectedEncodedString.append("=00");
        }
        final byte[] expectedBytes = expectedEncodedString.toString().getBytes(StandardCharsets.US_ASCII);

        // Act
        // A null BitSet should cause the method to use the default set of printable characters.
        final byte[] encodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, inputBytes, true);

        // Assert
        assertArrayEquals(expectedBytes, encodedBytes);
    }
}