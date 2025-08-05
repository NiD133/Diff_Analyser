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

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link URLCodec}.
 */
class URLCodecTest {
    // Test strings
    private static final String PLAIN_TEXT = "Hello there!";
    private static final String ENCODED_TEXT = "Hello+there%21";
    private static final String SAFE_CHARS = "abc123_-.*";
    private static final String UNSAFE_CHARS = "~!@#$%^&()+{}\"\\;:`,/[]";
    private static final String UNSAFE_CHARS_ENCODED = 
        "%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D";
    
    // Character sequences
    private static final int[] SWISS_GERMAN_STUFF_UNICODE = { 
        0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 
    };
    private static final int[] RUSSIAN_STUFF_UNICODE = { 
        0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 
    };

    // Expected encoded values
    private static final String RUSSIAN_ENCODED = 
        "%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82";
    private static final String SWISS_GERMAN_ENCODED = 
        "Gr%C3%BCezi_z%C3%A4m%C3%A4";

    // Invalid encoding name
    private static final String INVALID_ENCODING = "NONSENSE";

    /**
     * Constructs a string from Unicode character codes.
     */
    private String constructString(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    /* ========== Basic Functionality Tests ========== */
    
    @Test
    void basicEncodeAndDecode_ShouldHandleAlphanumericAndSpecialCharacters() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertEquals(ENCODED_TEXT, urlCodec.encode(PLAIN_TEXT));
        assertEquals(PLAIN_TEXT, urlCodec.decode(ENCODED_TEXT));
    }

    @Test
    void encodeAndDecodeSafeCharacters_ShouldNotModifyInput() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertEquals(SAFE_CHARS, urlCodec.encode(SAFE_CHARS));
        assertEquals(SAFE_CHARS, urlCodec.decode(SAFE_CHARS));
    }

    @Test
    void encodeAndDecodeUnsafeCharacters_ShouldProperlyEncodeAndDecode() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertEquals(UNSAFE_CHARS_ENCODED, urlCodec.encode(UNSAFE_CHARS));
        assertEquals(UNSAFE_CHARS, urlCodec.decode(UNSAFE_CHARS_ENCODED));
    }

    /* ========== Null Handling Tests ========== */
    
    @Test
    void encodeNullString_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.encode((String) null));
    }

    @Test
    void decodeNullString_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.decode((String) null));
    }

    @Test
    void encodeNullByteArray_ShouldReturnNull() {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.encode((byte[]) null));
    }

    @Test
    void decodeWithNullArray_ShouldReturnNull() {
        assertNull(URLCodec.decodeUrl(null));
    }

    @Test
    void encodeStringWithNullInput_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.encode(null, "charset"));
    }

    @Test
    void decodeStringWithNullInput_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.decode(null, "charset"));
    }

    /* ========== Object Handling Tests ========== */
    
    @Test
    void encodeObjectWithString_ShouldReturnEncodedString() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        String encoded = (String) urlCodec.encode((Object) PLAIN_TEXT);
        assertEquals(ENCODED_TEXT, encoded);
    }

    @Test
    void encodeObjectWithByteArray_ShouldReturnEncodedBytes() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final byte[] plainBA = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedBA = (byte[]) urlCodec.encode((Object) plainBA);
        assertEquals(ENCODED_TEXT, new String(encodedBA));
    }

    @Test
    void encodeObjectWithNull_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.encode((Object) null));
    }

    @Test
    void encodeObjectWithInvalidType_ShouldThrowException() {
        final URLCodec urlCodec = new URLCodec();
        assertThrows(EncoderException.class, 
            () -> urlCodec.encode(Double.valueOf(3.0d)));
    }

    @Test
    void decodeObjectWithString_ShouldReturnDecodedString() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        String decoded = (String) urlCodec.decode((Object) ENCODED_TEXT);
        assertEquals(PLAIN_TEXT, decoded);
    }

    @Test
    void decodeObjectWithByteArray_ShouldReturnDecodedBytes() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        final byte[] encodedBA = ENCODED_TEXT.getBytes(StandardCharsets.UTF_8);
        final byte[] decodedBA = (byte[]) urlCodec.decode((Object) encodedBA);
        assertEquals(PLAIN_TEXT, new String(decodedBA));
    }

    @Test
    void decodeObjectWithNull_ShouldReturnNull() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        assertNull(urlCodec.decode((Object) null));
    }

    @Test
    void decodeObjectWithInvalidType_ShouldThrowException() {
        final URLCodec urlCodec = new URLCodec();
        assertThrows(DecoderException.class, 
            () -> urlCodec.decode(Double.valueOf(3.0d)));
    }

    /* ========== Edge Case Tests ========== */
    
    @Test
    void decodeInvalidPercentEncoding_ShouldThrowException() {
        final URLCodec urlCodec = new URLCodec();
        assertThrows(DecoderException.class, () -> urlCodec.decode("%"));
        assertThrows(DecoderException.class, () -> urlCodec.decode("%A"));
        assertThrows(DecoderException.class, () -> urlCodec.decode("%WW"));  // Bad 1st char
        assertThrows(DecoderException.class, () -> urlCodec.decode("%0W"));  // Bad 2nd char
    }

    @Test
    void decodeNonEncodedContent_ShouldReturnOriginalBytes() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        String swissGerman = constructString(SWISS_GERMAN_STUFF_UNICODE);
        byte[] input = swissGerman.getBytes(StandardCharsets.ISO_8859_1);
        byte[] output = urlCodec.decode(input);
        assertArrayEquals(input, output);
    }

    @Test
    void encodeUrlWithNullBitSet_ShouldUseDefaultSafeSet() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        byte[] encoded = URLCodec.encodeUrl(null, PLAIN_TEXT.getBytes(StandardCharsets.UTF_8));
        assertEquals(ENCODED_TEXT, new String(encoded));
        assertEquals(PLAIN_TEXT, urlCodec.decode(ENCODED_TEXT));
    }

    /* ========== Encoding Configuration Tests ========== */
    
    @Test
    void defaultEncoding_ShouldUseSpecifiedCharset() throws Exception {
        final URLCodec urlCodec = new URLCodec("UnicodeBig");
        String encoded1 = urlCodec.encode(PLAIN_TEXT, "UnicodeBig");
        String encoded2 = urlCodec.encode(PLAIN_TEXT);
        assertEquals(encoded1, encoded2);
    }

    @Test
    void operationsWithInvalidEncoding_ShouldThrowException() {
        final URLCodec urlCodec = new URLCodec(INVALID_ENCODING);
        assertThrows(EncoderException.class, () -> urlCodec.encode(PLAIN_TEXT));
        assertThrows(DecoderException.class, () -> urlCodec.decode(PLAIN_TEXT));
    }

    /* ========== Internationalization Tests ========== */
    
    @Test
    void utf8RoundTrip_ShouldHandleNonAsciiCharacters() throws Exception {
        final URLCodec urlCodec = new URLCodec();
        String russianText = constructString(RUSSIAN_STUFF_UNICODE);
        String swissGermanText = constructString(SWISS_GERMAN_STUFF_UNICODE);

        // Test encoding
        assertEquals(RUSSIAN_ENCODED, urlCodec.encode(russianText, CharEncoding.UTF_8));
        assertEquals(SWISS_GERMAN_ENCODED, urlCodec.encode(swissGermanText, CharEncoding.UTF_8));

        // Test round trip
        assertEquals(russianText, 
            urlCodec.decode(urlCodec.encode(russianText, CharEncoding.UTF_8), CharEncoding.UTF_8));
        assertEquals(swissGermanText, 
            urlCodec.decode(urlCodec.encode(swissGermanText, CharEncoding.UTF_8), CharEncoding.UTF_8));
    }
}