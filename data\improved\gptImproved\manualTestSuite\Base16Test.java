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
 * Unit tests for the Base16 encoding and decoding.
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

        // Encode
        final byte[] encodedBytes = base16.encode(original.getBytes(CHARSET_UTF8));
        final String encodedString = new String(encodedBytes, CHARSET_UTF8);
        assertEquals("48656C6C6F20576F726C64", encodedString, "Encoding failed for 'Hello World'");

        // Decode
        final byte[] decodedBytes = base16.decode(encodedBytes);
        final String decodedString = new String(decodedBytes, CHARSET_UTF8);
        assertEquals(original, decodedString, "Decoding failed for 'Hello World'");
    }

    @Test
    void testBase16EncodingAtBufferPositions() {
        testBase16EncodingWithBufferPadding(0, 100); // Start
        testBase16EncodingWithBufferPadding(100, 0); // End
        testBase16EncodingWithBufferPadding(100, 100); // Middle
    }

    private void testBase16EncodingWithBufferPadding(final int startPadding, final int endPadding) {
        final String content = "Hello World";
        final byte[] contentBytes = content.getBytes(CHARSET_UTF8);
        byte[] buffer = ArrayUtils.addAll(new byte[startPadding], contentBytes);
        buffer = ArrayUtils.addAll(buffer, new byte[endPadding]);

        final Base16 base16 = new Base16();
        final byte[] encodedBytes = base16.encode(buffer, startPadding, contentBytes.length);
        final String encodedString = new String(encodedBytes, CHARSET_UTF8);

        assertEquals("48656C6C6F20576F726C64", encodedString, "Encoding failed with buffer padding");
    }

    @Test
    void testByteToStringEncodingVariations() {
        final Base16 base16 = new Base16();
        final byte[] helloWorldBytes = "Hello World".getBytes(CHARSET_UTF8);
        final byte[] emptyBytes = {};
        final byte[] nullBytes = null;

        assertEquals("48656C6C6F20576F726C64", base16.encodeToString(helloWorldBytes), "Encoding failed for 'Hello World'");
        assertEquals("", base16.encodeToString(emptyBytes), "Encoding failed for empty byte array");
        assertNull(base16.encodeToString(nullBytes), "Encoding failed for null byte array");
    }

    @Test
    void testEncodeLengthBounds() {
        final Base16 base16 = new Base16();
        assertThrows(IllegalArgumentException.class, () -> base16.encode(new byte[10], 0, 1 << 30), "Expected exception for invalid length");
    }

    @Test
    void testDecodeInvalidBase16Bytes() {
        final byte[] invalidBytes = { 'n', 'H', '=', '=', (byte) 0x9c };
        final Base16 base16 = new Base16();
        assertThrows(RuntimeException.class, () -> base16.decode(invalidBytes), "Expected exception for invalid Base16 bytes");
    }

    @Test
    void testConstructorWithLowerCase() {
        final Base16 base16 = new Base16(true);
        final byte[] encoded = base16.encode(BaseNTestData.DECODED);
        final String result = new String(encoded, CHARSET_UTF8);
        assertEquals(Base16TestData.ENCODED_UTF8_LOWERCASE, result, "Lowercase encoding failed");
    }

    @Test
    void testConstructorWithLowerCaseAndStrictPolicy() {
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
        final byte[] encoded = base16.encode(BaseNTestData.DECODED);
        final String result = new String(encoded, CHARSET_UTF8);
        assertEquals(Base16TestData.ENCODED_UTF8_UPPERCASE, result, "Uppercase encoding with strict policy failed");
    }

    @Test
    void testConstructors() {
        new Base16();
        new Base16(false);
        new Base16(true);
        new Base16(false, CodecPolicy.LENIENT);
        new Base16(false, CodecPolicy.STRICT);
    }

    @Test
    void testDecodeSingleBytes() {
        final String encoded = "556E74696C206E6578742074696D6521";
        final Base16 base16 = new Base16();
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final byte[] encodedBytes = encoded.getBytes(CHARSET_UTF8);

        // Decode byte-by-byte
        base16.decode(encodedBytes, 0, 1, context);
        base16.decode(encodedBytes, 1, 1, context); // yields "U"
        base16.decode(encodedBytes, 2, 1, context);
        base16.decode(encodedBytes, 3, 1, context); // yields "n"

        // Decode split hex-pairs
        base16.decode(encodedBytes, 4, 3, context); // yields "t"
        base16.decode(encodedBytes, 7, 3, context); // yields "il"
        base16.decode(encodedBytes, 10, 3, context); // yields " "

        // Decode remaining
        base16.decode(encodedBytes, 13, 19, context); // yields "next time!"

        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        final String decoded = new String(decodedBytes, CHARSET_UTF8);

        assertEquals("Until next time!", decoded, "Decoding single bytes failed");
    }

    @Test
    void testDecodeSingleBytesOptimization() {
        final BaseNCodec.Context context = new BaseNCodec.Context();
        assertEquals(0, context.ibitWorkArea);
        assertNull(context.buffer);

        final byte[] data = new byte[1];
        final Base16 base16 = new Base16();

        data[0] = (byte) 'E';
        base16.decode(data, 0, 1, context);
        assertEquals(15, context.ibitWorkArea);
        assertNull(context.buffer);

        data[0] = (byte) 'F';
        base16.decode(data, 0, 1, context);
        assertEquals(0, context.ibitWorkArea);

        assertEquals((byte) 0xEF, context.buffer[0]);
    }

    @Test
    void testEmptyBase16EncodingAndDecoding() {
        byte[] empty = {};
        Base16 base16 = new Base16();

        byte[] result = base16.encode(empty);
        assertEquals(0, result.length, "Encoding failed for empty byte array");
        assertNull(base16.encode(null), "Encoding failed for null byte array");

        result = base16.decode(empty);
        assertEquals(0, result.length, "Decoding failed for empty byte array");
        assertNull(base16.decode((byte[]) null), "Decoding failed for null byte array");
    }

    @Test
    void testEncodeDecodeRandomData() {
        for (int i = 1; i < 5; i++) {
            final int len = random.nextInt(10000) + 1;
            final byte[] data = new byte[len];
            random.nextBytes(data);

            final Base16 base16 = new Base16();
            final byte[] encoded = base16.encode(data);
            final byte[] decoded = base16.decode(encoded);

            assertArrayEquals(data, decoded, "Random data encoding/decoding failed");
        }
    }

    @Test
    void testEncodeDecodeSmallData() {
        for (int i = 0; i < 12; i++) {
            final byte[] data = new byte[i];
            random.nextBytes(data);

            final Base16 base16 = new Base16();
            final byte[] encoded = base16.encode(data);
            final byte[] decoded = base16.decode(encoded);

            assertArrayEquals(data, decoded, "Small data encoding/decoding failed");
        }
    }

    @Test
    void testIsInAlphabet() {
        Base16 base16 = new Base16(true);

        // Test invalid bounds
        assertFalse(base16.isInAlphabet((byte) 0));
        assertFalse(base16.isInAlphabet((byte) 1));
        assertFalse(base16.isInAlphabet((byte) -1));
        assertFalse(base16.isInAlphabet((byte) -15));
        assertFalse(base16.isInAlphabet((byte) -16));
        assertFalse(base16.isInAlphabet((byte) 128));
        assertFalse(base16.isInAlphabet((byte) 255));

        // Test lower-case alphabet
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        for (char c = 'a'; c <= 'f'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        for (char c = 'A'; c <= 'F'; c++) {
            assertFalse(base16.isInAlphabet((byte) c));
        }

        // Test upper-case alphabet
        base16 = new Base16(false);
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        for (char c = 'a'; c <= 'f'; c++) {
            assertFalse(base16.isInAlphabet((byte) c));
        }
        for (char c = 'A'; c <= 'F'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
    }

    @Test
    void testKnownDecodings() {
        Base16 base16 = new Base16(true);

        assertEquals("The quick brown fox jumped over the lazy dogs.",
                new String(base16.decode("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e".getBytes(CHARSET_UTF8))));
        assertEquals("It was the best of times, it was the worst of times.",
                new String(base16.decode("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e".getBytes(CHARSET_UTF8))));
        assertEquals("http://jakarta.apache.org/commmons",
                new String(base16.decode("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73".getBytes(CHARSET_UTF8))));
        assertEquals("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz",
                new String(base16.decode("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a".getBytes(CHARSET_UTF8))));
        assertEquals("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }",
                new String(base16.decode("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d".getBytes(CHARSET_UTF8))));
        assertEquals("xyzzy!", new String(base16.decode("78797a7a7921".getBytes(CHARSET_UTF8))));
    }

    @Test
    void testKnownEncodings() {
        Base16 base16 = new Base16(true);

        assertEquals("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e",
                new String(base16.encode("The quick brown fox jumped over the lazy dogs.".getBytes(CHARSET_UTF8))));
        assertEquals("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e",
                new String(base16.encode("It was the best of times, it was the worst of times.".getBytes(CHARSET_UTF8))));
        assertEquals("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73",
                new String(base16.encode("http://jakarta.apache.org/commmons".getBytes(CHARSET_UTF8))));
        assertEquals("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a",
                new String(base16.encode("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz".getBytes(CHARSET_UTF8))));
        assertEquals("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d",
                new String(base16.encode("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }".getBytes(CHARSET_UTF8))));
        assertEquals("78797a7a7921", new String(base16.encode("xyzzy!".getBytes(CHARSET_UTF8))));
    }

    @Test
    void testLenientDecoding() {
        final String encoded = "aabbccdde"; // Note the trailing `e` which does not make up a hex-pair and so is only 1/2 byte

        final Base16 base16 = new Base16(true, CodecPolicy.LENIENT);
        assertEquals(CodecPolicy.LENIENT, base16.getCodecPolicy());

        final byte[] decoded = base16.decode(encoded.getBytes(CHARSET_UTF8));
        assertArrayEquals(new byte[] { (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd }, decoded, "Lenient decoding failed");
    }

    @Test
    void testInvalidBase16Characters() {
        final byte[] invalidChars = { '/', ':', '@', 'G', '%', '`', 'g' };
        final Base16 base16 = new Base16();

        for (final byte invalidChar : invalidChars) {
            assertThrows(IllegalArgumentException.class, () -> base16.decode(new byte[] { invalidChar }), "Expected exception for invalid char: " + (char) invalidChar);
        }
    }

    @Test
    void testObjectDecodeWithInvalidParameter() {
        assertThrows(DecoderException.class, () -> new Base16().decode(Integer.valueOf(5)), "Expected exception for invalid object type");
    }

    @Test
    void testObjectDecodeWithValidParameter() throws Exception {
        final String original = "Hello World!";
        final Object encodedObject = new Base16().encode(original.getBytes(CHARSET_UTF8));

        final Base16 base16 = new Base16();
        final Object decodedObject = base16.decode(encodedObject);
        final byte[] decodedBytes = (byte[]) decodedObject;
        final String decodedString = new String(decodedBytes, CHARSET_UTF8);

        assertEquals(original, decodedString, "Object decoding failed");
    }

    @Test
    void testObjectEncode() {
        final Base16 base16 = new Base16();
        assertEquals("48656C6C6F20576F726C64", new String(base16.encode("Hello World".getBytes(CHARSET_UTF8))), "Object encoding failed");
    }

    @Test
    void testObjectEncodeWithInvalidParameter() {
        assertThrows(EncoderException.class, () -> new Base16().encode("Yadayadayada"), "Expected exception for invalid object type");
    }

    @Test
    void testObjectEncodeWithValidParameter() throws Exception {
        final String original = "Hello World!";
        final Object originalObject = original.getBytes(CHARSET_UTF8);

        final Object encodedObject = new Base16().encode(originalObject);
        final byte[] decodedBytes = new Base16().decode((byte[]) encodedObject);
        final String decodedString = new String(decodedBytes, CHARSET_UTF8);

        assertEquals(original, decodedString, "Object encoding/decoding failed");
    }

    @Test
    void testOddEvenDecoding() {
        final String encoded = "4142434445";

        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 base16 = new Base16();

        final byte[] encodedBytes = encoded.getBytes(CHARSET_UTF8);

        // Pass odd, then even, then odd amount of data
        base16.decode(encodedBytes, 0, 3, context);
        base16.decode(encodedBytes, 3, 4, context);
        base16.decode(encodedBytes, 7, 3, context);

        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        final String decoded = new String(decodedBytes, CHARSET_UTF8);

        assertEquals("ABCDE", decoded, "Odd/even decoding failed");
    }

    @Test
    void testPairsEncoding() {
        Base16 base16 = new Base16();

        assertEquals("0000", new String(base16.encode(new byte[] { (byte) 0, (byte) 0 })));
        assertEquals("0001", new String(base16.encode(new byte[] { (byte) 0, (byte) 1 })));
        assertEquals("0002", new String(base16.encode(new byte[] { (byte) 0, (byte) 2 })));
        assertEquals("0003", new String(base16.encode(new byte[] { (byte) 0, (byte) 3 })));
        assertEquals("0004", new String(base16.encode(new byte[] { (byte) 0, (byte) 4 })));
        assertEquals("0005", new String(base16.encode(new byte[] { (byte) 0, (byte) 5 })));
        assertEquals("0006", new String(base16.encode(new byte[] { (byte) 0, (byte) 6 })));
        assertEquals("0007", new String(base16.encode(new byte[] { (byte) 0, (byte) 7 })));
        assertEquals("0008", new String(base16.encode(new byte[] { (byte) 0, (byte) 8 })));
        assertEquals("0009", new String(base16.encode(new byte[] { (byte) 0, (byte) 9 })));
        assertEquals("000A", new String(base16.encode(new byte[] { (byte) 0, (byte) 10 })));
        assertEquals("000B", new String(base16.encode(new byte[] { (byte) 0, (byte) 11 })));
        assertEquals("000C", new String(base16.encode(new byte[] { (byte) 0, (byte) 12 })));
        assertEquals("000D", new String(base16.encode(new byte[] { (byte) 0, (byte) 13 })));
        assertEquals("000E", new String(base16.encode(new byte[] { (byte) 0, (byte) 14 })));
        assertEquals("000F", new String(base16.encode(new byte[] { (byte) 0, (byte) 15 })));
        assertEquals("0010", new String(base16.encode(new byte[] { (byte) 0, (byte) 16 })));
        assertEquals("0011", new String(base16.encode(new byte[] { (byte) 0, (byte) 17 })));

        for (int i = -128; i <= 127; i++) {
            final byte[] test = { (byte) i, (byte) i };
            assertArrayEquals(test, base16.decode(base16.encode(test)), "Pair encoding/decoding failed for: " + i);
        }
    }

    @Test
    void testSingletonsEncoding() {
        Base16 base16 = new Base16();

        assertEquals("00", new String(base16.encode(new byte[] { (byte) 0 })));
        assertEquals("01", new String(base16.encode(new byte[] { (byte) 1 })));
        assertEquals("02", new String(base16.encode(new byte[] { (byte) 2 })));
        assertEquals("03", new String(base16.encode(new byte[] { (byte) 3 })));
        assertEquals("04", new String(base16.encode(new byte[] { (byte) 4 })));
        assertEquals("05", new String(base16.encode(new byte[] { (byte) 5 })));
        assertEquals("06", new String(base16.encode(new byte[] { (byte) 6 })));
        assertEquals("07", new String(base16.encode(new byte[] { (byte) 7 })));
        assertEquals("08", new String(base16.encode(new byte[] { (byte) 8 })));
        assertEquals("09", new String(base16.encode(new byte[] { (byte) 9 })));
        assertEquals("0A", new String(base16.encode(new byte[] { (byte) 10 })));
        assertEquals("0B", new String(base16.encode(new byte[] { (byte) 11 })));
        assertEquals("0C", new String(base16.encode(new byte[] { (byte) 12 })));
        assertEquals("0D", new String(base16.encode(new byte[] { (byte) 13 })));
        assertEquals("0E", new String(base16.encode(new byte[] { (byte) 14 })));
        assertEquals("0F", new String(base16.encode(new byte[] { (byte) 15 })));
        assertEquals("10", new String(base16.encode(new byte[] { (byte) 16 })));
        assertEquals("11", new String(base16.encode(new byte[] { (byte) 17 })));

        for (int i = -128; i <= 127; i++) {
            final byte[] test = { (byte) i };
            assertArrayEquals(test, base16.decode(base16.encode(test)), "Singleton encoding/decoding failed for: " + i);
        }
    }

    @Test
    void testStrictDecoding() {
        final String encoded = "aabbccdde"; // Note the trailing `e` which does not make up a hex-pair and so is only 1/2 byte

        final Base16 base16 = new Base16(true, CodecPolicy.STRICT);
        assertEquals(CodecPolicy.STRICT, base16.getCodecPolicy());
        assertThrows(IllegalArgumentException.class, () -> base16.decode(encoded.getBytes(CHARSET_UTF8)), "Expected exception for strict decoding");
    }

    @Test
    void testStringToByteVariations() throws DecoderException {
        final Base16 base16 = new Base16();
        final String encodedHelloWorld = "48656C6C6F20576F726C64";
        final String emptyString = "";
        final String nullString = null;

        assertEquals("Hello World", new String(base16.decode(encodedHelloWorld), CHARSET_UTF8), "String to byte decoding failed for 'Hello World'");
        assertEquals("", new String(base16.decode(emptyString), CHARSET_UTF8), "String to byte decoding failed for empty string");
        assertNull(base16.decode(nullString), "String to byte decoding failed for null string");
    }

    @Test
    void testTripletsEncoding() {
        Base16 base16 = new Base16();

        assertEquals("000000", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 0 })));
        assertEquals("000001", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 1 })));
        assertEquals("000002", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 2 })));
        assertEquals("000003", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 3 })));
        assertEquals("000004", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 4 })));
        assertEquals("000005", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 5 })));
        assertEquals("000006", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 6 })));
        assertEquals("000007", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 7 })));
        assertEquals("000008", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 8 })));
        assertEquals("000009", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 9 })));
        assertEquals("00000A", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 10 })));
        assertEquals("00000B", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 11 })));
        assertEquals("00000C", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 12 })));
        assertEquals("00000D", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 13 })));
        assertEquals("00000E", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 14 })));
        assertEquals("00000F", new String(base16.encode(new byte[] { (byte) 0, (byte) 0, (byte) 15 })));
    }
}