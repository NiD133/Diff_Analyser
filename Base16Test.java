/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link Base16}.
 */
@DisplayName("Tests Base16")
class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private static final String HELLO_WORLD = "Hello World";
    private static final byte[] HELLO_WORLD_BYTES = StringUtils.getBytesUtf8(HELLO_WORLD);
    private static final String ENCODED_HELLO_WORLD_UPPERCASE = "48656C6C6F20576F726C64";
    private static final String ENCODED_HELLO_WORLD_LOWERCASE = "48656c6c6f20576f726c64";

    private final Random random = new Random();

    @Test
    @DisplayName("should perform a basic encode/decode cycle")
    void testBasicEncodeDecodeCycle() {
        final Base16 base16 = new Base16();
        final byte[] encoded = base16.encode(HELLO_WORLD_BYTES);
        assertEquals(ENCODED_HELLO_WORLD_UPPERCASE, StringUtils.newStringUtf8(encoded));

        final byte[] decoded = base16.decode(encoded);
        assertArrayEquals(HELLO_WORLD_BYTES, decoded);
    }

    @Test
    @DisplayName("encode/decode should be reversible for random byte arrays")
    void testEncodeDecodeRandom() {
        final Base16 base16 = new Base16();
        for (int i = 1; i < 5; i++) {
            final int len = random.nextInt(10000) + 1;
            final byte[] data = new byte[len];
            random.nextBytes(data);
            final byte[] enc = base16.encode(data);
            final byte[] data2 = base16.decode(enc);
            assertArrayEquals(data, data2);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    @DisplayName("encode/decode should be reversible for small byte arrays")
    void testEncodeDecodeSmall(final int size) {
        final Base16 base16 = new Base16();
        final byte[] data = new byte[size];
        random.nextBytes(data);
        final byte[] enc = base16.encode(data);
        final byte[] data2 = base16.decode(enc);
        assertArrayEquals(data, data2);
    }

    @Test
    @DisplayName("encode/decode should be reversible for all single byte values")
    void testEncodeDecodeIsReversibleForAllByteValues() {
        final Base16 base16 = new Base16();
        for (int i = -128; i <= 127; i++) {
            final byte[] data = {(byte) i};
            final byte[] encoded = base16.encode(data);
            final byte[] decoded = base16.decode(encoded);
            assertArrayEquals(data, decoded, () -> "Failed for byte value: " + i);
        }
    }

    @Nested
    @DisplayName("Constructor and Alphabet Case Tests")
    class ConstructorAndAlphabetTests {
        @Test
        @DisplayName("should encode to lowercase when configured")
        void testConstructorWithLowerCaseTrue() {
            final Base16 base16 = new Base16(true);
            final byte[] encoded = base16.encode(HELLO_WORLD_BYTES);
            assertEquals(ENCODED_HELLO_WORLD_LOWERCASE, StringUtils.newStringUtf8(encoded));
        }

        @Test
        @DisplayName("should encode to uppercase by default")
        void testConstructorWithLowerCaseFalse() {
            final Base16 base16 = new Base16(false);
            final byte[] encoded = base16.encode(HELLO_WORLD_BYTES);
            assertEquals(ENCODED_HELLO_WORLD_UPPERCASE, StringUtils.newStringUtf8(encoded));
        }

        @Test
        @DisplayName("should validate characters against the lowercase alphabet")
        void testIsInAlphabetLowercase() {
            final Base16 b16 = new Base16(true);
            assertTrue(b16.isInAlphabet((byte) '0'));
            assertTrue(b16.isInAlphabet((byte) '9'));
            assertTrue(b16.isInAlphabet((byte) 'a'));
            assertTrue(b16.isInAlphabet((byte) 'f'));

            assertFalse(b16.isInAlphabet((byte) 'A'));
            assertFalse(b16.isInAlphabet((byte) 'F'));
            assertFalse(b16.isInAlphabet((byte) 'g'));
            assertFalse(b16.isInAlphabet((byte) 'z'));
            assertFalse(b16.isInAlphabet((byte) 'Z'));
        }

        @Test
        @DisplayName("should validate characters against the uppercase alphabet")
        void testIsInAlphabetUppercase() {
            final Base16 b16 = new Base16(false);
            assertTrue(b16.isInAlphabet((byte) '0'));
            assertTrue(b16.isInAlphabet((byte) '9'));
            assertTrue(b16.isInAlphabet((byte) 'A'));
            assertTrue(b16.isInAlphabet((byte) 'F'));

            assertFalse(b16.isInAlphabet((byte) 'a'));
            assertFalse(b16.isInAlphabet((byte) 'f'));
            assertFalse(b16.isInAlphabet((byte) 'G'));
            assertFalse(b16.isInAlphabet((byte) 'z'));
            assertFalse(b16.isInAlphabet((byte) 'Z'));
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, -128, 127 })
        @DisplayName("isInAlphabet should return false for out-of-range byte values")
        void testIsInAlphabetWithOutOfRangeValues(final int value) {
            final Base16 b16 = new Base16();
            assertFalse(b16.isInAlphabet((byte) value));
        }
    }

    @Nested
    @DisplayName("Decoding Policy Tests")
    class DecodingPolicyTests {

        @Test
        @DisplayName("should decode with lenient policy ignoring an incomplete trailing hex pair")
        void testLenientDecoding() {
            final String encoded = "aabbccdde"; // Trailing 'e' is an incomplete pair
            final Base16 b16 = new Base16(true, CodecPolicy.LENIENT);
            assertEquals(CodecPolicy.LENIENT, b16.getCodecPolicy());

            final byte[] decoded = b16.decode(StringUtils.getBytesUtf8(encoded));
            assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd}, decoded);
        }

        @Test
        @DisplayName("should throw exception with strict policy for an incomplete trailing hex pair")
        void testStrictDecoding() {
            final String encoded = "aabbccdde"; // Trailing 'e' is an incomplete pair
            final Base16 b16 = new Base16(true, CodecPolicy.STRICT);
            assertEquals(CodecPolicy.STRICT, b16.getCodecPolicy());
            assertThrows(IllegalArgumentException.class, () -> b16.decode(StringUtils.getBytesUtf8(encoded)));
        }
    }

    @Nested
    @DisplayName("API Method Tests")
    class ApiMethodTests {

        @ParameterizedTest(name = "startPad={0}, endPad={1}")
        @CsvSource({"100, 0", "100, 100", "0, 100"})
        @DisplayName("should correctly encode a subsection of a padded buffer")
        void testEncodeWithBufferPadding(final int startPadSize, final int endPadSize) {
            byte[] buffer = ArrayUtils.addAll(HELLO_WORLD_BYTES, new byte[endPadSize]);
            buffer = ArrayUtils.addAll(new byte[startPadSize], buffer);

            final byte[] encodedBytes = new Base16().encode(buffer, startPadSize, HELLO_WORLD_BYTES.length);
            assertEquals(ENCODED_HELLO_WORLD_UPPERCASE, StringUtils.newStringUtf8(encodedBytes));
        }

        @Test
        void encodeShouldReturnEmptyForEmptyInput() {
            assertArrayEquals(ArrayUtils.EMPTY_BYTE_ARRAY, new Base16().encode(ArrayUtils.EMPTY_BYTE_ARRAY));
        }

        @Test
        void encodeShouldReturnNullForNullInput() {
            assertNull(new Base16().encode(null));
        }

        @Test
        void decodeShouldReturnEmptyForEmptyInput() {
            assertArrayEquals(ArrayUtils.EMPTY_BYTE_ARRAY, new Base16().decode(ArrayUtils.EMPTY_BYTE_ARRAY));
        }

        @Test
        void decodeShouldReturnNullForNullInput() {
            assertNull(new Base16().decode((byte[]) null));
        }

        @Test
        void encodeToStringShouldReturnCorrectString() {
            assertEquals(ENCODED_HELLO_WORLD_UPPERCASE, new Base16().encodeToString(HELLO_WORLD_BYTES));
        }

        @Test
        void encodeToStringShouldReturnEmptyForEmptyInput() {
            assertEquals("", new Base16().encodeToString(ArrayUtils.EMPTY_BYTE_ARRAY));
        }

        @Test
        void encodeToStringShouldReturnNullForNullInput() {
            assertNull(new Base16().encodeToString(null));
        }

        @Test
        void decodeStringShouldReturnCorrectBytes() {
            assertArrayEquals(HELLO_WORLD_BYTES, new Base16().decode(ENCODED_HELLO_WORLD_UPPERCASE));
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {
        @ParameterizedTest
        @ValueSource(chars = {'/', ':', '@', 'G', '%', '`', 'g'})
        @DisplayName("decode should throw IllegalArgumentException for non-Base16 characters")
        void testDecodeWithInvalidCharacters(final char invalidChar) {
            final byte[] encoded = {(byte) invalidChar};
            assertThrows(IllegalArgumentException.class, () -> new Base16().decode(encoded),
                "Invalid Base16 char: " + invalidChar);
        }

        @Test
        @DisplayName("decode should throw ArrayIndexOutOfBoundsException for out-of-range byte values")
        void testDecodeWithOutOfRangeByteValues() {
            // This test is for CODEC-68, where some bytes could cause an ArrayIndexOutOfBoundsException
            final byte[] x = {'n', 'H', '=', '=', (byte) 0x9c};
            final Base16 b16 = new Base16();
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> b16.decode(x));
        }

        @Test
        @DisplayName("encode should throw IllegalArgumentException for oversized length parameter")
        void testEncodeWithOversizedLength() {
            final Base16 base16 = new Base16();
            assertThrows(IllegalArgumentException.class, () -> base16.encode(new byte[10], 0, 1 << 30));
        }
    }

    @Nested
    @DisplayName("Object API (Encoder/Decoder) Tests")
    class ObjectApiTests {

        @Test
        void testEncodeObjectWithValidParameter() throws EncoderException {
            final Object origObj = HELLO_WORLD_BYTES;
            final Object oEncoded = new Base16().encode(origObj);
            final byte[] bArray = new Base16().decode((byte[]) oEncoded);
            assertEquals(HELLO_WORLD, new String(bArray, CHARSET_UTF8));
        }

        @Test
        void testEncodeObjectWithInvalidParameter() {
            assertThrows(EncoderException.class, () -> new Base16().encode("This is a string, not a byte array"));
        }

        @Test
        void testDecodeObjectWithValidParameter() throws DecoderException {
            final Object o = new Base16().encode(HELLO_WORLD_BYTES);
            final Object oDecoded = new Base16().decode(o);
            assertEquals(HELLO_WORLD, new String((byte[]) oDecoded, CHARSET_UTF8));
        }

        @Test
        void testDecodeObjectWithInvalidParameter() {
            assertThrows(DecoderException.class, () -> new Base16().decode(Integer.valueOf(5)));
        }
    }

    @Nested
    @DisplayName("Known Vector Tests")
    class KnownVectorTests {

        static Stream<Arguments> knownBase16Vectors() {
            return Stream.of(
                Arguments.of("The quick brown fox jumped over the lazy dogs.", "54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e"),
                Arguments.of("It was the best of times, it was the worst of times.", "497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e"),
                Arguments.of("http://jakarta.apache.org/commmons", "687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73"),
                Arguments.of("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz", "4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a"),
                Arguments.of("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }", "7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d"),
                Arguments.of("xyzzy!", "78797a7a7921")
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("knownBase16Vectors")
        @DisplayName("should encode known strings correctly (lowercase)")
        void testKnownEncodings(final String original, final String expected) {
            final Base16 base16 = new Base16(true); // Lowercase
            assertEquals(expected, base16.encodeToString(StringUtils.getBytesUtf8(original)));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("knownBase16Vectors")
        @DisplayName("should decode known strings correctly")
        void testKnownDecodings(final String expected, final String encoded) {
            final Base16 base16 = new Base16();
            final byte[] decodedBytes = base16.decode(StringUtils.getBytesUtf8(encoded));
            assertEquals(expected, StringUtils.newStringUtf8(decodedBytes));
        }
    }

    @Nested
    @DisplayName("Streaming API (Context) Tests")
    class StreamingApiTests {
        @Test
        @DisplayName("should decode correctly when data is fed byte-by-byte")
        void testDecodeByteByByte() {
            final String encoded = "556E74696C206E6578742074696D6521";
            final String decodedString = "Until next time!";
            final byte[] encodedBytes = StringUtils.getBytesUtf8(encoded);

            final Base16 b16 = new Base16();
            final BaseNCodec.Context context = new BaseNCodec.Context();

            for (int i = 0; i < encodedBytes.length; i++) {
                b16.decode(encodedBytes, i, 1, context);
            }
            b16.decode(encodedBytes, 0, -1, context); // Finalize

            final byte[] decodedBytes = new byte[context.pos];
            System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);

            assertEquals(decodedString, StringUtils.newStringUtf8(decodedBytes));
        }

        @Test
        @DisplayName("should decode correctly when hex pairs are split across calls")
        void testDecodeWithFragmentedHexPairs() {
            final String encoded = "4142434445"; // ABCDE
            final byte[] encodedBytes = StringUtils.getBytesUtf8(encoded);

            final Base16 b16 = new Base16();
            final BaseNCodec.Context context = new BaseNCodec.Context();

            // Pass odd, then even, then odd amount of data
            b16.decode(encodedBytes, 0, 3, context); // 414 -> "A" and half of "B"
            b16.decode(encodedBytes, 3, 4, context); // 24344 -> "BC" and half of "D"
            b16.decode(encodedBytes, 7, 3, context); // 445 -> "DE"
            b16.decode(encodedBytes, 0, -1, context); // Finalize

            final byte[] decodedBytes = new byte[context.pos];
            System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
            assertEquals("ABCDE", StringUtils.newStringUtf8(decodedBytes));
        }

        @Test
        @DisplayName("should optimize single-byte decoding using internal context")
        void testDecodeContextState() {
            final BaseNCodec.Context context = new BaseNCodec.Context();
            assertEquals(0, context.ibitWorkArea);
            assertNull(context.buffer);

            final Base16 b16 = new Base16();

            // First hex digit 'E'
            b16.decode(new byte[]{(byte) 'E'}, 0, 1, context);
            assertEquals(15, context.ibitWorkArea);
            assertNull(context.buffer);

            // Second hex digit 'F' completes the byte
            b16.decode(new byte[]{(byte) 'F'}, 0, 1, context);
            assertEquals(0, context.ibitWorkArea);
            assertEquals((byte) 0xEF, context.buffer[0]);
        }
    }

    @ParameterizedTest(name = "byte {0} -> \"{1}\"")
    @CsvSource({
            "0, '00'",
            "1, '01'",
            "10, '0A'",
            "15, '0F'",
            "16, '10'",
            "127, '7F'",
            "-1, 'FF'",
            "-128, '80'"
    })
    @DisplayName("should encode single bytes to two-digit hex strings")
    void testEncodeSingleByteToHexString(byte value, String expected) {
        assertEquals(expected, new Base16().encodeToString(new byte[]{value}));
    }

    @ParameterizedTest(name = "byte[] {0} -> \"{1}\"")
    @MethodSource
    @DisplayName("should encode byte pairs to four-digit hex strings")
    void testEncodeBytePairsToHexString(byte[] data, String expected) {
        assertEquals(expected, new Base16().encodeToString(data));
    }

    static Stream<Arguments> testEncodeBytePairsToHexString() {
        return IntStream.of(0, 1, 15, 16, 127, -1, -128)
            .mapToObj(i -> Arguments.of(new byte[]{(byte) i, (byte) i}, String.format("%02X%02X", (byte) i, (byte) i)));
    }
}