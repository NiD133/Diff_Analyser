package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec} focusing on specific decoding scenarios.
 */
// The original class name "URLCodecTestTest3" was renamed for clarity.
public class URLCodecTest {

    /**
     * This test verifies that the {@code decode(byte[])} method correctly handles
     * byte arrays that do not contain any URL-encoded sequences (i.e., no '%'
     * escape characters or '+' for spaces). In this scenario, the method should
     * return the input byte array unmodified.
     *
     * The original test was named "testDecodeInvalidContent", which was misleading
     * as the input is valid but simply contains nothing to decode.
     */
    @Test
    @DisplayName("decode(byte[]) should pass through bytes that are not URL-encoded")
    void decodeByteArrayWithNoEncodedCharsReturnsSameArray() throws DecoderException {
        // Arrange
        // The original test used a helper method to build this string from an array of
        // Unicode code points. Using a string literal is more direct and readable.
        // The string contains non-ASCII characters ('ü', 'ä') to ensure the test
        // correctly handles byte values greater than 127.
        final String originalString = "Grüezi_zämä";
        final byte[] inputBytes = originalString.getBytes(StandardCharsets.ISO_8859_1);

        final URLCodec urlCodec = new URLCodec();

        // Act
        // The decode method is expected to iterate through the bytes and, finding
        // no characters to decode, return the original byte array.
        final byte[] resultBytes = urlCodec.decode(inputBytes);

        // Assert
        // The output should be identical to the input.
        // The original test used a for-loop for comparison, but assertArrayEquals
        // is more concise and idiomatic for this purpose.
        assertArrayEquals(inputBytes, resultBytes);
    }
}