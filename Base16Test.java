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
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Base16} implementation.
 */
class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    private static final String HELLO_WORLD = "Hello World";
    private static final String HELLO_WORLD_HEX = "48656C6C6F20576F726C64";

    private final Random random = new Random();

    // ====================== Basic Functionality Tests ======================

    @Test
    void encodeAndDecode_WithValidInput_ReturnsOriginalString() {
        final byte[] encoded = new Base16().encode(HELLO_WORLD.getBytes(CHARSET_UTF8));
        assertEquals(HELLO_WORLD_HEX, new String(encoded, CHARSET_UTF8));

        final byte[] decoded = new Base16().decode(encoded);
        assertEquals(HELLO_WORLD, new String(decoded, CHARSET_UTF8));
    }

    @Test
    void encodeAndDecode_WithEmptyInput_ReturnsEmpty() {
        final byte[] empty = {};
        final byte[] encoded = new Base16().encode(empty);
        assertEquals(0, encoded.length);
        assertNull(new Base16().encode(null));

        final byte[] decoded = new Base16().decode(empty);
        assertEquals(0, decoded.length);
        assertNull(new Base16().decode(null));
    }

    // ====================== Buffer Position Tests ======================

    @Test
    void encode_WithDataAtBufferStart_CorrectlyEncoded() {
        testEncodeInBuffer(0, 100);
    }

    @Test
    void encode_WithDataAtBufferMiddle_CorrectlyEncoded() {
        testEncodeInBuffer(100, 100);
    }

    @Test
    void encode_WithDataAtBufferEnd_CorrectlyEncoded() {
        testEncodeInBuffer(100, 0);
    }

    private void testEncodeInBuffer(final int startPadSize, final int endPadSize) {
        final byte[] originalBytes = HELLO_WORLD.getBytes(CHARSET_UTF8);
        byte[] buffer = ArrayUtils.addAll(originalBytes, new byte[endPadSize]);
        buffer = ArrayUtils.addAll(new byte[startPadSize], buffer);

        final byte[] encodedBytes = new Base16().encode(buffer, startPadSize, originalBytes.length);
        final String encodedContent = new String(encodedBytes, CHARSET_UTF8);

        assertEquals(HELLO_WORLD_HEX, encodedContent);
    }

    // ====================== String/Byte Conversion Tests ======================

    @Test
    void encode_WithVariousInputs_ProducesExpectedString() {
        final Base16 base16 = new Base16();
        final byte[] b1 = HELLO_WORLD.getBytes(CHARSET_UTF8);
        final byte[] b2 = {};
        final byte[] b3 = null;

        // Test encodeToString
        assertEquals(HELLO_WORLD_HEX, base16.encodeToString(b1));
        assertEquals("", base16.encodeToString(b2));
        assertNull(base16.encodeToString(b3));

        // Test direct encode
        assertEquals(HELLO_WORLD_HEX, new String(new Base16().encode(b1), CHARSET_UTF8));
        assertEquals("", new String(new Base16().encode(b2), CHARSET_UTF8));
        assertNull(new Base16().encode(b3));
    }

    @Test
    void decode_WithVariousStringInputs_ProducesExpected() throws DecoderException {
        final Base16 base16 = new Base16();
        final String s1 = HELLO_WORLD_HEX;
        final String s2 = "";
        final String s3 = null;

        // Test decode
        assertEquals(HELLO_WORLD, new String(base16.decode(s1), CHARSET_UTF8));
        assertEquals("", new String(base16.decode(s2), CHARSET_UTF8));
        assertNull(base16.decode(s3));

        // Test object-based decode
        assertEquals(HELLO_WORLD, new String((byte[]) new Base16().decode((Object) s1)), CHARSET_UTF8));
    }

    // ====================== Known Values Tests ======================

    @Test
    void encode_KnownStrings_ProducesExpectedHex() {
        final Base16 base16 = new Base16(true); // Use lower-case for known encodings
        verifyKnownEncoding("The quick brown fox jumped over the lazy dogs.", 
            "54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e", base16);
        verifyKnownEncoding("It was the best of times, it was the worst of times.", 
            "497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e", base16);
        verifyKnownEncoding("http://jakarta.apache.org/commmons", 
            "687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73", base16);
        verifyKnownEncoding("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz", 
            "4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a", base16);
        verifyKnownEncoding("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }", 
            "7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d", base16);
        verifyKnownEncoding("xyzzy!", 
            "78797a7a7921", base16);
    }

    @Test
    void decode_KnownHexStrings_ProducesExpected() {
        verifyKnownDecoding("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e", 
            "The quick brown fox jumped over the lazy dogs.");
        verifyKnownDecoding("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e", 
            "It was the best of times, it was the worst of times.");
        verifyKnownDecoding("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73", 
            "http://jakarta.apache.org/commmons");
        verifyKnownDecoding("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a", 
            "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz");
        verifyKnownDecoding("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d", 
            "{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }");
        verifyKnownDecoding("78797a7a7921", 
            "xyzzy!");
    }

    private void verifyKnownEncoding(final String input, final String expectedHex, final Base16 base16) {
        final byte[] encoded = base16.encode(input.getBytes(CHARSET_UTF8));
        assertEquals(expectedHex, new String(encoded, CHARSET_UTF8));
    }

    private void verifyKnownDecoding(final String hexInput, final String expectedOutput) {
        final byte[] decoded = new Base16(true).decode(hexInput.getBytes(CHARSET_UTF8));
        assertEquals(expectedOutput, new String(decoded, CHARSET_UTF8));
    }

    // ====================== Constructor Tests ======================

    @Test
    void constructor_Instantiation_Succeeds() {
        // Verify all constructors can be instantiated without exceptions
        new Base16();
        new Base16(false);
        new Base16(true);
        new Base16(false, CodecPolicy.LENIENT);
        new Base16(false, CodecPolicy.STRICT);
    }

    @Test
    void constructor_LowerCase_EncodesToLowerCase() {
        final Base16 base16 = new Base16(true);
        final byte[] encoded = base16.encode(BaseNTestData.DECODED);
        final String result = new String(encoded, CHARSET_UTF8);
        assertEquals(Base16TestData.ENCODED_UTF8_LOWERCASE, result);
    }

    @Test
    void constructor_LowerCaseWithStrictPolicy_EncodesToUpperCase() {
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
        final byte[] encoded = base16.encode(BaseNTestData.DECODED);
        final String result = new String(encoded, CHARSET_UTF8);
        assertEquals(Base16TestData.ENCODED_UTF8_UPPERCASE, result);
    }

    // ====================== Alphabet Validation Tests ======================

    @Test
    void isInAlphabet_WithVariousBytes_ReturnsExpected() {
        // Test lower-case configuration
        Base16 b16 = new Base16(true);
        verifyAlphabetForLowerCase(b16);

        // Test upper-case configuration
        b16 = new Base16(false);
        verifyAlphabetForUpperCase(b16);
    }

    private void verifyAlphabetForLowerCase(final Base16 base16) {
        // Valid digits
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        // Valid lower-case letters
        for (char c = 'a'; c <= 'f'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        // Invalid upper-case letters
        for (char c = 'A'; c <= 'F'; c++) {
            assertFalse(base16.isInAlphabet((byte) c));
        }
        // Boundaries
        assertFalse(base16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(base16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(base16.isInAlphabet((byte) ('a' - 1)));
        assertFalse(base16.isInAlphabet((byte) ('f' + 1)));
    }

    private void verifyAlphabetForUpperCase(final Base16 base16) {
        // Valid digits
        for (char c = '0'; c <= '9'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        // Valid upper-case letters
        for (char c = 'A'; c <= 'F'; c++) {
            assertTrue(base16.isInAlphabet((byte) c));
        }
        // Invalid lower-case letters
        for (char c = 'a'; c <= 'f'; c++) {
            assertFalse(base16.isInAlphabet((byte) c));
        }
        // Boundaries
        assertFalse(base16.isInAlphabet((byte) ('0' - 1)));
        assertFalse(base16.isInAlphabet((byte) ('9' + 1)));
        assertFalse(base16.isInAlphabet((byte) ('A' - 1)));
        assertFalse(base16.isInAlphabet((byte) ('F' + 1)));
    }

    // ====================== Random Data Round-Trip Tests ======================

    @Test
    void encodeAndDecode_RandomData_RoundTripSuccessful() {
        final Random localRandom = new Random();
        for (int i = 1; i < 5; i++) {
            final int len = localRandom.nextInt(10000) + 1;
            final byte[] data = new byte[len];
            localRandom.nextBytes(data);
            final byte[] encoded = new Base16().encode(data);
            final byte[] decoded = new Base16().decode(encoded);
            assertArrayEquals(data, decoded);
        }
    }

    @Test
    void encodeAndDecode_SmallRandomData_RoundTripSuccessful() {
        final Random localRandom = new Random();
        for (int size = 0; size < 12; size++) {
            final byte[] data = new byte[size];
            localRandom.nextBytes(data);
            final byte[] encoded = new Base16().encode(data);
            final byte[] decoded = new Base16().decode(encoded);
            assertArrayEquals(data, decoded, () -> "Failed for size " + size + ": " + Arrays.toString(data));
        }
    }

    // ====================== Decoding Policy Tests ======================

    @Test
    void decode_WithTrailingHalfByteInLenientMode_IgnoresTrailing() {
        final String encoded = "aabbccdde"; // Trailing 'e' (half byte)
        final Base16 base16 = new Base16(true, CodecPolicy.LENIENT);
        final byte[] decoded = base16.decode(encoded.getBytes(CHARSET_UTF8));
        assertArrayEquals(new byte[] { (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd }, decoded);
    }

    @Test
    void decode_WithTrailingHalfByteInStrictMode_ThrowsException() {
        final String encoded = "aabbccdde"; // Trailing 'e' (half byte)
        final Base16 base16 = new Base16(true, CodecPolicy.STRICT);
        assertThrows(IllegalArgumentException.class, () -> base16.decode(encoded.getBytes(CHARSET_UTF8)));
    }

    // ====================== Invalid Input Handling Tests ======================

    @Test
    void encode_WithExcessiveLength_ThrowsIllegalArgumentException() {
        final Base16 base16 = new Base16();
        assertThrows(IllegalArgumentException.class, () -> base16.encode(new byte[10], 0, 1 << 30));
    }

    @Test
    void decode_WithInvalidBase16Chars_ThrowsException() {
        final byte[] invalidChars = { '/', ':', '@', 'G', '%', '`', 'g' };
        for (final byte invalidChar : invalidChars) {
            final byte[] invalidInput = { invalidChar };
            assertThrows(IllegalArgumentException.class, () -> new Base16().decode(invalidInput),
                "Expected exception for char: " + (char) invalidChar);
        }
    }

    @Test
    void decode_WithMalformedInput_ThrowsRuntimeException() {
        final byte[] malformed = { 'n', 'H', '=', '=', (byte) 0x9c };
        final Base16 base16 = new Base16();
        assertThrows(RuntimeException.class, () -> base16.decode(malformed));
    }

    // ====================== Object Method Tests ======================

    @Test
    void encode_WithValidByteArrayObject_ReturnsEncoded() throws Exception {
        final String original = "Hello World!";
        final Object origObj = original.getBytes(CHARSET_UTF8);
        final Object encodedObj = new Base16().encode(origObj);
        final byte[] decoded = new Base16().decode((byte[]) encodedObj);
        assertEquals(original, new String(decoded, CHARSET_UTF8));
    }

    @Test
    void encode_WithInvalidObject_ThrowsEncoderException() {
        assertThrows(EncoderException.class, () -> new Base16().encode("Invalid Object"));
    }

    @Test
    void decode_WithValidByteArrayObject_ReturnsOriginal() throws Exception {
        final String original = "Hello World!";
        final Object encoded = new Base16().encode(original.getBytes(CHARSET_UTF8));
        final Object decodedObj = new Base16().decode(encoded);
        assertEquals(original, new String((byte[]) decodedObj, CHARSET_UTF8));
    }

    @Test
    void decode_WithInvalidObject_ThrowsDecoderException() {
        assertThrows(DecoderException.class, () -> new Base16().decode(Integer.valueOf(5)));
    }

    // ====================== Byte-by-Byte Decoding Tests ======================

    @Test
    void decode_WithSingleBytes_ProducesExpectedResult() {
        final String encoded = "556E74696C206E6578742074696D6521"; // "Until next time!"
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 base16 = new Base16();
        final byte[] encodedBytes = encoded.getBytes(CHARSET_UTF8);

        // Simulate byte-by-byte decoding
        base16.decode(encodedBytes, 0, 1, context);
        base16.decode(encodedBytes, 1, 1, context); // 'U'
        base16.decode(encodedBytes, 2, 1, context);
        base16.decode(encodedBytes, 3, 1, context); // 'n'
        base16.decode(encodedBytes, 4, 3, context); // 't'
        base16.decode(encodedBytes, 7, 3, context); // 'il'
        base16.decode(encodedBytes, 10, 3, context); // ' '
        base16.decode(encodedBytes, 13, 19, context); // "next time!"

        final byte[] decoded = extractBuffer(context);
        assertEquals("Until next time!", new String(decoded, CHARSET_UTF8));
    }

    @Test
    void decode_WithOddEvenChunks_ProducesExpected() {
        final String encoded = "4142434445"; // "ABCDE"
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 base16 = new Base16();
        final byte[] encodedBytes = encoded.getBytes(CHARSET_UTF8);

        // Pass odd, even, then odd chunks
        base16.decode(encodedBytes, 0, 3, context); // 'A' and half of 'B'
        base16.decode(encodedBytes, 3, 4, context); // rest of 'B', 'C', 'D'
        base16.decode(encodedBytes, 7, 3, context); // 'E'

        final byte[] decoded = extractBuffer(context);
        assertEquals("ABCDE", new String(decoded, CHARSET_UTF8));
    }

    @Test
    void decode_WithSingleBytes_UpdatesContextCorrectly() {
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final Base16 base16 = new Base16();
        final byte[] data = new byte[1];

        // First byte 'E' (partial)
        data[0] = 'E';
        base16.decode(data, 0, 1, context);
        assertEquals(15, context.ibitWorkArea);
        assertNull(context.buffer);

        // Second byte 'F' completes the pair
        data[0] = 'F';
        base16.decode(data, 0, 1, context);
        assertEquals(0, context.ibitWorkArea);
        assertEquals((byte) 0xEF, context.buffer[0]);
    }

    private byte[] extractBuffer(final BaseNCodec.Context context) {
        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        return decodedBytes;
    }

    // ====================== Exact Encoding Value Tests ======================

    @ParameterizedTest
    @MethodSource("singleByteTestData")
    void encode_SingleByte_ProducesExpectedHexString(final byte input, final String expectedHex) {
        final byte[] encoded = new Base16().encode(new byte[] { input });
        assertEquals(expectedHex, new String(encoded, CHARSET_UTF8));
    }

    @ParameterizedTest
    @MethodSource("twoByteTestData")
    void encode_TwoBytes_ProducesExpectedHexString(final byte b1, final byte b2, final String expectedHex) {
        final byte[] encoded = new Base16().encode(new byte[] { b1, b2 });
        assertEquals(expectedHex, new String(encoded, CHARSET_UTF8));
    }

    @ParameterizedTest
    @MethodSource("threeByteTestData")
    void encode_ThreeBytes_ProducesExpectedHexString(final byte b1, final byte b2, final byte b3, final String expectedHex) {
        final byte[] encoded = new Base16().encode(new byte[] { b1, b2, b3 });
        assertEquals(expectedHex, new String(encoded, CHARSET_UTF8));
    }

    private static Stream<Arguments> singleByteTestData() {
        return Stream.of(
            Arguments.of((byte) 0, "00"),
            Arguments.of((byte) 1, "01"),
            // ... other single byte values ...
            Arguments.of((byte) 100, "64")
        );
    }

    private static Stream<Arguments> twoByteTestData() {
        return Stream.of(
            Arguments.of((byte) 0, (byte) 0, "0000"),
            Arguments.of((byte) 0, (byte) 1, "0001"),
            // ... other two-byte combinations ...
            Arguments.of((byte) 0, (byte) 15, "000F")
        );
    }

    private static Stream<Arguments> threeByteTestData() {
        return Stream.of(
            Arguments.of((byte) 0, (byte) 0, (byte) 0, "000000"),
            Arguments.of((byte) 0, (byte) 0, (byte) 1, "000001"),
            // ... other three-byte combinations ...
            Arguments.of((byte) 0, (byte) 0, (byte) 15, "00000F")
        );
    }

    // ====================== Round-Trip Tests ======================

    @Test
    void roundTrip_AllSingleBytes() {
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            final byte[] test = { (byte) i };
            final byte[] encoded = new Base16().encode(test);
            final byte[] decoded = new Base16().decode(encoded);
            assertArrayEquals(test, decoded);
        }
    }

    @Test
    void roundTrip_AllTwoBytePairs() {
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            final byte[] test = { (byte) i, (byte) i };
            final byte[] encoded = new Base16().encode(test);
            final byte[] decoded = new Base16().decode(encoded);
            assertArrayEquals(test, decoded);
        }
    }
}