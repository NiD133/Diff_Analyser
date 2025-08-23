package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CodecPolicy;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Base16} class, focusing on its decoding behavior.
 */
public class Base16Test {

    /**
     * Tests that decoding with a lenient policy on an input string with an odd number of
     * characters will ignore the final, incomplete character.
     *
     * <p>According to the Base16 specification, two hexadecimal characters are required to form a
     * single byte. A lenient decoder should process as many full pairs as possible and
     * discard the trailing single character without throwing an error.</p>
     */
    @Test
    public void testLenientDecodingWithOddLengthStringShouldIgnoreLastCharacter() {
        // Arrange
        // The input string "aabbccdde" has an odd length (9). The last character 'e'
        // does not form a complete hex pair and should be ignored by a lenient decoder.
        final String encodedStringWithOddLength = "aabbccdde";
        final byte[] expectedDecodedBytes = {(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd};

        // The Base16 instance is configured to use a lower-case alphabet and a lenient decoding policy.
        final Base16 base16 = new Base16(true, CodecPolicy.LENIENT);
        assertEquals(CodecPolicy.LENIENT, base16.getCodecPolicy(), "Precondition: Codec policy should be LENIENT.");

        // Act
        final byte[] actualDecodedBytes = base16.decode(encodedStringWithOddLength.getBytes(StandardCharsets.UTF_8));

        // Assert
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes,
            "Decoding should ignore the trailing character of an odd-length string.");
    }
}