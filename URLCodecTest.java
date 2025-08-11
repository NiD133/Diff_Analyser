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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Test suite for URLCodec - validates URL encoding/decoding functionality
 * including basic operations, edge cases, error handling, and internationalization.
 */
class URLCodecTest {

    // Test data: "Grüezi_zämä" in Swiss German
    private static final int[] SWISS_GERMAN_UNICODE_CHARS = { 
        0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 
    };

    // Test data: "Всем_привет" (Hello everyone) in Russian
    private static final int[] RUSSIAN_UNICODE_CHARS = { 
        0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 
    };

    // Test strings for common scenarios
    private static final String SIMPLE_TEXT = "Hello there!";
    private static final String SIMPLE_TEXT_ENCODED = "Hello+there%21";
    private static final String SAFE_CHARACTERS = "abc123_-.*";
    private static final String UNSAFE_CHARACTERS = "~!@#$%^&()+{}\"\\;:`,/[]";
    private static final String UNSAFE_CHARACTERS_ENCODED = "%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D";

    /**
     * Helper method to construct strings from Unicode code points
     */
    private String buildStringFromUnicodeChars(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    // ========== Basic Functionality Tests ==========

    @Test
    void shouldEncodeAndDecodeSimpleText() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When
        final String encoded = codec.encode(SIMPLE_TEXT);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(SIMPLE_TEXT_ENCODED, encoded, "Should encode 'Hello there!' correctly");
        assertEquals(SIMPLE_TEXT, decoded, "Should decode back to original text");
        
        validateCodecState(codec);
    }

    @Test
    void shouldHandleSafeCharactersWithoutEncoding() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When
        final String encoded = codec.encode(SAFE_CHARACTERS);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(SAFE_CHARACTERS, encoded, "Safe characters should not be encoded");
        assertEquals(SAFE_CHARACTERS, decoded, "Safe characters should decode unchanged");
        
        validateCodecState(codec);
    }

    @Test
    void shouldEncodeUnsafeCharactersCorrectly() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When
        final String encoded = codec.encode(UNSAFE_CHARACTERS);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(UNSAFE_CHARACTERS_ENCODED, encoded, "All unsafe characters should be percent-encoded");
        assertEquals(UNSAFE_CHARACTERS, decoded, "Should decode back to original unsafe characters");
        
        validateCodecState(codec);
    }

    // ========== Null Handling Tests ==========

    @Test
    void shouldHandleNullInputsGracefully() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When & Then
        assertNull(codec.encode((String) null), "Encoding null string should return null");
        assertNull(codec.decode((String) null), "Decoding null string should return null");
        assertNull(codec.encode((byte[]) null), "Encoding null byte array should return null");
        assertNull(URLCodec.decodeUrl(null), "Static decode with null should return null");
        
        validateCodecState(codec);
    }

    @Test
    void shouldHandleNullStringWithCharset() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When & Then
        assertNull(codec.encode(null, "UTF-8"), "Encoding null string with charset should return null");
        assertNull(codec.decode(null, "UTF-8"), "Decoding null string with charset should return null");
    }

    // ========== Object Type Handling Tests ==========

    @Test
    void shouldEncodeStringAndByteArrayObjects() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        final byte[] inputBytes = SIMPLE_TEXT.getBytes(StandardCharsets.UTF_8);
        
        // When - encode String object
        final String encodedString = (String) codec.encode((Object) SIMPLE_TEXT);
        
        // When - encode byte array object
        final byte[] encodedBytes = (byte[]) codec.encode((Object) inputBytes);
        final String encodedBytesAsString = new String(encodedBytes);
        
        // Then
        assertEquals(SIMPLE_TEXT_ENCODED, encodedString, "Should encode String object correctly");
        assertEquals(SIMPLE_TEXT_ENCODED, encodedBytesAsString, "Should encode byte array object correctly");
        assertNull(codec.encode((Object) null), "Encoding null object should return null");
        
        validateCodecState(codec);
    }

    @Test
    void shouldDecodeStringAndByteArrayObjects() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        final byte[] encodedBytes = SIMPLE_TEXT_ENCODED.getBytes(StandardCharsets.UTF_8);
        
        // When - decode String object
        final String decodedString = (String) codec.decode((Object) SIMPLE_TEXT_ENCODED);
        
        // When - decode byte array object
        final byte[] decodedBytes = (byte[]) codec.decode((Object) encodedBytes);
        final String decodedBytesAsString = new String(decodedBytes);
        
        // Then
        assertEquals(SIMPLE_TEXT, decodedString, "Should decode String object correctly");
        assertEquals(SIMPLE_TEXT, decodedBytesAsString, "Should decode byte array object correctly");
        assertNull(codec.decode((Object) null), "Decoding null object should return null");
        
        validateCodecState(codec);
    }

    @Test
    void shouldRejectUnsupportedObjectTypes() {
        // Given
        final URLCodec codec = new URLCodec();
        final Double unsupportedObject = Double.valueOf(3.0d);
        
        // When & Then
        assertThrows(EncoderException.class, 
            () -> codec.encode(unsupportedObject), 
            "Should throw EncoderException for unsupported object type");
            
        assertThrows(DecoderException.class, 
            () -> codec.decode(unsupportedObject), 
            "Should throw DecoderException for unsupported object type");
            
        validateCodecState(codec);
    }

    // ========== Error Handling Tests ==========

    @Test
    void shouldRejectMalformedPercentEncodedStrings() {
        // Given
        final URLCodec codec = new URLCodec();
        
        // When & Then - test various malformed inputs
        assertThrows(DecoderException.class, () -> codec.decode("%"), 
            "Should reject incomplete percent encoding");
        assertThrows(DecoderException.class, () -> codec.decode("%A"), 
            "Should reject incomplete percent encoding with one hex digit");
        assertThrows(DecoderException.class, () -> codec.decode("%WW"), 
            "Should reject invalid hex characters in percent encoding");
        assertThrows(DecoderException.class, () -> codec.decode("%0W"), 
            "Should reject invalid second hex character in percent encoding");
            
        validateCodecState(codec);
    }

    @Test
    void shouldRejectInvalidCharsetEncoding() {
        // Given
        final URLCodec codecWithInvalidCharset = new URLCodec("INVALID_CHARSET");
        
        // When & Then
        assertThrows(EncoderException.class, 
            () -> codecWithInvalidCharset.encode(SIMPLE_TEXT), 
            "Should throw EncoderException for invalid charset");
            
        assertThrows(DecoderException.class, 
            () -> codecWithInvalidCharset.decode(SIMPLE_TEXT), 
            "Should throw DecoderException for invalid charset");
            
        validateCodecState(codecWithInvalidCharset);
    }

    // ========== Charset and Encoding Tests ==========

    @Test
    void shouldUseDefaultCharsetConsistently() throws Exception {
        // Given
        final String customCharset = "UnicodeBig";
        final URLCodec codec = new URLCodec(customCharset);
        
        // When
        codec.encode(SIMPLE_TEXT); // Workaround for Java 1.2.2 quirk
        final String encodedWithExplicitCharset = codec.encode(SIMPLE_TEXT, customCharset);
        final String encodedWithDefaultCharset = codec.encode(SIMPLE_TEXT);
        
        // Then
        assertEquals(encodedWithExplicitCharset, encodedWithDefaultCharset, 
            "Default charset should match explicitly specified charset");
            
        validateCodecState(codec);
    }

    @Test
    void shouldHandleUTF8InternationalCharacters() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        final String russianText = buildStringFromUnicodeChars(RUSSIAN_UNICODE_CHARS);
        final String swissGermanText = buildStringFromUnicodeChars(SWISS_GERMAN_UNICODE_CHARS);
        
        // When & Then - test Russian text
        final String encodedRussian = codec.encode(russianText, CharEncoding.UTF_8);
        assertEquals("%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", 
            encodedRussian, "Should encode Russian text correctly in UTF-8");
        assertEquals(russianText, 
            codec.decode(encodedRussian, CharEncoding.UTF_8), 
            "Should round-trip Russian text correctly");
        
        // When & Then - test Swiss German text
        final String encodedSwissGerman = codec.encode(swissGermanText, CharEncoding.UTF_8);
        assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4", 
            encodedSwissGerman, "Should encode Swiss German text correctly in UTF-8");
        assertEquals(swissGermanText, 
            codec.decode(encodedSwissGerman, CharEncoding.UTF_8), 
            "Should round-trip Swiss German text correctly");
            
        validateCodecState(codec);
    }

    @Test
    void shouldHandleISO88591Characters() throws DecoderException {
        // Given
        final URLCodec codec = new URLCodec();
        final String swissGermanText = buildStringFromUnicodeChars(SWISS_GERMAN_UNICODE_CHARS);
        final byte[] inputBytes = swissGermanText.getBytes(StandardCharsets.ISO_8859_1);
        
        // When
        final byte[] outputBytes = codec.decode(inputBytes);
        
        // Then
        assertEquals(inputBytes.length, outputBytes.length, "Output should have same length as input");
        for (int i = 0; i < inputBytes.length; i++) {
            assertEquals(inputBytes[i], outputBytes[i], 
                "Byte at position " + i + " should be unchanged");
        }
        
        validateCodecState(codec);
    }

    // ========== Static Method Tests ==========

    @Test
    void shouldEncodeWithNullBitSet() throws Exception {
        // Given
        final URLCodec codec = new URLCodec();
        final byte[] inputBytes = SIMPLE_TEXT.getBytes(StandardCharsets.UTF_8);
        
        // When
        final String encoded = new String(URLCodec.encodeUrl(null, inputBytes));
        
        // Then
        assertEquals(SIMPLE_TEXT_ENCODED, encoded, "Should encode correctly with null BitSet");
        assertEquals(SIMPLE_TEXT, codec.decode(encoded), "Should decode back to original");
        
        validateCodecState(codec);
    }

    /**
     * Validates that the codec is in a consistent state after operations.
     * Currently a placeholder for future state validation logic.
     */
    private void validateCodecState(final URLCodec codec) {
        // Placeholder for codec state validation
        // Could be extended to verify internal state consistency
    }
}