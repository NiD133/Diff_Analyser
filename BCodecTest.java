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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BCodec}
 */
class BCodecTest {
    // Test strings represented as Unicode code points
    private static final int[] SWISS_GERMAN_UNICODE_CHARS = 
        { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_UNICODE_CHARS = 
        { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    // Invalid base64 strings that should fail in strict mode
    private static final String[] INVALID_BASE64_STRINGS = {
        // These require RFC 1522 "encoded-word" header and have invalid padding
        "=?ASCII?B?ZE==?=",     // Invalid padding
        "=?ASCII?B?ZmC=?=",      // Invalid character
        "=?ASCII?B?Zm9vYE==?=",  // Invalid padding
        "=?ASCII?B?Zm9vYmC=?=",  // Invalid character
        "=?ASCII?B?AB==?="       // Invalid padding
    };

    /**
     * Creates string from Unicode code points
     */
    private String buildStringFromCodePoints(int[] codePoints) {
        if (codePoints == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        for (int codePoint : codePoints) {
            buffer.append((char) codePoint);
        }
        return buffer.toString();
    }

    @Test
    void defaultPolicy_ShouldDecodeInvalidBase64Strings() throws DecoderException {
        BCodec codec = new BCodec();
        assertFalse(codec.isStrictDecoding(), "Default policy should be lenient");
        
        for (String invalidString : INVALID_BASE64_STRINGS) {
            // Should not throw with default lenient policy
            codec.decode(invalidString);
        }
    }

    @Test
    void lenientPolicy_ShouldDecodeInvalidBase64Strings() throws DecoderException {
        BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);
        assertFalse(codec.isStrictDecoding(), "Explicit lenient policy");
        
        for (String invalidString : INVALID_BASE64_STRINGS) {
            // Should not throw in lenient mode
            codec.decode(invalidString);
        }
    }

    @Test
    void strictPolicy_ShouldThrowForInvalidBase64Strings() {
        BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(codec.isStrictDecoding(), "Should have strict decoding");
        
        for (String invalidString : INVALID_BASE64_STRINGS) {
            assertThrows(DecoderException.class, 
                () -> codec.decode(invalidString),
                "Strict policy should reject invalid string: " + invalidString
            );
        }
    }

    @Test
    void encodeThenDecode_ShouldReturnOriginalString() throws Exception {
        BCodec codec = new BCodec();
        String original = "Hello there";
        
        String encoded = codec.encode(original);
        String decoded = codec.decode(encoded);
        
        assertEquals("=?UTF-8?B?SGVsbG8gdGhlcmU=?=", encoded, "Encoded value");
        assertEquals(original, decoded, "Decoded value should match original");
    }

    @Test
    void decodeObject_WithValidString_ReturnsDecodedString() throws Exception {
        BCodec codec = new BCodec();
        Object result = codec.decode((Object) "=?UTF-8?B?d2hhdCBub3Q=?=");
        assertEquals("what not", result);
    }

    @Test
    void decodeObject_WithNull_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.decode((Object) null));
    }

    @Test
    void decodeObject_WithInvalidType_ThrowsDecoderException() {
        BCodec codec = new BCodec();
        assertThrows(DecoderException.class, 
            () -> codec.decode(Double.valueOf(3.0d)),
            "Should only decode String objects"
        );
    }

    @Test
    void decodeString_WithNull_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.decode((String) null));
    }

    @Test
    void encodeString_WithNull_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode((String) null));
    }

    @Test
    void decodeString_WithNull_AfterEncode_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.decode((String) null));
    }

    @Test
    void encodeObject_WithValidString_ReturnsEncodedString() throws Exception {
        BCodec codec = new BCodec();
        Object result = codec.encode((Object) "what not");
        assertEquals("=?UTF-8?B?d2hhdCBub3Q=?=", result);
    }

    @Test
    void encodeObject_WithNull_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode((Object) null));
    }

    @Test
    void encodeObject_WithInvalidType_ThrowsEncoderException() {
        BCodec codec = new BCodec();
        assertThrows(EncoderException.class,
            () -> codec.encode(Double.valueOf(3.0d)),
            "Should only encode String objects"
        );
    }

    @Test
    void encodeStringWithCharset_WithNull_ReturnsNull() throws Exception {
        BCodec codec = new BCodec();
        assertNull(codec.encode(null, "charset"));
    }

    @Test
    void constructor_WithInvalidCharset_ThrowsException() {
        assertThrows(UnsupportedCharsetException.class, 
            () -> new BCodec("NONSENSE"),
            "Should reject invalid charset names"
        );
    }

    @Test
    void doDecoding_WithNull_ReturnsNull() {
        BCodec codec = new BCodec();
        assertNull(codec.doDecoding(null));
    }

    @Test
    void doEncoding_WithNull_ReturnsNull() {
        BCodec codec = new BCodec();
        assertNull(codec.doEncoding(null));
    }

    @Test
    void encodeAndDecode_NonAsciiText_RoundTripSuccessful() throws Exception {
        BCodec codec = new BCodec(CharEncoding.UTF_8);
        
        String russianText = buildStringFromCodePoints(RUSSIAN_UNICODE_CHARS);
        String swissGermanText = buildStringFromCodePoints(SWISS_GERMAN_UNICODE_CHARS);

        // Test Russian text
        String encodedRussian = codec.encode(russianText);
        assertEquals("=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=", encodedRussian);
        assertEquals(russianText, codec.decode(encodedRussian));

        // Test Swiss-German text
        String encodedSwissGerman = codec.encode(swissGermanText);
        assertEquals("=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=", encodedSwissGerman);
        assertEquals(swissGermanText, codec.decode(encodedSwissGerman));
    }
}