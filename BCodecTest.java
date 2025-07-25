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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BCodec} class, which implements Base64 encoding and decoding for RFC 1522.
 */
class BCodecTest {

    // Test data containing Base64 strings that are impossible to decode strictly because they lack the RFC 1522 header.
    private static final String[] BASE64_IMPOSSIBLE_CASES = {
            // Require the RFC 1522 "encoded-word" header
            "=?ASCII?B?ZE==?=",
            "=?ASCII?B?ZmC=?=",
            "=?ASCII?B?Zm9vYE==?=",
            "=?ASCII?B?Zm9vYmC=?=",
            "=?ASCII?B?AB==?="
    };

    // Unicode code points for Swiss German characters.
    private static final int[] SWISS_GERMAN_STUFF_UNICODE =
        { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };

    // Unicode code points for Russian characters.
    private static final int[] RUSSIAN_STUFF_UNICODE =
        { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    private BCodec bCodec;

    @BeforeEach
    void setUp() {
        bCodec = new BCodec();
    }

    // Helper method to construct a string from an array of Unicode code points.
    private String constructString(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    @Test
    void testBase64ImpossibleSamplesDefault() throws DecoderException {
        // Given a default BCodec (lenient decoding)

        // When decoding impossible Base64 strings
        for (final String s : BASE64_IMPOSSIBLE_CASES) {
            bCodec.decode(s);
        }

        // Then no exception is thrown because lenient decoding is used
        assertFalse(bCodec.isStrictDecoding());
    }

    @Test
    void testBase64ImpossibleSamplesLenient() throws DecoderException {
        // Given a BCodec configured with lenient decoding
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);

        // When decoding impossible Base64 strings
        for (final String s : BASE64_IMPOSSIBLE_CASES) {
            codec.decode(s);
        }

        // Then no exception is thrown because lenient decoding is used
        assertFalse(codec.isStrictDecoding());
    }

    @Test
    void testBase64ImpossibleSamplesStrict() {
        // Given a BCodec configured with strict decoding
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(codec.isStrictDecoding());

        // When decoding impossible Base64 strings
        // Then a DecoderException is thrown
        for (final String s : BASE64_IMPOSSIBLE_CASES) {
            assertThrows(DecoderException.class, () -> codec.decode(s));
        }
    }

    @Test
    void testBasicEncodeDecode() throws Exception {
        // Given a plain string
        final String plain = "Hello there";

        // When encoding and decoding the string
        final String encoded = bCodec.encode(plain);
        final String decoded = bCodec.decode(encoded);

        // Then the encoded string is as expected and the decoded string equals the original plain string
        assertEquals("=?UTF-8?B?SGVsbG8gdGhlcmU=?=", encoded, "Basic B encoding test");
        assertEquals(plain, decoded, "Basic B decoding test");
    }

    @Test
    void testDecodeObjects() throws Exception {
        // Given a BCodec and a Base64 encoded string
        final String decoded = "=?UTF-8?B?d2hhdCBub3Q=?=";

        // When decoding the encoded string as an object
        final String plain = (String) bCodec.decode((Object) decoded);

        // Then the decoded string is as expected
        assertEquals("what not", plain, "Basic B decoding test");

        // When decoding a null object
        final Object result = bCodec.decode((Object) null);

        // Then the result is null
        assertNull(result, "Decoding a null Object should return null");

        // When decoding an object of an unexpected type
        // Then a DecoderException is thrown
        assertThrows(DecoderException.class, () -> bCodec.decode(Double.valueOf(3.0d)));
    }

    @Test
    void testDecodeStringWithNull() throws Exception {
        // Given a null string
        final String test = null;

        // When decoding the null string
        final String result = bCodec.decode(test);

        // Then the result is null
        assertNull(result, "Result should be null");
    }

    @Test
    void testEncodeDecodeNull() throws Exception {
        // Given a BCodec

        // When encoding or decoding a null string
        // Then the result is null
        assertNull(bCodec.encode((String) null), "Null string B encoding test");
        assertNull(bCodec.decode((String) null), "Null string B decoding test");
    }

    @Test
    void testEncodeObjects() throws Exception {
        // Given a plain string
        final String plain = "what not";

        // When encoding the string as an object
        final String encoded = (String) bCodec.encode((Object) plain);

        // Then the encoded string is as expected
        assertEquals("=?UTF-8?B?d2hhdCBub3Q=?=", encoded, "Basic B encoding test");

        // When encoding a null object
        final Object result = bCodec.encode((Object) null);

        // Then the result is null
        assertNull(result, "Encoding a null Object should return null");

        // When encoding an object of an unexpected type
        // Then an EncoderException is thrown
        assertThrows(EncoderException.class, () -> bCodec.encode(Double.valueOf(3.0d)),
            "Trying to url encode a Double object should cause an exception.");
    }

    @Test
    void testEncodeStringWithNull() throws Exception {
        // Given a null string

        final String test = null;

        // When encoding the null string with a charset
        final String result = bCodec.encode(test, "charset");

        // Then the result is null
        assertNull(result, "Result should be null");
    }

    @Test
    void testInvalidEncoding() {
        // Given an invalid charset name

        // When creating a BCodec with the invalid charset name
        // Then an UnsupportedCharsetException is thrown
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec("NONSENSE"));
    }

    @Test
    void testNullInput() throws Exception {
        // Given a BCodec

        // When calling doDecoding or doEncoding with a null input
        // Then the result is null
        assertNull(bCodec.doDecoding(null));
        assertNull(bCodec.doEncoding(null));
    }

    @Test
    void testUTF8RoundTrip() throws Exception {
        // Given Russian and Swiss German strings
        final String ru_msg = constructString(RUSSIAN_STUFF_UNICODE);
        final String ch_msg = constructString(SWISS_GERMAN_STUFF_UNICODE);

        // And a BCodec configured with UTF-8
        final BCodec bcodec = new BCodec(CharEncoding.UTF_8);

        // When encoding the strings
        // Then the encoded strings are as expected
        assertEquals("=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=", bcodec.encode(ru_msg));
        assertEquals("=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=", bcodec.encode(ch_msg));

        // When encoding and then decoding the strings
        // Then the decoded strings equal the original strings (round trip)
        assertEquals(ru_msg, bcodec.decode(bcodec.encode(ru_msg)));
        assertEquals(ch_msg, bcodec.decode(bcodec.encode(ch_msg)));
    }

}