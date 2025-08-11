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
package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Understandable and maintainable unit tests for {@link BCodec}.
 */
@DisplayName("BCodec Test Suite")
class BCodecTest {

    private BCodec bCodec;

    @BeforeEach
    void setUp() {
        // A new BCodec with default settings (UTF-8, lenient) is created for each test.
        bCodec = new BCodec();
    }

    @Test
    @DisplayName("getEncoding() should always return 'B'")
    void getEncoding_shouldReturnB() {
        assertEquals("B", bCodec.getEncoding());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should use UTF-8")
        void defaultConstructor_usesUTF8() {
            String encoded = bCodec.encode("test");
            assertTrue(encoded.startsWith("=?UTF-8?B?"), "Default charset should be UTF-8");
        }

        @Test
        @DisplayName("Constructor with Charset object should set the charset")
        void constructor_withCharsetObject_setsCharset() {
            BCodec codec = new BCodec(StandardCharsets.US_ASCII);
            String encoded = codec.encode("test");
            assertTrue(encoded.startsWith("=?US-ASCII?B?"), "Charset should be set to US-ASCII");
        }

        @Test
        @DisplayName("Constructor with charset name should set the charset")
        void constructor_withCharsetName_setsCharset() {
            BCodec codec = new BCodec("US-ASCII");
            String encoded = codec.encode("test");
            assertTrue(encoded.startsWith("=?US-ASCII?B?"), "Charset should be set to US-ASCII");
        }

        @Test
        @DisplayName("Constructor with null Charset object should throw NullPointerException")
        void constructor_withNullCharsetObject_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> new BCodec((Charset) null));
        }

        @Test
        @DisplayName("Constructor with null Charset object and policy should throw NullPointerException")
        void constructor_withNullCharsetObjectAndPolicy_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> new BCodec(null, CodecPolicy.STRICT));
        }

        @Test
        @DisplayName("Constructor with null charset name should throw IllegalArgumentException")
        void constructor_withNullCharsetName_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> new BCodec((String) null));
        }

        @Test
        @DisplayName("Constructor with illegal charset name should throw IllegalCharsetNameException")
        void constructor_withIllegalCharsetName_throwsIllegalCharsetNameException() {
            assertThrows(IllegalCharsetNameException.class, () -> new BCodec("!@#$"));
        }

        @Test
        @DisplayName("Constructor with unsupported charset name should throw UnsupportedCharsetException")
        void constructor_withUnsupportedCharsetName_throwsUnsupportedCharsetException() {
            assertThrows(UnsupportedCharsetException.class, () -> new BCodec("unsupported-charset"));
        }
    }

    @Nested
    @DisplayName("String Encoding Tests")
    class StringEncodingTests {

        @Test
        @DisplayName("encode(String) should return null for null input")
        void encode_nullString_returnsNull() throws EncoderException {
            assertNull(bCodec.encode((String) null));
        }

        @Test
        @DisplayName("encode(String, Charset) should return null for null input string")
        void encode_nullStringWithCharset_returnsNull() throws EncoderException {
            assertNull(bCodec.encode(null, StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("encode(String) should correctly encode a simple string with default charset")
        void encode_simpleString_encodesCorrectly() throws EncoderException {
            String plainText = "Hello World!";
            String expected = "=?UTF-8?B?SGVsbG8gV29ybGQh?=";
            assertEquals(expected, bCodec.encode(plainText));
        }

        @Test
        @DisplayName("encode(String, Charset) should correctly encode a string with a specified charset")
        void encode_stringWithSpecifiedCharset_encodesCorrectly() throws EncoderException {
            String plainText = "Hello World!";
            String expected = "=?US-ASCII?B?SGVsbG8gV29ybGQh?=";
            assertEquals(expected, bCodec.encode(plainText, StandardCharsets.US_ASCII));
        }

        @Test
        @DisplayName("encode(String, String) should throw UnsupportedCharsetException for an unsupported charset name")
        void encode_withUnsupportedCharsetName_throwsUnsupportedCharsetException() {
            // Charset.forName() is called internally and will throw this unchecked exception for an invalid name.
            assertThrows(UnsupportedCharsetException.class, () -> bCodec.encode("test", "unsupported-charset"));
        }

        @Test
        @DisplayName("encode(String, String) should throw IllegalCharsetNameException for an illegal charset name")
        void encode_withIllegalCharsetName_throwsIllegalCharsetNameException() {
            // An empty string is an illegal charset name.
            assertThrows(IllegalCharsetNameException.class, () -> bCodec.encode("test", ""));
        }

        @Test
        @DisplayName("encode(String, Charset) should throw NullPointerException for null charset")
        void encode_withNullCharsetObject_throwsNullPointerException() {
            // This behavior comes from String.getBytes(Charset) which is called internally.
            assertThrows(NullPointerException.class, () -> bCodec.encode("test", (Charset) null));
        }
    }

    @Nested
    @DisplayName("String Decoding Tests")
    class StringDecodingTests {

        @Test
        @DisplayName("decode(String) should return null for null input")
        void decode_nullString_returnsNull() throws DecoderException {
            assertNull(bCodec.decode(null));
        }

        @Test
        @DisplayName("decode(String) should correctly decode a valid string")
        void decode_validString_decodesCorrectly() throws DecoderException {
            String encodedText = "=?UTF-8?B?SGVsbG8gV29ybGQh?=";
            String expected = "Hello World!";
            assertEquals(expected, bCodec.decode(encodedText));
        }

        @Test
        @DisplayName("decode(String) should return an empty string for an empty payload")
        void decode_emptyPayload_returnsEmptyString() throws DecoderException {
            assertEquals("", bCodec.decode("=?UTF-8?B??="));
        }

        @Test
        @DisplayName("decode(String) should throw DecoderException for a malformed string")
        void decode_malformedString_throwsDecoderException() {
            // The input string is missing the required "?=" at the end.
            String malformed = "=?UTF-8?B?SGVsbG8";
            DecoderException e = assertThrows(DecoderException.class, () -> bCodec.decode(malformed));
            assertTrue(e.getMessage().contains("malformed encoded content"));
        }

        @Test
        @DisplayName("decode(String) should throw DecoderException for string with invalid Base64 characters")
        void decode_invalidBase64Chars_throwsDecoderException() {
            // The payload contains a '%' which is not a valid Base64 character.
            String malformed = "=?UTF-8?B?SGVsb%8gV29ybGQh?=";
            // The underlying Base64 decoder will throw an exception, which is wrapped.
            assertThrows(DecoderException.class, () -> bCodec.decode(malformed));
        }
    }

    @Nested
    @DisplayName("Object-based API Tests")
    class ObjectApiTests {

        @Test
        @DisplayName("encode(Object) should return null for null input")
        void encode_nullObject_returnsNull() throws EncoderException {
            assertNull(bCodec.encode((Object) null));
        }

        @Test
        @DisplayName("encode(Object) should correctly encode a String object")
        void encode_stringObject_encodesCorrectly() throws EncoderException {
            String plainText = "test";
            String expected = "=?UTF-8?B?dGVzdA==?=";
            assertEquals(expected, bCodec.encode((Object) plainText));
        }

        @Test
        @DisplayName("encode(Object) should throw EncoderException for unsupported object types")
        void encode_unsupportedObject_throwsEncoderException() {
            EncoderException e = assertThrows(EncoderException.class, () -> bCodec.encode(new Object()));
            assertEquals("Objects of type java.lang.Object cannot be encoded using BCodec", e.getMessage());
        }

        @Test
        @DisplayName("decode(Object) should return null for null input")
        void decode_nullObject_returnsNull() throws DecoderException {
            assertNull(bCodec.decode((Object) null));
        }

        @Test
        @DisplayName("decode(Object) should correctly decode an encoded String object")
        void decode_encodedStringObject_decodesCorrectly() throws DecoderException {
            String encodedText = "=?UTF-8?B?dGVzdA==?=";
            String expected = "test";
            assertEquals(expected, bCodec.decode((Object) encodedText));
        }

        @Test
        @DisplayName("decode(Object) should throw DecoderException for unsupported object types")
        void decode_unsupportedObject_throwsDecoderException() {
            DecoderException e = assertThrows(DecoderException.class, () -> bCodec.decode(new Object()));
            assertEquals("Objects of type java.lang.Object cannot be decoded using BCodec", e.getMessage());
        }
    }

    @Nested
    @DisplayName("CodecPolicy Tests")
    class CodecPolicyTests {

        @Test
        @DisplayName("isStrictDecoding should be false for default constructor")
        void isStrictDecoding_isFalseByDefault() {
            assertFalse(bCodec.isStrictDecoding(), "Default policy should be lenient (not strict)");
        }

        @Test
        @DisplayName("isStrictDecoding should be true when constructed with STRICT policy")
        void isStrictDecoding_isTrueForStrictPolicy() {
            BCodec strictCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
            assertTrue(strictCodec.isStrictDecoding(), "Policy should be strict");
        }

        @Test
        @DisplayName("doDecoding with STRICT policy should throw for invalid Base64 input")
        void doDecoding_strictPolicyWithInvalidInput_throwsException() {
            BCodec strictCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
            // This byte array represents a Base64 string that is valid in lenient mode but not strict,
            // because it has a single trailing encoded character ('-').
            byte[] invalidForStrict = "AAAA-".getBytes(StandardCharsets.US_ASCII);

            // The underlying Base64 decoder in strict mode throws an IllegalArgumentException.
            assertThrows(IllegalArgumentException.class, () -> strictCodec.doDecoding(invalidForStrict));
        }
    }

    @Nested
    @DisplayName("Internal Byte-level Helper Method Tests")
    class InternalMethodTests {

        @Test
        @DisplayName("doEncoding and doDecoding should perform a correct round trip")
        void doEncodingAndDoDecoding_roundTrip() {
            byte[] originalData = "This is a test string.".getBytes(StandardCharsets.UTF_8);
            byte[] encodedData = bCodec.doEncoding(originalData);
            byte[] decodedData = bCodec.doDecoding(encodedData);
            assertArrayEquals(originalData, decodedData);
        }

        @Test
        @DisplayName("doEncoding should return null for null input")
        void doEncoding_nullInput_returnsNull() {
            assertNull(bCodec.doEncoding(null));
        }

        @Test
        @DisplayName("doDecoding should return null for null input")
        void doDecoding_nullInput_returnsNull() {
            assertNull(bCodec.doDecoding(null));
        }

        @Test
        @DisplayName("doEncoding should return an empty array for an empty input")
        void doEncoding_emptyInput_returnsEmptyArray() {
            assertArrayEquals(new byte[0], bCodec.doEncoding(new byte[0]));
        }

        @Test
        @DisplayName("doDecoding should return an empty array for an empty input")
        void doDecoding_emptyInput_returnsEmptyArray() {
            assertArrayEquals(new byte[0], bCodec.doDecoding(new byte[0]));
        }
    }
}