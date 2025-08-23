package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for Base16 (Hex) encoding/decoding.
 * These tests aim to be easier to read and maintain by:
 * - Centralizing constants for repeated values.
 * - Extracting helpers for UTF-8 and hex conversions.
 * - Reducing repetition with small loops where applicable.
 * - Using clear, intention-revealing test names.
 */
class Base16Test {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String HELLO_WORLD = "Hello World";
    private static final String HELLO_WORLD_HEX_UPPER = "48656C6C6F20576F726C64";

    private static final Base16 BASE16_UPPER = new Base16();
    private static final Base16 BASE16_LOWER = new Base16(true);

    private final Random random = new Random();

    private static byte[] utf8(final String s) {
        return StringUtils.getBytesUtf8(s);
    }

    private static String strUtf8(final byte[] bytes) {
        return StringUtils.newStringUtf8(bytes);
    }

    private static String encodeUpperToHex(final byte[] bytes) {
        return strUtf8(BASE16_UPPER.encode(bytes));
    }

    private static byte[] decodeUpperHexToBytes(final String hex) {
        return BASE16_UPPER.decode(utf8(hex));
    }

    private static String toUpperHex(final byte... bytes) {
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            sb.append(String.format(Locale.ROOT, "%02X", b & 0xFF));
        }
        return sb.toString();
    }

    /**
     * Basic round-trip: "Hello World" -> hex (upper-case) -> original.
     */
    @Test
    void encodesAndDecodesHelloWorld() {
        final String encoded = encodeUpperToHex(utf8(HELLO_WORLD));
        assertEquals(HELLO_WORLD_HEX_UPPER, encoded);

        final String decoded = strUtf8(BASE16_UPPER.decode(utf8(encoded)));
        assertEquals(HELLO_WORLD, decoded);
    }

    @Test
    void encodesSubsetAtBufferStart() {
        assertEncodesInPaddedBuffer(0, 100);
    }

    @Test
    void encodesSubsetAtBufferMiddle() {
        assertEncodesInPaddedBuffer(100, 100);
    }

    @Test
    void encodesSubsetAtBufferEnd() {
        assertEncodesInPaddedBuffer(100, 0);
    }

    private void assertEncodesInPaddedBuffer(final int startPadSize, final int endPadSize) {
        final byte[] payload = utf8(HELLO_WORLD);
        byte[] buffer = ArrayUtils.addAll(payload, new byte[endPadSize]);
        buffer = ArrayUtils.addAll(new byte[startPadSize], buffer);

        final byte[] encoded = BASE16_UPPER.encode(buffer, startPadSize, payload.length);
        assertEquals(HELLO_WORLD_HEX_UPPER, strUtf8(encoded));
    }

    @Test
    void byteToStringVariations() {
        final byte[] nonEmpty = utf8("Hello World");
        final byte[] empty = {};
        final byte[] nullBytes = null;

        assertEquals(HELLO_WORLD_HEX_UPPER, BASE16_UPPER.encodeToString(nonEmpty));
        assertEquals(HELLO_WORLD_HEX_UPPER, strUtf8(new Base16().encode(nonEmpty)));
        assertEquals("", BASE16_UPPER.encodeToString(empty));
        assertEquals("", strUtf8(new Base16().encode(empty)));
        assertNull(BASE16_UPPER.encodeToString(nullBytes));
        assertNull(strUtf8(new Base16().encode(nullBytes)));
    }

    @Test
    void checkEncodeLengthBounds() {
        assertThrows(IllegalArgumentException.class, () -> BASE16_UPPER.encode(new byte[10], 0, 1 << 30));
    }

    /**
     * isBase16 throws RuntimeException on some non-Base16 bytes (CODEC-68).
     */
    @Test
    void codec68_nonBase16InputCausesRuntimeException() {
        final byte[] x = { 'n', 'H', '=', '=', (byte) 0x9c };
        assertThrows(RuntimeException.class, () -> BASE16_UPPER.decode(x));
    }

    @Test
    void constructor_lowerCaseEncoding() {
        final byte[] encoded = BASE16_LOWER.encode(BaseNTestData.DECODED);
        assertEquals(Base16TestData.ENCODED_UTF8_LOWERCASE, strUtf8(encoded), "new Base16(true)");
    }

    @Test
    void constructor_upperCaseWithStrictPolicy() {
        final Base16 b16 = new Base16(false, CodecPolicy.STRICT);
        assertEquals(Base16TestData.ENCODED_UTF8_UPPERCASE, strUtf8(b16.encode(BaseNTestData.DECODED)),
                "new Base16(false, CodecPolicy.STRICT)");
    }

    @Test
    void constructors_doNotThrow() {
        new Base16();
        new Base16(false);
        new Base16(true);
        new Base16(false, CodecPolicy.LENIENT);
        new Base16(false, CodecPolicy.STRICT);
    }

    @Test
    void decodeSingleBytes_acrossChunkBoundaries() {
        final String encoded = "556E74696C206E6578742074696D6521"; // "Until next time!"
        final BaseNCodec.Context ctx = new BaseNCodec.Context();
        final byte[] encodedBytes = utf8(encoded);

        // decode byte-by-byte (including split hex-pairs) to exercise streaming behavior
        final Base16 b16 = new Base16();
        b16.decode(encodedBytes, 0, 1, ctx);
        b16.decode(encodedBytes, 1, 1, ctx); // yields "U"
        b16.decode(encodedBytes, 2, 1, ctx);
        b16.decode(encodedBytes, 3, 1, ctx); // yields "n"
        b16.decode(encodedBytes, 4, 3, ctx);  // yields "t"
        b16.decode(encodedBytes, 7, 3, ctx);  // yields "il"
        b16.decode(encodedBytes, 10, 3, ctx); // yields " "
        b16.decode(encodedBytes, 13, 19, ctx);// yields "next time!"

        final byte[] out = new byte[ctx.pos];
        System.arraycopy(ctx.buffer, ctx.readPos, out, 0, out.length);
        assertEquals("Until next time!", strUtf8(out));
    }

    @Test
    void decodeSingleBytes_internalOptimization() {
        final BaseNCodec.Context ctx = new BaseNCodec.Context();
        assertEquals(0, ctx.ibitWorkArea);
        assertNull(ctx.buffer);

        final Base16 b16 = new Base16();
        final byte[] oneByte = new byte[1];

        oneByte[0] = (byte) 'E';
        b16.decode(oneByte, 0, 1, ctx);
        assertEquals(15, ctx.ibitWorkArea); // 'E' -> 0xE
        assertNull(ctx.buffer);             // still no output

        oneByte[0] = (byte) 'F';
        b16.decode(oneByte, 0, 1, ctx);
        assertEquals(0, ctx.ibitWorkArea);  // completed a byte

        assertEquals((byte) 0xEF, ctx.buffer[0]);
    }

    @Test
    void emptyInputs_encodeAndDecode() {
        byte[] empty = {};
        byte[] result = BASE16_UPPER.encode(empty);
        assertEquals(0, result.length);
        assertNull(new Base16().encode(null));

        result = new Base16().encode(empty, 0, 1);
        assertEquals(0, result.length);
        assertNull(new Base16().encode(null));

        empty = new byte[0];
        result = new Base16().decode(empty);
        assertEquals(0, result.length);
        assertNull(new Base16().decode((byte[]) null));
    }

    @Test
    void encodeDecode_randomArrays() {
        for (int i = 0; i < 4; i++) {
            final int len = random.nextInt(10_000) + 1;
            final byte[] data = new byte[len];
            random.nextBytes(data);

            final byte[] roundTrip = new Base16().decode(new Base16().encode(data));
            assertArrayEquals(data, roundTrip);
        }
    }

    @Test
    void encodeDecode_smallArrays() {
        for (int i = 0; i < 12; i++) {
            final byte[] data = new byte[i];
            random.nextBytes(data);

            final byte[] roundTrip = new Base16().decode(new Base16().encode(data));
            assertArrayEquals(data, roundTrip, "Roundtrip mismatch for length " + i);
        }
    }

    @Test
    void isInAlphabet_lowerAndUpperCasesAndBounds() {
        // invalid bounds
        Base16 b16 = new Base16(true);
        for (final int v : new int[] {0, 1, -1, -15, -16, 128, 255}) {
            assertFalse(b16.isInAlphabet((byte) v));
        }

        // lower-case alphabet accepts 0-9 and a-f only
        b16 = new Base16(true);
        for (char c = '0'; c <= '9'; c++) assertTrue(b16.isInAlphabet((byte) c));
        for (char c = 'a'; c <= 'f'; c++) assertTrue(b16.isInAlphabet((byte) c));
        for (char c = 'A'; c <= 'F'; c++) assertFalse(b16.isInAlphabet((byte) c));
        assertFalse(b16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('a' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('f' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('z' + 1)));

        // upper-case alphabet accepts 0-9 and A-F only
        b16 = new Base16(false);
        for (char c = '0'; c <= '9'; c++) assertTrue(b16.isInAlphabet((byte) c));
        for (char c = 'a'; c <= 'f'; c++) assertFalse(b16.isInAlphabet((byte) c));
        for (char c = 'A'; c <= 'F'; c++) assertTrue(b16.isInAlphabet((byte) c));
        assertFalse(b16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('A' - 1)));
        assertFalse(b16.isInAlphabet((byte) ('F' + 1)));
        assertFalse(b16.isInAlphabet((byte) ('Z' + 1)));
    }

    @Test
    void knownDecodings_lowerCaseInput() {
        assertEquals("The quick brown fox jumped over the lazy dogs.",
                strUtf8(new Base16(true).decode(utf8("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e"))));
        assertEquals("It was the best of times, it was the worst of times.",
                strUtf8(new Base16(true).decode(utf8("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e"))));
        assertEquals("http://jakarta.apache.org/commmons",
                strUtf8(new Base16(true).decode(utf8("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73"))));
        assertEquals("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz",
                strUtf8(new Base16(true).decode(utf8("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a"))));
        assertEquals("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }",
                strUtf8(new Base16(true).decode(utf8("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d"))));
        assertEquals("xyzzy!", strUtf8(new Base16(true).decode(utf8("78797a7a7921"))));
    }

    @Test
    void knownEncodings_lowerCaseOutput() {
        assertEquals("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e",
                strUtf8(new Base16(true).encode(utf8("The quick brown fox jumped over the lazy dogs."))));
        assertEquals("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e",
                strUtf8(new Base16(true).encode(utf8("It was the best of times, it was the worst of times."))));
        assertEquals("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73",
                strUtf8(new Base16(true).encode(utf8("http://jakarta.apache.org/commmons"))));
        assertEquals("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a",
                strUtf8(new Base16(true).encode(utf8("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"))));
        assertEquals("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d",
                strUtf8(new Base16(true).encode(utf8("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }"))));
        assertEquals("78797a7a7921", strUtf8(new Base16(true).encode(utf8("xyzzy!"))));
    }

    @Test
    void lenientDecoding_truncatesTrailingNibble() {
        final String encoded = "aabbccdde"; // trailing 'e' is a half-byte
        final Base16 b16 = new Base16(true, CodecPolicy.LENIENT);
        assertEquals(CodecPolicy.LENIENT, b16.getCodecPolicy());
        assertArrayEquals(new byte[] {(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd}, b16.decode(utf8(encoded)));
    }

    @Test
    void strictDecoding_rejectsTrailingNibble() {
        final String encoded = "aabbccdde"; // trailing 'e' is a half-byte
        final Base16 b16 = new Base16(true, CodecPolicy.STRICT);
        assertEquals(CodecPolicy.STRICT, b16.getCodecPolicy());
        assertThrows(IllegalArgumentException.class, () -> b16.decode(utf8(encoded)));
    }

    @Test
    void nonBase16Chars_throwIllegalArgumentException() {
        final byte[] invalids = {'/', ':', '@', 'G', '%', '`', 'g'};
        final byte[] single = new byte[1];
        for (final byte b : invalids) {
            single[0] = b;
            assertThrows(IllegalArgumentException.class, () -> BASE16_UPPER.decode(single),
                    "Invalid Base16 char: " + (char) b);
        }
    }

    @Test
    void objectDecode_withInvalidParameter_throws() {
        assertThrows(DecoderException.class, () -> new Base16().decode(Integer.valueOf(5)));
    }

    @Test
    void objectDecode_withValidParameter_roundTrips() throws Exception {
        final String original = "Hello World!";
        final Object encoded = new Base16().encode(original.getBytes(UTF8));

        final byte[] decoded = (byte[]) new Base16().decode(encoded);
        assertEquals(original, new String(decoded, UTF8));
    }

    @Test
    void objectEncode_roundTrips() {
        final Base16 b16 = new Base16();
        assertEquals(HELLO_WORLD_HEX_UPPER, new String(b16.encode(HELLO_WORLD.getBytes(UTF8)), UTF8));
    }

    @Test
    void objectEncode_withInvalidParameter_throws() {
        assertThrows(EncoderException.class, () -> new Base16().encode("Yadayadayada"));
    }

    @Test
    void objectEncode_withValidParameter_roundTrips() throws Exception {
        final String original = "Hello World!";
        final Object origObj = original.getBytes(UTF8);

        final Object encoded = new Base16().encode(origObj);
        final byte[] roundTrip = new Base16().decode((byte[]) encoded);
        assertEquals(original, new String(roundTrip, UTF8));
    }

    @Test
    void oddEvenDecoding_streamingAcrossOddEvenBoundaries() {
        final String encoded = "4142434445"; // "ABCDE"
        final BaseNCodec.Context ctx = new BaseNCodec.Context();
        final Base16 b16 = new Base16();
        final byte[] bytes = utf8(encoded);

        b16.decode(bytes, 0, 3, ctx);  // odd
        b16.decode(bytes, 3, 4, ctx);  // even
        b16.decode(bytes, 7, 3, ctx);  // odd

        final byte[] out = new byte[ctx.pos];
        System.arraycopy(ctx.buffer, ctx.readPos, out, 0, out.length);
        assertEquals("ABCDE", strUtf8(out));
    }

    @Test
    void encodePairs_andAllEqualPairsRoundTrip() {
        // Verify 0x00XX for a few initial values with expected upper-case hex.
        for (int i = 0; i <= 17; i++) {
            final byte[] pair = {(byte) 0x00, (byte) i};
            assertEquals(toUpperHex(pair), encodeUpperToHex(pair));
        }
        // Round-trip for all equal pairs
        for (int i = -128; i <= 127; i++) {
            final byte[] test = {(byte) i, (byte) i};
            assertArrayEquals(test, new Base16().decode(new Base16().encode(test)));
        }
    }

    @Test
    void encodeSingletons_andAllSingletonsRoundTrip() {
        // Verify 0x00..0x68 explicitly via computed expected hex
        for (int i = 0; i <= 0x68; i++) {
            final byte[] singleton = {(byte) i};
            assertEquals(toUpperHex(singleton), encodeUpperToHex(singleton));
        }
        // Round-trip for all byte values
        for (int i = -128; i <= 127; i++) {
            final byte[] singleton = {(byte) i};
            assertArrayEquals(singleton, new Base16().decode(new Base16().encode(singleton)));
        }
    }

    @Test
    void encodeTriplets_firstTwoZero_thirdInLowRange() {
        for (int i = 0; i <= 0x0F; i++) {
            final byte[] triplet = {(byte) 0x00, (byte) 0x00, (byte) i};
            assertEquals(toUpperHex(triplet), encodeUpperToHex(triplet));
        }
    }

    @Test
    void stringToByteVariations() throws DecoderException {
        final String s1 = HELLO_WORLD_HEX_UPPER;
        final String s2 = "";
        final String s3 = null;

        assertEquals(HELLO_WORLD, strUtf8(BASE16_UPPER.decode(s1)));
        assertEquals(HELLO_WORLD, strUtf8((byte[]) new Base16().decode((Object) s1)));
        assertEquals(HELLO_WORLD, strUtf8(new Base16().decode(s1)));
        assertEquals("", strUtf8(new Base16().decode(s2)));
        assertEquals("", strUtf8(new Base16().decode(s2)));
        assertNull(strUtf8(new Base16().decode(s3)));
        assertNull(strUtf8(new Base16().decode(s3)));
    }
}