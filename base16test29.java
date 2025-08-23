package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.CodecPolicy;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the {@link Base16} codec when using the {@link CodecPolicy#STRICT} policy.
 * This test focuses on handling invalid input that should be rejected in strict mode.
 */
public class Base16StrictDecodingTest {

    /**
     * Tests that decoding a Base16 string with an odd number of characters
     * throws an {@link IllegalArgumentException} when the codec is in STRICT mode.
     * According to RFC 4648, Base16 encoded data must have an even number of characters,
     * as each byte of original data is represented by two hexadecimal characters.
     */
    @Test
    void strictDecodingShouldThrowExceptionForInputWithOddLength() {
        // Arrange
        // The trailing 'e' gives this string an odd length (9), which is invalid for Base16.
        final String invalidEncodedString = "aabbccdde";
        final byte[] encodedBytes = StringUtils.getBytesUtf8(invalidEncodedString);
        final Base16 base16Strict = new Base16(true, CodecPolicy.STRICT);

        // Sanity check to ensure the test setup is correct.
        assertEquals(CodecPolicy.STRICT, base16Strict.getCodecPolicy(), "Precondition failed: Codec was not in STRICT mode.");

        // Act & Assert
        // The decode operation is expected to fail fast on invalid input length in strict mode.
        assertThrows(IllegalArgumentException.class,
            () -> base16Strict.decode(encodedBytes),
            "Decoding a string with an odd number of characters should fail in strict mode."
        );
    }
}