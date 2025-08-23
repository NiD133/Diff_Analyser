package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Contains unit tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the internal decoding method correctly handles byte arrays
     * containing invalid Base64 characters. By default, BCodec operates in a
     * lenient mode, which should cause it to ignore any characters that are
     * not part of the Base64 alphabet.
     */
    @Test
    public void doDecodingShouldIgnoreInvalidCharactersInLenientMode() {
        // Arrange
        // The default BCodec constructor creates a decoder with a lenient policy.
        BCodec bCodec = new BCodec();

        // This byte array represents an encoded string with valid Base64 characters ('4', 'K')
        // separated by invalid characters (null bytes). The lenient decoder should
        // skip the null bytes and process the valid ones.
        byte[] encodedBytesWithInvalidChars = new byte[]{
            (byte) '4', 0, 0, 0, 0, 0, (byte) 'K'
        };

        // The effective input for the Base64 decoder is "4K".
        // '4' (index 56) -> 111000
        // 'K' (index 10) -> 001010
        // Concatenated bits: 111000 001010
        // Grouped into 8-bit bytes: 11100000 (224) and 1010 (discarded)
        // The expected result is a single byte with the value 224 (0xE0).
        byte[] expectedDecodedBytes = new byte[]{(byte) 224};

        // Act
        // We test doDecoding directly to isolate the Base64 logic from the
        // RFC 1522 header parsing performed by the public decode() method.
        // This is possible because test classes are in the same package.
        byte[] actualDecodedBytes = bCodec.doDecoding(encodedBytesWithInvalidChars);

        // Assert
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes);
    }
}