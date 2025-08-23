package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Base16} class.
 */
// Renamed from Base16TestTest9 to a more standard name.
public class Base16Test {

    // Note: This helper method is not called by the provided test case,
    // but is improved here assuming it's used by other tests in the same class.
    /**
     * Verifies that encoding a slice of a byte array (using offset and length) works correctly.
     *
     * @param startPadSize The number of bytes to pad at the beginning of the buffer.
     * @param endPadSize   The number of bytes to pad at the end of the buffer.
     */
    private void verifyEncodeWithBufferOffset(final int startPadSize, final int endPadSize) {
        // Arrange
        final String originalString = "Hello World";
        final String expectedEncodedString = "48656C6C6F20576F726C64";
        final byte[] originalBytes = StringUtils.getBytesUtf8(originalString);

        // Create a larger buffer with padding at the start and end.
        byte[] buffer = ArrayUtils.addAll(new byte[startPadSize], originalBytes);
        buffer = ArrayUtils.addAll(buffer, new byte[endPadSize]);

        final Base16 base16 = new Base16();

        // Act
        // Encode only the relevant slice of the buffer, not the padding.
        final byte[] encodedBytes = base16.encode(buffer, startPadSize, originalBytes.length);
        final String actualEncodedString = StringUtils.newStringUtf8(encodedBytes);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString,
            "Encoding a slice of a buffer should produce the same result as encoding the original array.");
    }

    /**
     * Tests that the constructor with `lowerCase=false` and a `STRICT` policy
     * correctly creates an encoder that produces upper-case hexadecimal strings.
     */
    @Test
    void testConstructorWithUpperCaseAndStrictPolicyEncodesToUpperCase() {
        // Arrange
        final String expectedEncoded = Base16TestData.ENCODED_UTF8_UPPERCASE;
        // The 'lowerCase' parameter is false, so we expect an upper-case alphabet.
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);

        // Act
        final byte[] actualEncodedBytes = base16.encode(BaseNTestData.DECODED);
        final String actualEncodedString = StringUtils.newStringUtf8(actualEncodedBytes);

        // Assert
        // Standard assertEquals(expected, actual, message) order is used for clarity.
        assertEquals(expectedEncoded, actualEncodedString,
            "Encoding with lowerCase=false should produce upper-case hex characters.");
    }
}