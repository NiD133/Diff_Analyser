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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides well-structured and understandable tests for the {@link URLCodec} class.
 */
class URLCodecTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Nested
    @DisplayName("Constructor and Getter Tests")
    class ConstructorAndGetterTests {

        @Test
        @DisplayName("Default constructor should use UTF-8")
        void defaultConstructorUsesUtf8() {
            final URLCodec codec = new URLCodec();
            assertEquals(UTF_8, codec.getDefaultCharset(), "Default charset should be UTF-8");
        }

        @Test
        @DisplayName("Constructor should accept a custom charset")
        void constructorWithCustomCharset() {
            final URLCodec codec = new URLCodec("UTF-16");
            assertEquals("UTF-16", codec.getDefaultCharset(), "The specified charset should be set");
        }

        @Test
        @DisplayName("Constructor should accept a null charset")
        void constructorWithNullCharset() {
            final URLCodec codec = new URLCodec(null);
            assertNull(codec.getDefaultCharset(), "A null charset should be accepted");
        }

        @Test
        @DisplayName("getEncoding() should return the configured charset")
        void getEncodingReturnsCharset() {
            final URLCodec codec = new URLCodec(UTF_8);
            // getEncoding() is deprecated but should still work
            assertEquals(UTF_8, codec.getEncoding());
        }
    }

    @Nested
    @DisplayName("Encoding Tests")
    class EncodingTests {

        private final URLCodec codec = new URLCodec();

        @Test
        @DisplayName("Should encode string with unsafe characters")
        void encodeStringWithUnsafeCharacters() throws Exception {
            final String input = "Hello World!/?=";
            final String expected = "Hello+World%21%2F%3F%3D";
            assertEquals(expected, codec.encode(input));
        }

        @Test
        @DisplayName("Should encode string with a specified charset")
        void encodeStringWithCharset() throws Exception {
            final String input = "VoM;";
            final String expected = "VoM%3B";
            assertEquals(expected, codec.encode(input, UTF_8));
        }
        
        @Test
        @DisplayName("Should encode string with only safe characters as is")
        void encodeSafeString() throws Exception {
            final String input = "abcdefghijklmnopqrstuvwxyz.ABCDEFGHIJKLMNOPQRSTUVWXYZ-0123456789*_";
            assertEquals(input, codec.encode(input));
        }

        @Test
        @DisplayName("Should encode spaces as '+'")
        void encodeSpacesAsPlus() throws Exception {
            final String input = "a b c";
            assertEquals("a+b+c", codec.encode(input));
        }

        @Test
        @DisplayName("Should encode an empty string as an empty string")
        void encodeEmptyString() throws Exception {
            assertEquals("", codec.encode(""));
        }

        @Test
        @DisplayName("Should encode a null string as null")
        void encodeNullString() throws Exception {
            assertNull(codec.encode((String) null));
        }

        @Test
        @DisplayName("Should encode a byte array")
        void encodeByteArray() {
            final byte[] input = {(byte) 0x00, (byte) 0x10, (byte) 0x20, (byte) 0xFF};
            final byte[] expected = {'%', '0', '0', '%', '1', '0', '+', '%', 'F', 'F'};
            assertArrayEquals(expected, codec.encode(input));
        }

        @Test
        @DisplayName("Should encode an empty byte array as an empty byte array")
        void encodeEmptyByteArray() {
            assertArrayEquals(new byte[0], codec.encode(new byte[0]));
        }

        @Test
        @DisplayName("Should encode a null byte array as null")
        void encodeNullByteArray() {
            assertNull(codec.encode((byte[]) null));
        }

        @Test
        @DisplayName("Should encode an object of type String")
        void encodeObject() throws Exception {
            final String input = "test encoding";
            final String expected = "test+encoding";
            assertEquals(expected, codec.encode((Object) input));
        }

        @Test
        @DisplayName("Should encode a null object as null")
        void encodeNullObject() throws Exception {
            assertNull(codec.encode((Object) null));
        }
    }

    @Nested
    @DisplayName("Decoding Tests")
    class DecodingTests {

        private final URLCodec codec = new URLCodec();

        @Test
        @DisplayName("Should decode string with percent-encoded characters")
        void decodeString() throws Exception {
            final String input = "Hello+World%21%2F%3F%3D";
            final String expected = "Hello World!/?=";
            assertEquals(expected, codec.decode(input));
        }

        @Test
        @DisplayName("Should decode string with a specified charset")
        void decodeStringWithCharset() throws Exception {
            final String input = "VoM%3B";
            final String expected = "VoM;";
            assertEquals(expected, codec.decode(input, UTF_8));
        }

        @Test
        @DisplayName("Should decode '+' as space")
        void decodePlusAsSpace() throws Exception {
            assertEquals(" ", codec.decode("+"));
        }

        @Test
        @DisplayName("Should decode an empty string as an empty string")
        void decodeEmptyString() throws Exception {
            assertEquals("", codec.decode(""));
        }

        @Test
        @DisplayName("Should decode a null string as null")
        void decodeNullString() throws Exception {
            assertNull(codec.decode((String) null));
        }

        @Test
        @DisplayName("Should decode a byte array")
        void decodeByteArray() throws Exception {
            final byte[] input = {'%', '0', '0', '%', '1', '0', '+', '%', 'F', 'F'};
            final byte[] expected = {(byte) 0x00, (byte) 0x10, (byte) 0x20, (byte) 0xFF};
            assertArrayEquals(expected, codec.decode(input));
        }

        @Test
        @DisplayName("Should decode an empty byte array as an empty byte array")
        void decodeEmptyByteArray() throws Exception {
            assertArrayEquals(new byte[0], codec.decode(new byte[0]));
        }

        @Test
        @DisplayName("Should decode a null byte array as null")
        void decodeNullByteArray() throws Exception {
            assertNull(codec.decode((byte[]) null));
        }

        @Test
        @DisplayName("Should decode an object of type String")
        void decodeObject() throws Exception {
            final String input = "test+decoding";
            final String expected = "test decoding";
            assertEquals(expected, codec.decode((Object) input));
        }

        @Test
        @DisplayName("Should decode a null object as null")
        void decodeNullObject() throws Exception {
            assertNull(codec.decode((Object) null));
        }
    }
    
    @Nested
    @DisplayName("Static Method Tests")
    class StaticMethodTests {

        @Test
        @DisplayName("encodeUrl should use custom safe-character set")
        void encodeUrlWithCustomSafeSet() {
            // This BitSet marks only the null byte (0) as safe.
            final BitSet safeChars = new BitSet();
            safeChars.set(0);

            final byte[] data = {'A', '\0', 'B'}; // 'A' = 65, 'B' = 66
            final byte[] encoded = URLCodec.encodeUrl(safeChars, data);

            // 'A' and 'B' are not in the safe set, so they should be encoded.
            // '\0' is in the safe set, so it should pass through.
            assertArrayEquals(new byte[]{'%', '4', '1', '\0', '%', '4', '2'}, encoded);
        }

        @Test
        @DisplayName("encodeUrl with null safe-set should encode all bytes")
        void encodeUrlWithNullSafeSetEncodesAll() {
            final byte[] data = {'a', 'b', 'c'};
            final byte[] encoded = URLCodec.encodeUrl(null, data);
            assertArrayEquals(new byte[]{'%', '6', '1', '%', '6', '2', '%', '6', '3'}, encoded);
        }

        @Test
        @DisplayName("decodeUrl should decode a percent-encoded byte array")
        void decodeUrl() throws DecoderException {
            final byte[] input = "a+b%20c".getBytes(StandardCharsets.US_ASCII);
            final byte[] decoded = URLCodec.decodeUrl(input);
            assertArrayEquals("a b c".getBytes(StandardCharsets.US_ASCII), decoded);
        }
    }

    @Nested
    @DisplayName("Round-Trip Tests")
    class RoundTripTests {

        @Test
        @DisplayName("String should remain the same after encoding and decoding")
        void stringRoundTrip() throws Exception {
            final URLCodec codec = new URLCodec();
            final String original = "This is a test string with special characters: !@#$%^&*()_+";
            
            final String encoded = codec.encode(original);
            final String decoded = codec.decode(encoded);
            
            assertEquals(original, decoded);
        }

        @Test
        @DisplayName("Byte array should remain the same after encoding and decoding")
        void byteArrayRoundTrip() throws Exception {
            final URLCodec codec = new URLCodec();
            final byte[] original = {0, 1, 2, 3, -1, -10, -128, 127};

            final byte[] encoded = codec.encode(original);
            final byte[] decoded = codec.decode(encoded);

            assertArrayEquals(original, decoded);
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionTests {

        @Test
        @DisplayName("Encoding with an unsupported charset should throw EncoderException")
        void encodeWithUnsupportedEncoding() {
            final URLCodec codec = new URLCodec("INVALID-CHARSET");
            EncoderException e = assertThrows(EncoderException.class, () -> codec.encode("test"));
            assertInstanceOf(UnsupportedEncodingException.class, e.getCause());
        }

        @Test
        @DisplayName("Decoding with an unsupported charset should throw DecoderException")
        void decodeWithUnsupportedEncoding() {
            final URLCodec codec = new URLCodec("INVALID-CHARSET");
            DecoderException e = assertThrows(DecoderException.class, () -> codec.decode("test"));
            assertInstanceOf(UnsupportedEncodingException.class, e.getCause());
        }

        @Test
        @DisplayName("Decoding a string with an invalid hex character should throw DecoderException")
        void decodeInvalidHexCharacter() {
            final URLCodec codec = new URLCodec();
            final String input = "%G0"; // 'G' is not a valid hex character
            DecoderException e = assertThrows(DecoderException.class, () -> codec.decode(input));
            assertTrue(e.getMessage().contains("not a valid digit"));
        }

        @Test
        @DisplayName("Decoding a string with an incomplete escape sequence should throw DecoderException")
        void decodeIncompleteEscapeSequence() {
            final URLCodec codec = new URLCodec();
            assertThrows(DecoderException.class, () -> codec.decode("%"), "Should fail for single '%'");
            assertThrows(DecoderException.class, () -> codec.decode("%A"), "Should fail for single char after '%'");
        }
        
        @Test
        @DisplayName("Decoding a byte array with an invalid escape sequence should throw DecoderException")
        void decodeInvalidByteArray() {
            final byte[] input = {'%', 'X', 'Y'};
            DecoderException e = assertThrows(DecoderException.class, () -> URLCodec.decodeUrl(input));
            assertTrue(e.getMessage().contains("not a valid digit"));
        }

        @Test
        @DisplayName("Encoding an unsupported object type should throw EncoderException")
        void encodeInvalidObjectType() {
            final URLCodec codec = new URLCodec();
            EncoderException e = assertThrows(EncoderException.class, () -> codec.encode(new Object()));
            assertEquals("Objects of type java.lang.Object cannot be URL encoded", e.getMessage());
        }

        @Test
        @DisplayName("Decoding an unsupported object type should throw DecoderException")
        void decodeInvalidObjectType() {
            final URLCodec codec = new URLCodec();
            DecoderException e = assertThrows(DecoderException.class, () -> codec.decode(new Object()));
            assertEquals("Objects of type java.lang.Object cannot be URL decoded", e.getMessage());
        }
        
        @Test
        @DisplayName("Encoding with a null charset provided in constructor should throw NullPointerException")
        void encodeWithNullCharset() {
            final URLCodec codec = new URLCodec(null);
            assertThrows(NullPointerException.class, () -> codec.encode("test"));
        }

        @Test
        @DisplayName("Decoding with a null charset provided in constructor should throw NullPointerException")
        void decodeWithNullCharset() {
            final URLCodec codec = new URLCodec(null);
            assertThrows(NullPointerException.class, () -> codec.decode("test"));
        }
    }
}