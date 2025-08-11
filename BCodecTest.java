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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test cases for BCodec - a Base64 encoding implementation for RFC 1522 encoded-word headers.
 * 
 * BCodec encodes/decodes strings using Base64 encoding within the RFC 1522 format:
 * =?charset?B?base64-encoded-data?=
 */
@DisplayName("BCodec Tests")
class BCodecTest {
    
    // Test data constants
    private static final String PLAIN_TEXT = "Hello there";
    private static final String ENCODED_HELLO = "=?UTF-8?B?SGVsbG8gdGhlcmU=?=";
    private static final String PLAIN_WHAT_NOT = "what not";
    private static final String ENCODED_WHAT_NOT = "=?UTF-8?B?d2hhdCBub3Q=?=";
    
    // Invalid Base64 samples that should fail in strict mode
    private static final String[] INVALID_BASE64_ENCODED_WORDS = {
            "=?ASCII?B?ZE==?=",      // Invalid padding
            "=?ASCII?B?ZmC=?=",      // Invalid character '`'
            "=?ASCII?B?Zm9vYE==?=",  // Invalid character '`' in middle
            "=?ASCII?B?Zm9vYmC=?=",  // Invalid character '`' at end
            "=?ASCII?B?AB==?="       // Incomplete data
    };

    // Unicode test data for internationalization testing
    private static final int[] SWISS_GERMAN_UNICODE_CODEPOINTS = 
        { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 }; // "Grüezi_zämä"
    
    private static final int[] RUSSIAN_UNICODE_CODEPOINTS = 
        { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 }; // "Всем_привет"

    @Nested
    @DisplayName("Basic Encoding and Decoding")
    class BasicEncodingDecoding {
        
        @Test
        @DisplayName("Should encode plain text to RFC 1522 Base64 format")
        void shouldEncodePlainTextToBase64() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            String encoded = codec.encode(PLAIN_TEXT);
            
            // Then
            assertEquals(ENCODED_HELLO, encoded);
        }
        
        @Test
        @DisplayName("Should decode RFC 1522 Base64 format back to plain text")
        void shouldDecodeBase64ToPlainText() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            String decoded = codec.decode(ENCODED_HELLO);
            
            // Then
            assertEquals(PLAIN_TEXT, decoded);
        }
        
        @Test
        @DisplayName("Should handle round-trip encoding and decoding")
        void shouldHandleRoundTripEncodingDecoding() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            String encoded = codec.encode(PLAIN_TEXT);
            String decoded = codec.decode(encoded);
            
            // Then
            assertEquals(PLAIN_TEXT, decoded);
        }
    }
    
    @Nested
    @DisplayName("Null Handling")
    class NullHandling {
        
        @Test
        @DisplayName("Should return null when encoding null string")
        void shouldReturnNullWhenEncodingNullString() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When & Then
            assertNull(codec.encode((String) null));
        }
        
        @Test
        @DisplayName("Should return null when decoding null string")
        void shouldReturnNullWhenDecodingNullString() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When & Then
            assertNull(codec.decode((String) null));
        }
        
        @Test
        @DisplayName("Should return null when encoding null string with charset")
        void shouldReturnNullWhenEncodingNullStringWithCharset() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When & Then
            assertNull(codec.encode(null, "UTF-8"));
        }
        
        @Test
        @DisplayName("Should return null for internal encoding/decoding methods with null input")
        void shouldReturnNullForInternalMethodsWithNullInput() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When & Then
            assertNull(codec.doEncoding(null));
            assertNull(codec.doDecoding(null));
        }
    }
    
    @Nested
    @DisplayName("Object Encoding and Decoding")
    class ObjectEncodingDecoding {
        
        @Test
        @DisplayName("Should encode String object")
        void shouldEncodeStringObject() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            String encoded = (String) codec.encode((Object) PLAIN_WHAT_NOT);
            
            // Then
            assertEquals(ENCODED_WHAT_NOT, encoded);
        }
        
        @Test
        @DisplayName("Should decode String object")
        void shouldDecodeStringObject() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            String decoded = (String) codec.decode((Object) ENCODED_WHAT_NOT);
            
            // Then
            assertEquals(PLAIN_WHAT_NOT, decoded);
        }
        
        @Test
        @DisplayName("Should return null when encoding null object")
        void shouldReturnNullWhenEncodingNullObject() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            Object result = codec.encode((Object) null);
            
            // Then
            assertNull(result);
        }
        
        @Test
        @DisplayName("Should return null when decoding null object")
        void shouldReturnNullWhenDecodingNullObject() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // When
            Object result = codec.decode((Object) null);
            
            // Then
            assertNull(result);
        }
        
        @Test
        @DisplayName("Should throw exception when encoding non-String object")
        void shouldThrowExceptionWhenEncodingNonStringObject() {
            // Given
            BCodec codec = new BCodec();
            Double nonStringObject = 3.0d;
            
            // When & Then
            assertThrows(EncoderException.class, 
                () -> codec.encode(nonStringObject),
                "Encoding non-String objects should throw EncoderException");
        }
        
        @Test
        @DisplayName("Should throw exception when decoding non-String object")
        void shouldThrowExceptionWhenDecodingNonStringObject() {
            // Given
            BCodec codec = new BCodec();
            Double nonStringObject = 3.0d;
            
            // When & Then
            assertThrows(DecoderException.class, 
                () -> codec.decode(nonStringObject),
                "Decoding non-String objects should throw DecoderException");
        }
    }
    
    @Nested
    @DisplayName("Decoding Policy Tests")
    class DecodingPolicyTests {
        
        @Test
        @DisplayName("Should use lenient decoding by default")
        void shouldUseLenientDecodingByDefault() throws Exception {
            // Given
            BCodec codec = new BCodec();
            
            // Then
            assertFalse(codec.isStrictDecoding());
            
            // And should decode invalid Base64 without throwing exception
            for (String invalidEncoded : INVALID_BASE64_ENCODED_WORDS) {
                codec.decode(invalidEncoded); // Should not throw
            }
        }
        
        @Test
        @DisplayName("Should use lenient decoding when explicitly set")
        void shouldUseLenientDecodingWhenExplicitlySet() throws Exception {
            // Given
            BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);
            
            // Then
            assertFalse(codec.isStrictDecoding());
            
            // And should decode invalid Base64 without throwing exception
            for (String invalidEncoded : INVALID_BASE64_ENCODED_WORDS) {
                codec.decode(invalidEncoded); // Should not throw
            }
        }
        
        @Test
        @DisplayName("Should throw exception for invalid Base64 in strict mode")
        void shouldThrowExceptionForInvalidBase64InStrictMode() {
            // Given
            BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
            
            // Then
            assertTrue(codec.isStrictDecoding());
            
            // And should throw exception for each invalid Base64 sample
            for (String invalidEncoded : INVALID_BASE64_ENCODED_WORDS) {
                assertThrows(DecoderException.class, 
                    () -> codec.decode(invalidEncoded),
                    "Should throw DecoderException for: " + invalidEncoded);
            }
        }
    }
    
    @Nested
    @DisplayName("Internationalization Tests")
    class InternationalizationTests {
        
        @Test
        @DisplayName("Should handle UTF-8 encoding for Russian text")
        void shouldHandleUtf8EncodingForRussianText() throws Exception {
            // Given
            String russianText = createStringFromCodepoints(RUSSIAN_UNICODE_CODEPOINTS);
            BCodec codec = new BCodec(CharEncoding.UTF_8);
            String expectedEncoded = "=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=";
            
            // When
            String encoded = codec.encode(russianText);
            
            // Then
            assertEquals(expectedEncoded, encoded);
            assertEquals(russianText, codec.decode(encoded));
        }
        
        @Test
        @DisplayName("Should handle UTF-8 encoding for Swiss German text")
        void shouldHandleUtf8EncodingForSwissGermanText() throws Exception {
            // Given
            String swissGermanText = createStringFromCodepoints(SWISS_GERMAN_UNICODE_CODEPOINTS);
            BCodec codec = new BCodec(CharEncoding.UTF_8);
            String expectedEncoded = "=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=";
            
            // When
            String encoded = codec.encode(swissGermanText);
            
            // Then
            assertEquals(expectedEncoded, encoded);
            assertEquals(swissGermanText, codec.decode(encoded));
        }
    }
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should throw exception for invalid charset name")
        void shouldThrowExceptionForInvalidCharsetName() {
            // When & Then
            assertThrows(UnsupportedCharsetException.class, 
                () -> new BCodec("INVALID_CHARSET_NAME"),
                "Should throw UnsupportedCharsetException for invalid charset");
        }
    }
    
    /**
     * Helper method to create a string from Unicode codepoints.
     * This makes the test data more readable and maintainable.
     */
    private String createStringFromCodepoints(int[] codepoints) {
        StringBuilder builder = new StringBuilder();
        if (codepoints != null) {
            for (int codepoint : codepoints) {
                builder.append((char) codepoint);
            }
        }
        return builder.toString();
    }
}