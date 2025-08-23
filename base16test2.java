package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Base16} class, specifically focusing on the encode method
 * that operates on a subsection of a byte array (using an offset and length).
 */
public class Base16Test {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * A helper method to test Base16 encoding on a slice of a larger byte array.
     * It constructs a buffer with optional padding at the beginning and end of the
     * actual data to be encoded.
     *
     * @param prefixPaddingSize The number of zero-bytes to prepend to the data.
     * @param suffixPaddingSize The number of zero-bytes to append to the data.
     */
    private void assertEncodeSlice(final int prefixPaddingSize, final int suffixPaddingSize) {
        // Arrange
        final String originalString = "Hello World";
        final byte[] originalBytes = originalString.getBytes(UTF_8);
        final String expectedEncodedString = "48656C6C6F20576F726C64";

        // Create a buffer with the specified padding around the original data.
        byte[] buffer = ArrayUtils.addAll(new byte[prefixPaddingSize], originalBytes);
        buffer = ArrayUtils.addAll(buffer, new byte[suffixPaddingSize]);

        final Base16 base16 = new Base16();

        // Act: Encode only the slice of the buffer containing the original data.
        final byte[] encodedBytes = base16.encode(buffer, prefixPaddingSize, originalBytes.length);
        final String actualEncodedString = new String(encodedBytes, UTF_8);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString,
            "Encoding a slice of a padded buffer should produce the correct Base16 string.");
    }

    @Test
    void testEncodeWithPrefixPadding() {
        // This test ensures the encoder correctly handles an offset,
        // ignoring any prefix padding in the buffer.
        assertEncodeSlice(100, 0);
    }

    @Test
    void testEncodeWithSuffixPadding() {
        // This test ensures the encoder correctly handles the length parameter,
        // ignoring any suffix padding in the buffer.
        assertEncodeSlice(0, 100);
    }

    @Test
    void testEncodeWithPrefixAndSuffixPadding() {
        // This test ensures the encoder correctly handles both an offset and a length,
        // ignoring padding at both ends of the buffer.
        assertEncodeSlice(50, 50);
    }

    @Test
    void testEncodeWithoutPadding() {
        // This test serves as a baseline, ensuring the helper works correctly
        // in the simplest case with no padding.
        assertEncodeSlice(0, 0);
    }
}