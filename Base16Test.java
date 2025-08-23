package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Base16}.
 */
class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    private Random random;

    @BeforeEach
    void setUp() {
        random = new Random();
    }

    @Test
    void testBase16EncodingAndDecoding() {
        final String original = "Hello World";
        final Base16 base16 = new Base16();

        byte[] encodedBytes = base16.encode(original.getBytes(CHARSET_UTF8));
        String encodedString = new String(encodedBytes, CHARSET_UTF8);
        assertEquals("48656C6C6F20576F726C64", encodedString, "Encoding failed for 'Hello World'");

        byte[] decodedBytes = base16.decode(encodedBytes);
        String decodedString = new String(decodedBytes, CHARSET_UTF8);
        assertEquals(original, decodedString, "Decoding failed for 'Hello World'");
    }

    @Test
    void testBase16EncodingWithBufferPadding() {
        verifyEncodingWithPadding(0, 100);
        verifyEncodingWithPadding(100, 0);
        verifyEncodingWithPadding(100, 100);
    }

    private void verifyEncodingWithPadding(int startPadding, int endPadding) {
        final String original = "Hello World";
        final Base16 base16 = new Base16();
        byte[] originalBytes = original.getBytes(CHARSET_UTF8);

        byte[] paddedBytes = ArrayUtils.addAll(new byte[startPadding], originalBytes);
        paddedBytes = ArrayUtils.addAll(paddedBytes, new byte[endPadding]);

        byte[] encodedBytes = base16.encode(paddedBytes, startPadding, originalBytes.length);
        String encodedString = new String(encodedBytes, CHARSET_UTF8);
        assertEquals("48656C6C6F20576F726C64", encodedString, "Encoding failed with padding");
    }

    @Test
    void testEncodeDecodeEmptyArray() {
        final Base16 base16 = new Base16();

        byte[] emptyArray = {};
        assertArrayEquals(emptyArray, base16.encode(emptyArray), "Encoding empty array failed");
        assertNull(base16.encode(null), "Encoding null array should return null");

        assertArrayEquals(emptyArray, base16.decode(emptyArray), "Decoding empty array failed");
        assertNull(base16.decode((byte[]) null), "Decoding null array should return null");
    }

    @Test
    void testRandomEncodeDecode() {
        for (int i = 0; i < 5; i++) {
            int length = random.nextInt(10000) + 1;
            byte[] randomData = new byte[length];
            random.nextBytes(randomData);

            Base16 base16 = new Base16();
            byte[] encodedData = base16.encode(randomData);
            byte[] decodedData = base16.decode(encodedData);

            assertArrayEquals(randomData, decodedData, "Random data encode/decode failed");
        }
    }

    @Test
    void testKnownEncodingsAndDecodings() {
        assertKnownEncodingDecoding("The quick brown fox jumped over the lazy dogs.", 
                                    "54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e");
        assertKnownEncodingDecoding("It was the best of times, it was the worst of times.", 
                                    "497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e");
        assertKnownEncodingDecoding("http://jakarta.apache.org/commons", 
                                    "687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6f6e73");
    }

    private void assertKnownEncodingDecoding(String original, String expectedEncoded) {
        Base16 base16 = new Base16(true);
        byte[] encodedBytes = base16.encode(original.getBytes(CHARSET_UTF8));
        assertEquals(expectedEncoded, new String(encodedBytes, CHARSET_UTF8), "Encoding failed for known string");

        byte[] decodedBytes = base16.decode(encodedBytes);
        assertEquals(original, new String(decodedBytes, CHARSET_UTF8), "Decoding failed for known string");
    }

    @Test
    void testInvalidDecoding() {
        final byte[] invalidEncodedChars = { '/', ':', '@', 'G', '%', '`', 'g' };
        Base16 base16 = new Base16();

        for (byte invalidChar : invalidEncodedChars) {
            assertThrows(IllegalArgumentException.class, () -> base16.decode(new byte[]{invalidChar}),
                         "Decoding should fail for invalid character: " + (char) invalidChar);
        }
    }

    @Test
    void testObjectEncodeDecode() throws Exception {
        final String original = "Hello World!";
        final Base16 base16 = new Base16();

        Object encoded = base16.encode(original.getBytes(CHARSET_UTF8));
        byte[] decoded = base16.decode((byte[]) encoded);

        assertEquals(original, new String(decoded, CHARSET_UTF8), "Object encode/decode failed");
    }

    @Test
    void testStrictDecodingPolicy() {
        final String encoded = "aabbccdde"; // Trailing 'e' is invalid

        Base16 base16 = new Base16(true, CodecPolicy.STRICT);
        assertThrows(IllegalArgumentException.class, () -> base16.decode(encoded.getBytes(CHARSET_UTF8)),
                     "Strict decoding should fail for incomplete hex pair");
    }

    @Test
    void testLenientDecodingPolicy() {
        final String encoded = "aabbccdde"; // Trailing 'e' is ignored

        Base16 base16 = new Base16(true, CodecPolicy.LENIENT);
        byte[] decoded = base16.decode(encoded.getBytes(CHARSET_UTF8));

        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd}, decoded,
                          "Lenient decoding failed for incomplete hex pair");
    }
}