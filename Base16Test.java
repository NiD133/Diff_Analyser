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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
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
@DisplayName("Tests for Base16 codec")
class Base16Test {

    private static final String HELLO_WORLD_STR = "Hello World";
    private static final byte[] HELLO_WORLD_BYTES = HELLO_WORLD_STR.getBytes(StandardCharsets.UTF_8);
    private static final String HELLO_WORLD_B16_UPPER = "48656C6C6F20576F726C64";
    private static final String HELLO_WORLD_B16_LOWER = "48656c6c6f20576f726c64";

    private final Random random = new Random();

    @Test
    @DisplayName("Should encode and decode a simple string correctly")
    void shouldEncodeAndDecodeBasicString() throws DecoderException {
        final Base16 base16 = new Base16();

        final byte[] encoded = base16.encode(HELLO_WORLD_BYTES);
        assertEquals(HELLO_WORLD_B16_UPPER, new String(encoded, StandardCharsets.UTF_8));

        final byte[] decoded = base16.decode(HELLO_WORLD_B16_UPPER);
        assertArrayEquals(HELLO_WORLD_BYTES, decoded);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 100",
        "100, 0",
        "100, 100"
    })
    @DisplayName("Should encode a byte array segment correctly when padded in a larger buffer")
    void shouldEncodeWithBufferPadding(final int startPadSize, final int endPadSize) {
        final Base16 base16 = new Base16();
        byte[] buffer = ArrayUtils.addAll(HELLO_WORLD_BYTES, new byte[endPadSize]);
        buffer = ArrayUtils.addAll(new byte[startPadSize], buffer);

        final byte[] encodedBytes = base16.encode(buffer, startPadSize, HELLO_WORLD_BYTES.length);
        assertEquals(HELLO_WORLD_B16_UPPER, new String(encodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Should throw when attempting to encode with a length that exceeds integer limits")
    void shouldThrowOnEncodeLengthOverflow() {
        final Base16 base16 = new Base16();
        // This length will cause an integer overflow when calculating the output buffer size (len * 2)
        assertThrows(IllegalArgumentException.class, () -> base16.encode(new byte[10], 0, 1 << 30));
    }

    @Nested
    @DisplayName("Constructor and Alphabet Tests")
    class ConstructorAndAlphabetTests {

        @Test
        @DisplayName("Constructor with 'true' flag should use lower-case alphabet for encoding")
        void constructor_withTrue_shouldUseLowerCase() {
            final Base16 base16 = new Base16(true);
            final String result = base16.encodeToString(HELLO_WORLD_BYTES);
            assertEquals(HELLO_WORLD_B16_LOWER, result);
        }

        @Test
        @DisplayName("Constructor with 'false' flag should use upper-case alphabet for encoding")
        void constructor_withFalse_shouldUseUpperCase() {
            final Base16 base16 = new Base16(false);
            final String result = base16.encodeToString(HELLO_WORLD_BYTES);
            assertEquals(HELLO_WORLD_B16_UPPER, result);
        }

        @Test
        @DisplayName("isInAlphabet should return false for bytes outside the 0-127 range")
        void isInAlphabet_shouldReturnFalse_forInvalidBounds() {
            final Base16 b16 = new Base16();
            assertFalse(b16.isInAlphabet((byte) -1));
            assertFalse(b16.isInAlphabet((byte) 128));
            assertFalse(b16.isInAlphabet((byte) 255));
        }

        @Test
        @DisplayName("isInAlphabet should correctly identify characters for the lower-case alphabet")
        void isInAlphabet_shouldWorkForLowerCase() {
            final Base16 b16 = new Base16(true); // Lower-case
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(b16.isInAlphabet((byte) c));
            }
            for (char c = 'a'; c <= 'f'; c++) {
                assertTrue(b16.isInAlphabet((byte) c));
            }
            for (char c = 'A'; c <= 'F'; c++) {
                assertFalse(b16.isInAlphabet((byte) c));
            }
        }

        @Test
        @DisplayName("isInAlphabet should correctly identify characters for the upper-case alphabet")
        void isInAlphabet_shouldWorkForUpperCase() {
            final Base16 b16 = new Base16(false); // Upper-case
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(b16.isInAlphabet((byte) c));
            }
            for (char c = 'a'; c <= 'f'; c++) {
                assertFalse(b16.isInAlphabet((byte) c));
            }
            for (char c = 'A'; c <= 'F'; c++) {
                assertTrue(b16.isInAlphabet((byte) c));
            }
        }
    }

    @Nested
    @DisplayName("Encoding Tests")
    class EncodingTests {

        @Test
        @DisplayName("encode(null) should return null")
        void encode_withNull_shouldReturnNull() {
            assertNull(new Base16().encode(null));
        }

        @Test
        @DisplayName("encode(empty array) should return empty array")
        void encode_withEmptyArray_shouldReturnEmptyArray() {
            assertArrayEquals(new byte[0], new Base16().encode(new byte[0]));
        }

        @Test
        @DisplayName("encodeToString(null) should return null")
        void encodeToString_withNull_shouldReturnNull() {
            assertNull(new Base16().encodeToString(null));
        }

        @Test
        @DisplayName("encodeToString(empty array) should return empty string")
        void encodeToString_withEmptyArray_shouldReturnEmptyString() {
            assertEquals("", new Base16().encodeToString(new byte[0]));
        }

        @ParameterizedTest(name = "Byte {0} should be encoded to ''{1}''")
        @MethodSource("org.apache.commons.codec.binary.Base16Test#singleByteProvider")
        @DisplayName("Should encode single bytes correctly")
        void shouldEncodeSingleBytes(byte input, String expectedUpperCase) {
            final Base16 base16 = new Base16();
            final byte[] data = {input};
            assertEquals(expectedUpperCase, base16.encodeToString(data));
        }
    }

    @Nested
    @DisplayName("Decoding Tests")
    class DecodingTests {

        @Test
        @DisplayName("decode(null) should return null")
        void decode_withNull_shouldReturnNull() throws DecoderException {
            assertNull(new Base16().decode((byte[]) null));
            assertNull(new Base16().decode((String) null));
        }

        @Test
        @DisplayName("decode(empty) should return empty array")
        void decode_withEmpty_shouldReturnEmptyArray() throws DecoderException {
            assertArrayEquals(new byte[0], new Base16().decode(new byte[0]));
            assertEquals("", new String(new Base16().decode(""), StandardCharsets.UTF_8));
        }

        @ParameterizedTest(name = "Decoding ''{1}'' should produce ''{0}''")
        @CsvSource({
            "'The quick brown fox jumped over the lazy dogs.', '54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e'",
            "'It was the best of times, it was the worst of times.', '497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e'",
            "'http://jakarta.apache.org/commmons', '687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73'",
            "'AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz', '4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a'",
            "'{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }', '7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d'",
            "'xyzzy!', '78797a7a7921'"
        })
        @DisplayName("Should decode known Base16 strings correctly")
        void shouldDecodeKnownBase16Strings(String decoded, String encoded) throws DecoderException {
            // Use default upper-case decoder as test data is upper-case
            final Base16 base16 = new Base16();
            final byte[] expected = decoded.getBytes(StandardCharsets.UTF_8);
            assertArrayEquals(expected, base16.decode(encoded));
        }

        @ParameterizedTest(name = "Decoding with invalid char ''{0}'' should throw")
        @ValueSource(chars = {'/', ':', '@', 'G', '%', '`', 'g', 'n', 'H', '=', '\u009c'})
        @DisplayName("Should throw when decoding data with invalid Base16 characters")
        void shouldThrowOnDecodeInvalidCharacter(char invalidChar) {
            final Base16 base16 = new Base16();
            final byte[] encoded = {(byte) invalidChar};
            assertThrows(IllegalArgumentException.class, () -> base16.decode(encoded),
                "Should throw for invalid Base16 char: " + invalidChar);
        }
    }

    @Nested
    @DisplayName("Codec Policy Tests")
    class PolicyTests {

        @Test
        @DisplayName("Strict policy should throw for input with an odd number of characters")
        void strictPolicy_shouldThrow_forOddLengthInput() {
            final String encoded = "aabbccdde"; // Odd length
            final Base16 b16 = new Base16(true, CodecPolicy.STRICT);
            assertEquals(CodecPolicy.STRICT, b16.getCodecPolicy());
            assertThrows(IllegalArgumentException.class, () -> b16.decode(encoded.getBytes(StandardCharsets.UTF_8)));
        }

        @Test
        @DisplayName("Lenient policy should ignore a final, incomplete hex pair")
        void lenientPolicy_shouldIgnore_forOddLengthInput() {
            final String encoded = "aabbccdde"; // Final 'e' is ignored
            final Base16 b16 = new Base16(true, CodecPolicy.LENIENT);
            assertEquals(CodecPolicy.LENIENT, b16.getCodecPolicy());

            final byte[] decoded = b16.decode(encoded.getBytes(StandardCharsets.UTF_8));
            assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd}, decoded);
        }
    }

    @Nested
    @DisplayName("Streaming API Tests")
    class StreamingApiTests {

        @Test
        @DisplayName("Should decode correctly when data is fed in chunks of varying sizes")
        void shouldDecodeInChunks() {
            final String original = "Until next time!";
            final String encoded = "556E74696C206E6578742074696D6521";
            final byte[] encodedBytes = encoded.getBytes(StandardCharsets.UTF_8);

            final Base16 b16 = new Base16();
            final BaseNCodec.Context context = new BaseNCodec.Context();

            // Decode byte-by-byte for the first two bytes
            b16.decode(encodedBytes, 0, 1, context);
            b16.decode(encodedBytes, 1, 1, context); // Decodes "U"
            b16.decode(encodedBytes, 2, 1, context);
            b16.decode(encodedBytes, 3, 1, context); // Decodes "n"

            // Decode in chunks that split hex pairs
            b16.decode(encodedBytes, 4, 3, context);  // Decodes "t" and half of "i"
            b16.decode(encodedBytes, 7, 3, context);  // Decodes "il" and half of " "
            b16.decode(encodedBytes, 10, 3, context); // Decodes " " and half of "n"

            // Decode the rest
            b16.decode(encodedBytes, 13, 19, context); // Decodes "next time!"

            final byte[] decodedBytes = new byte[context.pos];
            System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);

            assertEquals(original, new String(decodedBytes, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("Should process single bytes efficiently without intermediate buffering")
        void shouldOptimizeSingleByteDecoding() {
            final Base16 b16 = new Base16();
            final BaseNCodec.Context context = new BaseNCodec.Context();
            assertEquals(0, context.ibitWorkArea);
            assertNull(context.buffer);

            // First hex char 'E' is stored in the work area
            b16.decode(new byte[]{(byte) 'E'}, 0, 1, context);
            assertEquals(14, context.ibitWorkArea); // 'E' is 14
            assertNull(context.buffer);

            // Second hex char 'F' completes the byte 0xEF
            b16.decode(new byte[]{(byte) 'F'}, 0, 1, context);
            assertEquals(0, context.ibitWorkArea); // Work area is cleared
            assertEquals((byte) 0xEF, context.buffer[0]);
        }
    }

    @Nested
    @DisplayName("Codec Interface (Object) Tests")
    class CodecInterfaceTests {

        @Test
        @DisplayName("decode(Object) should throw DecoderException for invalid object types")
        void decodeObject_withInvalidType_shouldThrow() {
            assertThrows(DecoderException.class, () -> new Base16().decode(5));
        }

        @Test
        @DisplayName("decode(Object) should work for valid encoded byte arrays and strings")
        void decodeObject_withValidTypes_shouldSucceed() throws Exception {
            final Base16 b16 = new Base16();

            // Test with byte[]
            Object decodedObject1 = b16.decode((Object) HELLO_WORLD_B16_UPPER.getBytes(StandardCharsets.UTF_8));
            assertArrayEquals(HELLO_WORLD_BYTES, (byte[]) decodedObject1);

            // Test with String
            Object decodedObject2 = b16.decode((Object) HELLO_WORLD_B16_UPPER);
            assertArrayEquals(HELLO_WORLD_BYTES, (byte[]) decodedObject2);
        }

        @Test
        @DisplayName("encode(Object) should throw EncoderException for non-byte-array types")
        void encodeObject_withInvalidType_shouldThrow() {
            assertThrows(EncoderException.class, () -> new Base16().encode("Yadayadayada"));
        }

        @Test
        @DisplayName("encode(Object) should work for byte arrays")
        void encodeObject_withByteArray_shouldSucceed() throws Exception {
            final Object oEncoded = new Base16().encode((Object) HELLO_WORLD_BYTES);
            assertArrayEquals(HELLO_WORLD_B16_UPPER.getBytes(StandardCharsets.UTF_8), (byte[]) oEncoded);
        }
    }

    @Nested
    @DisplayName("Property-Based Tests")
    class PropertyBasedTests {

        @Test
        @DisplayName("Encoding and then decoding should return the original data for all byte values")
        void encodeAndDecode_shouldBeReversible_forAllBytes() {
            final Base16 base16 = new Base16();
            for (int i = -128; i < 128; i++) {
                final byte[] data = {(byte) i};
                final byte[] encoded = base16.encode(data);
                final byte[] decoded = base16.decode(encoded);
                assertArrayEquals(data, decoded, "Failed for byte value: " + i);
            }
        }

        @Test
        @DisplayName("Encoding and then decoding should return the original data for small random arrays")
        void encodeAndDecode_shouldBeReversible_forSmallArrays() {
            final Base16 base16 = new Base16();
            for (int i = 0; i < 12; i++) {
                final byte[] data = new byte[i];
                random.nextBytes(data);
                final byte[] encoded = base16.encode(data);
                final byte[] decoded = base16.decode(encoded);
                assertArrayEquals(data, decoded, "Failed for data: " + Arrays.toString(data));
            }
        }

        @Test
        @DisplayName("Encoding and then decoding should return the original data for large random arrays")
        void encodeAndDecode_shouldBeReversible_forLargeArrays() {
            final Base16 base16 = new Base16();
            for (int i = 0; i < 5; i++) {
                final int len = random.nextInt(10000) + 1;
                final byte[] data = new byte[len];
                random.nextBytes(data);
                final byte[] encoded = base16.encode(data);
                final byte[] decoded = base16.decode(encoded);
                assertArrayEquals(data, decoded);
            }
        }
    }

    // Provider for parameterized tests
    static Stream<Arguments> singleByteProvider() {
        return Stream.of(
            Arguments.of((byte) 0, "00"),
            Arguments.of((byte) 1, "01"),
            Arguments.of((byte) 10, "0A"),
            Arguments.of((byte) 15, "0F"),
            Arguments.of((byte) 16, "10"),
            Arguments.of((byte) 127, "7F"),
            Arguments.of((byte) -1, "FF"), // (byte) 255
            Arguments.of((byte) -128, "80") // (byte) 128
        );
    }
}