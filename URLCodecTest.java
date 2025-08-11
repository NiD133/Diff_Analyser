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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link URLCodec}.
 */
class URLCodecTest {

    private static final String PLAIN_TEXT = "Hello there!";
    private static final String ENCODED_TEXT = "Hello+there%21";

    private final URLCodec urlCodec = new URLCodec();

    @DisplayName("Encoding and decoding should be symmetrical for various inputs")
    @ParameterizedTest
    @CsvSource({
            // Basic case with space and symbol
            "'Hello there!', 'Hello+there%21'",
            // Safe characters should not be encoded
            "'abc123_-.*', 'abc123_-.*'",
            // Unsafe characters should be percent-encoded
            "'~!@#$%^&()+{}\"\\;:`,/[]', '%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D'"
    })
    void shouldEncodeAndDecodeSymmetrically(final String plain, final String encoded) throws Exception {
        assertEquals(encoded, urlCodec.encode(plain));
        assertEquals(plain, urlCodec.decode(encoded));
    }

    @Nested
    @DisplayName("Charset Handling")
    class CharsetHandlingTest {

        @Test
        @DisplayName("Should use constructor-provided charset as default for encoding")
        void shouldUseConstructorCharsetAsDefault() throws Exception {
            final URLCodec customCodec = new URLCodec(StandardCharsets.UTF_16BE.name());
            final String encodedWithDefault = customCodec.encode(PLAIN_TEXT);
            final String encodedWithExplicit = customCodec.encode(PLAIN_TEXT, StandardCharsets.UTF_16BE.name());
            assertEquals(encodedWithExplicit, encodedWithDefault);
        }

        @Test
        @DisplayName("Should perform a round-trip for multi-byte characters using UTF-8")
        void shouldRoundTripMultiByteStrings() throws Exception {
            final String swissGermanGreeting = "Grüezi_zämä";
            final String russianGreeting = "Всем_привет";

            // Test with Swiss-German
            String encoded = urlCodec.encode(swissGermanGreeting, StandardCharsets.UTF_8.name());
            assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4", encoded);
            assertEquals(swissGermanGreeting, urlCodec.decode(encoded, StandardCharsets.UTF_8.name()));

            // Test with Russian
            encoded = urlCodec.encode(russianGreeting, StandardCharsets.UTF_8.name());
            assertEquals("%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", encoded);
            assertEquals(russianGreeting, urlCodec.decode(encoded, StandardCharsets.UTF_8.name()));
        }

        @Test
        @DisplayName("Should throw exception when using an unsupported charset")
        void shouldUseUnsupportedCharset() {
            final URLCodec badCodec = new URLCodec("NONSENSE_CHARSET");
            assertThrows(EncoderException.class, () -> badCodec.encode(PLAIN_TEXT),
                    "Encoding with a bogus charset should fail.");
            assertThrows(DecoderException.class, () -> badCodec.decode(ENCODED_TEXT),
                    "Decoding with a bogus charset should fail.");
        }
    }

    @Nested
    @DisplayName("Invalid Input Handling")
    class InvalidInputTest {

        @DisplayName("Decoding an invalid percent-encoded sequence should throw DecoderException")
        @ParameterizedTest
        @ValueSource(strings = {"%", "%A", "%WW", "%0W"})
        void shouldThrowOnDecodeOfInvalidPercentSequence(final String invalidInput) {
            assertThrows(DecoderException.class, () -> urlCodec.decode(invalidInput));
        }

        @Test
        @DisplayName("Decoding byte array without escape characters should return the original array")
        void shouldReturnOriginalBytesWhenNoEscapeCharIsPresent() throws DecoderException {
            // This test verifies that if the input byte array does not contain the
            // escape character '%', the decoder returns the original byte array as is.
            final String swissGermanGreeting = "Grüezi_zämä";
            final byte[] originalBytes = swissGermanGreeting.getBytes(StandardCharsets.ISO_8859_1);

            final byte[] decodedBytes = urlCodec.decode(originalBytes);

            assertArrayEquals(originalBytes, decodedBytes);
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputTest {

        @Test
        @DisplayName("Encoding a null String should return null")
        void shouldReturnNullWhenEncodingNullString() throws Exception {
            assertNull(urlCodec.encode((String) null));
            assertNull(urlCodec.encode(null, StandardCharsets.UTF_8.name()));
        }

        @Test
        @DisplayName("Decoding a null String should return null")
        void shouldReturnNullWhenDecodingNullString() throws Exception {
            assertNull(urlCodec.decode((String) null));
            assertNull(urlCodec.decode(null, StandardCharsets.UTF_8.name()));
        }

        @Test
        @DisplayName("Encoding a null byte array should return null")
        void shouldReturnNullWhenEncodingNullByteArray() {
            assertNull(urlCodec.encode((byte[]) null));
        }

        @Test
        @DisplayName("Decoding a null byte array should return null")
        void shouldReturnNullWhenDecodingNullByteArray() throws DecoderException {
            assertNull(URLCodec.decodeUrl(null));
        }

        @Test
        @DisplayName("Encoding or decoding a null Object should return null")
        void shouldReturnNullWhenHandlingNullObject() throws Exception {
            assertNull(urlCodec.encode((Object) null));
            assertNull(urlCodec.decode((Object) null));
        }
    }

    @Nested
    @DisplayName("Object and byte[] Type Handling")
    class TypeHandlingTest {

        @Test
        @DisplayName("Should encode and decode a String when passed as an Object")
        void shouldHandleStringAsObject() throws Exception {
            final Object encoded = urlCodec.encode((Object) PLAIN_TEXT);
            assertEquals(ENCODED_TEXT, encoded);

            final Object decoded = urlCodec.decode(encoded);
            assertEquals(PLAIN_TEXT, decoded);
        }

        @Test
        @DisplayName("Should encode and decode a byte array when passed as an Object")
        void shouldHandleByteArrayAsObject() throws Exception {
            final byte[] plainBytes = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);
            final byte[] expectedEncodedBytes = ENCODED_TEXT.getBytes(StandardCharsets.US_ASCII);

            final Object encodedResult = urlCodec.encode((Object) plainBytes);
            assertArrayEquals(expectedEncodedBytes, (byte[]) encodedResult);

            final Object decodedResult = urlCodec.decode(encodedResult);
            assertArrayEquals(plainBytes, (byte[]) decodedResult);
        }

        @Test
        @DisplayName("Should throw exception when encoding or decoding an unsupported Object type")
        void shouldThrowOnUnsupportedObjectType() {
            final Double unsupportedObject = 3.0d;
            assertThrows(EncoderException.class, () -> urlCodec.encode(unsupportedObject));
            assertThrows(DecoderException.class, () -> urlCodec.decode(unsupportedObject));
        }
    }

    @Nested
    @DisplayName("Static Method Tests")
    class StaticMethodTest {

        @Test
        @DisplayName("encodeUrl with a null BitSet should use default safe characters")
        void shouldUseDefaultSafeCharsWhenBitSetIsNull() throws Exception {
            final byte[] plainBytes = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);
            final byte[] encodedBytes = URLCodec.encodeUrl(null, plainBytes);

            assertEquals(ENCODED_TEXT, new String(encodedBytes, StandardCharsets.US_ASCII));

            // Verify round-trip
            final byte[] decodedBytes = URLCodec.decodeUrl(encodedBytes);
            assertArrayEquals(plainBytes, decodedBytes);
        }
    }
}