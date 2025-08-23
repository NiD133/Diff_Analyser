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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for URLCodec (application/x-www-form-urlencoded rules).
 * 
 * Notes for maintainers:
 * - Prefer explicit charsets to avoid platform-default surprises.
 * - Use clear constants instead of magic strings.
 * - Keep validateCodecState() as a placeholder for future state checks.
 */
@DisplayName("URLCodec tests")
class URLCodecTest {

    // Common test fixtures
    private static final String HELLO = "Hello there!";
    private static final String HELLO_ENCODED = "Hello+there%21";

    private static final String SAFE_CHARS = "abc123_-.*";
    private static final String UNSAFE_CHARS = "~!@#$%^&()+{}\"\\;:`,/[]";
    private static final String UNSAFE_ENCODED = "%7E%21%40%23%24%25%5E%26%28%29%2B%7B%7D%22%5C%3B%3A%60%2C%2F%5B%5D";

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    private static final int[] SWISS_GERMAN_CODEPOINTS = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_CODEPOINTS = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };

    // Helper to build strings from code points (handles BMP and beyond).
    private static String fromCodePoints(final int[] codePoints) {
        final StringBuilder sb = new StringBuilder();
        if (codePoints != null) {
            for (final int cp : codePoints) {
                sb.appendCodePoint(cp);
            }
        }
        return sb.toString();
    }

    // Placeholder for future state checks; keeps test intent obvious/readable.
    private static void validateCodecState(final URLCodec codec) {
        // No internal state to validate at present.
    }

    @Test
    @DisplayName("Basic String encode/decode")
    void basicEncodeDecode() throws Exception {
        final URLCodec codec = new URLCodec();

        final String encoded = codec.encode(HELLO);
        assertAll(
            () -> assertEquals(HELLO_ENCODED, encoded, "should percent-encode and replace spaces with +"),
            () -> assertEquals(HELLO, codec.decode(encoded), "should decode back to original")
        );

        validateCodecState(codec);
    }

    @ParameterizedTest(name = "Invalid escape sequence: \"{0}\"")
    @ValueSource(strings = { "%", "%A", "%WW", "%0W" })
    @DisplayName("Decode rejects malformed % sequences")
    void decodeInvalidSequences(final String input) throws Exception {
        final URLCodec codec = new URLCodec();
        assertThrows(DecoderException.class, () -> codec.decode(input));
        validateCodecState(codec);
    }

    @Test
    @DisplayName("decode(byte[]) returns identical bytes when input has no escapes")
    void decodeInvalidContentPassThrough() throws Exception {
        final String swiss = fromCodePoints(SWISS_GERMAN_CODEPOINTS);
        final URLCodec codec = new URLCodec();

        final byte[] input = swiss.getBytes(ISO_8859_1);
        final byte[] output = codec.decode(input);

        assertArrayEquals(input, output, "bytes without % escapes should pass through unchanged");
        validateCodecState(codec);
    }

    @Test
    @DisplayName("decode(Object) handles String and byte[]; rejects other types")
    void decodeObjects() throws Exception {
        final URLCodec codec = new URLCodec();
        final String encoded = HELLO_ENCODED;

        final String decodedFromString = (String) codec.decode((Object) encoded);
        final byte[] decodedFromBytes = (byte[]) codec.decode((Object) encoded.getBytes(UTF_8));

        assertAll(
            () -> assertEquals(HELLO, decodedFromString),
            () -> assertEquals(HELLO, new String(decodedFromBytes, UTF_8)),
            () -> assertNull(codec.decode((Object) null), "null input should yield null"),
            () -> assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)),
                               "non-String/byte[] should be rejected")
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("decode(null, charset) -> null")
    void decodeStringWithNull() throws Exception {
        final URLCodec codec = new URLCodec();
        assertNull(codec.decode((String) null, "charset"));
    }

    @Test
    @DisplayName("decodeUrl(null) -> null")
    void decodeWithNullArray() throws Exception {
        assertNull(URLCodec.decodeUrl(null));
    }

    @Test
    @DisplayName("Default encoding consistency")
    void defaultEncoding() throws Exception {
        final String nonDefault = "UnicodeBig"; // Alias for UTF-16BE on older JDKs.

        final URLCodec codec = new URLCodec(nonDefault);
        // Historical quirk for older JDKs retained from original test suite.
        codec.encode(HELLO);

        final String encodedWithExplicit = codec.encode(HELLO, nonDefault);
        final String encodedWithDefault = codec.encode(HELLO);

        assertEquals(encodedWithExplicit, encodedWithDefault, "explicit and default encodings should match");
        validateCodecState(codec);
    }

    @Test
    @DisplayName("encode/decode null Strings")
    void encodeDecodeNullStrings() throws Exception {
        final URLCodec codec = new URLCodec();
        assertAll(
            () -> assertNull(codec.encode((String) null)),
            () -> assertNull(codec.decode((String) null))
        );
        validateCodecState(codec);
    }

    @Test
    @DisplayName("encode(byte[]) handles null input")
    void encodeNullByteArray() throws Exception {
        final URLCodec codec = new URLCodec();
        assertNull(codec.encode((byte[]) null));
        validateCodecState(codec);
    }

    @Test
    @DisplayName("encode(Object) handles String and byte[]; rejects other types")
    void encodeObjects() throws Exception {
        final URLCodec codec = new URLCodec();

        final String encodedFromString = (String) codec.encode((Object) HELLO);
        final byte[] encodedFromBytes = (byte[]) codec.encode((Object) HELLO.getBytes(UTF_8));

        assertAll(
            () -> assertEquals(HELLO_ENCODED, encodedFromString),
            () -> assertEquals(HELLO_ENCODED, new String(encodedFromBytes, UTF_8)),
            () -> assertNull(codec.encode((Object) null), "null input should yield null"),
            () -> assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)),
                               "non-String/byte[] should be rejected")
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("encode(null, charset) -> null")
    void encodeStringWithNull() throws Exception {
        final URLCodec codec = new URLCodec();
        assertNull(codec.encode((String) null, "charset"));
    }

    @Test
    @DisplayName("Static encodeUrl(null, bytes) uses default safe set")
    void encodeUrlWithNullBitSet() throws Exception {
        final URLCodec codec = new URLCodec();

        final String encoded = new String(URLCodec.encodeUrl(null, HELLO.getBytes(UTF_8)), UTF_8);

        assertAll(
            () -> assertEquals(HELLO_ENCODED, encoded),
            () -> assertEquals(HELLO, codec.decode(encoded))
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("Bogus default charset leads to encoder/decoder exceptions")
    void invalidDefaultEncoding() {
        final URLCodec codec = new URLCodec("NONSENSE");

        assertAll(
            () -> assertThrows(EncoderException.class, () -> codec.encode(HELLO),
                               "bogus default charset should fail encode"),
            () -> assertThrows(DecoderException.class, () -> codec.decode(HELLO),
                               "bogus default charset should fail decode")
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("Safe characters are passed through unchanged")
    void safeCharEncodeDecode() throws Exception {
        final URLCodec codec = new URLCodec();

        final String encoded = codec.encode(SAFE_CHARS);

        assertAll(
            () -> assertEquals(SAFE_CHARS, encoded),
            () -> assertEquals(SAFE_CHARS, codec.decode(encoded))
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("Unsafe characters are percent-encoded")
    void unsafeEncodeDecode() throws Exception {
        final URLCodec codec = new URLCodec();

        final String encoded = codec.encode(UNSAFE_CHARS);

        assertAll(
            () -> assertEquals(UNSAFE_ENCODED, encoded),
            () -> assertEquals(UNSAFE_CHARS, codec.decode(encoded))
        );

        validateCodecState(codec);
    }

    @Test
    @DisplayName("UTF-8 round-trip for Swiss German and Russian strings")
    void utf8RoundTrip() throws Exception {
        final String russian = fromCodePoints(RUSSIAN_CODEPOINTS);     // "Всем_привет"
        final String swiss = fromCodePoints(SWISS_GERMAN_CODEPOINTS);  // "Grüezi_zämä"

        final URLCodec codec = new URLCodec();
        validateCodecState(codec);

        assertAll(
            () -> assertEquals("%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82",
                               codec.encode(russian, CharEncoding.UTF_8),
                               "expected canonical UTF-8 percent-encoding"),
            () -> assertEquals("Gr%C3%BCezi_z%C3%A4m%C3%A4",
                               codec.encode(swiss, CharEncoding.UTF_8),
                               "expected canonical UTF-8 percent-encoding"),
            () -> assertEquals(russian,
                               codec.decode(codec.encode(russian, CharEncoding.UTF_8), CharEncoding.UTF_8)),
            () -> assertEquals(swiss,
                               codec.decode(codec.encode(swiss, CharEncoding.UTF_8), CharEncoding.UTF_8))
        );

        validateCodecState(codec);
    }
}