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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.stream.Stream;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the BCodec, which implements RFC 1522 'B' encoding.
 */
class BCodecTest {

    @Test
    void shouldEncodeAndDecodeAsciiString() throws DecoderException, EncoderException {
        final BCodec codec = new BCodec();
        final String plainText = "Hello there";
        final String encodedText = "=?UTF-8?B?SGVsbG8gdGhlcmU=?=";

        assertEquals(encodedText, codec.encode(plainText));
        assertEquals(plainText, codec.decode(encodedText));
    }

    @Test
    void shouldEncodeAndDecodeNonAsciiStrings() throws DecoderException, EncoderException {
        final BCodec codec = new BCodec(StandardCharsets.UTF_8);
        final String swissGermanText = "Grüezi_zämä";
        final String russianText = "Всем_привет";

        final String encodedSwissGerman = "=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=";
        final String encodedRussian = "=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=";

        assertEquals(encodedSwissGerman, codec.encode(swissGermanText));
        assertEquals(swissGermanText, codec.decode(encodedSwissGerman));

        assertEquals(encodedRussian, codec.encode(russianText));
        assertEquals(russianText, codec.decode(encodedRussian));
    }

    @Test
    void encodeAndDecodeShouldReturnNullForNullString() throws DecoderException, EncoderException {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((String) null));
        assertNull(codec.decode((String) null));
    }

    @Test
    void encodeWithCharsetShouldReturnNullForNullInput() throws EncoderException {
        final BCodec codec = new BCodec();
        assertNull(codec.encode(null, "UTF-8"));
    }

    @Test
    void constructorShouldThrowExceptionForInvalidCharsetName() {
        assertThrows(UnsupportedCharsetException.class, () -> new BCodec("ThisIsAnInvalidCharsetName"));
    }

    @Test
    void encodeObjectShouldCorrectlyEncodeString() throws EncoderException {
        final BCodec codec = new BCodec();
        final String plainText = "what not";
        final String expectedEncoded = "=?UTF-8?B?d2hhdCBub3Q=?=";

        Object result = codec.encode((Object) plainText);

        assertEquals(expectedEncoded, result);
    }

    @Test
    void encodeObjectShouldReturnNullForNullInput() throws EncoderException {
        final BCodec codec = new BCodec();
        assertNull(codec.encode((Object) null));
    }

    @Test
    void encodeObjectShouldThrowExceptionForNonStringInput() {
        final BCodec codec = new BCodec();
        assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)));
    }

    @Test
    void decodeObjectShouldCorrectlyDecodeString() throws DecoderException {
        final BCodec codec = new BCodec();
        final String encodedText = "=?UTF-8?B?d2hhdCBub3Q=?=";
        final String expectedPlainText = "what not";

        Object result = codec.decode((Object) encodedText);

        assertEquals(expectedPlainText, result);
    }

    @Test
    void decodeObjectShouldReturnNullForNullInput() throws DecoderException {
        final BCodec codec = new BCodec();
        assertNull(codec.decode((Object) null));
    }

    @Test
    void decodeObjectShouldThrowExceptionForNonStringInput() {
        final BCodec codec = new BCodec();
        assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)));
    }

    // --- Tests for Strict vs. Lenient decoding of malformed input ---

    static Stream<String> malformedBase64Provider() {
        // These strings are syntactically valid "encoded-word"s but contain
        // Base64 that is impossible (e.g., represents 1 or 4 bytes), which
        // should be rejected by a strict decoder.
        return Stream.of(
            "=?ASCII?B?ZE==?=",      // "d" -> 1 byte, invalid Base64 padding
            "=?ASCII?B?ZmC=?=",      // "fo" -> 2 bytes, invalid Base64 padding
            "=?ASCII?B?Zm9vYE==?=",  // "foo`" -> 4 bytes, invalid Base64 padding
            "=?ASCII?B?Zm9vYmC=?=",  // "foob" -> 4 bytes, invalid Base64 padding
            "=?ASCII?B?AB==?="       // 0x00 0x10 -> 2 bytes, invalid Base64 padding
        );
    }

    @DisplayName("Should not throw exception for malformed Base64 in lenient mode")
    @ParameterizedTest
    @MethodSource("malformedBase64Provider")
    void shouldNotThrowOnMalformedInputInLenientMode(final String malformedInput) {
        // Default constructor creates a lenient codec
        final BCodec defaultCodec = new BCodec();
        assertFalse(defaultCodec.isStrictDecoding(), "Default codec should be lenient");
        assertDoesNotThrow(() -> defaultCodec.decode(malformedInput));

        // Explicitly create a lenient codec
        final BCodec lenientCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.LENIENT);
        assertFalse(lenientCodec.isStrictDecoding(), "Codec should be lenient");
        assertDoesNotThrow(() -> lenientCodec.decode(malformedInput));
    }

    @DisplayName("Should throw DecoderException for malformed Base64 in strict mode")
    @ParameterizedTest
    @MethodSource("malformedBase64Provider")
    void shouldThrowOnMalformedInputInStrictMode(final String malformedInput) {
        final BCodec strictCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(strictCodec.isStrictDecoding(), "Codec should be strict");
        assertThrows(DecoderException.class, () -> strictCodec.decode(malformedInput));
    }
}