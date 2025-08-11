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
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Test suite for QuotedPrintableCodec functionality.
 * 
 * Quoted-Printable encoding is used to encode data that consists largely of 
 * printable ASCII characters, making it safe for email transmission.
 */
class QuotedPrintableCodecTest {

    // Test data: "Grüezi_zämä" in Swiss German
    private static final int[] SWISS_GERMAN_UNICODE_CHARS = { 
        0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 
    };

    // Test data: "Всем_привет" (Hello everyone) in Russian
    private static final int[] RUSSIAN_UNICODE_CHARS = { 
        0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 
    };

    private static final String BASIC_TEST_STRING = "= Hello there =\r\n";
    private static final String BASIC_ENCODED_EXPECTED = "=3D Hello there =3D=0D=0A";
    
    private static final String SAFE_CHARS_STRING = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";
    private static final String UNSAFE_CHARS_STRING = "=\r\n";
    private static final String UNSAFE_ENCODED_EXPECTED = "=3D=0D=0A";

    /**
     * Helper method to construct a string from Unicode code points.
     */
    private String createStringFromUnicodeChars(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    // ========== Basic Encoding/Decoding Tests ==========

    @Test
    void shouldEncodeAndDecodeBasicString() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When
        final String encoded = codec.encode(BASIC_TEST_STRING);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(BASIC_ENCODED_EXPECTED, encoded, 
            "Should encode special characters with =XX format");
        assertEquals(BASIC_TEST_STRING, decoded, 
            "Should decode back to original string");
    }

    @Test
    void shouldHandleSafeCharactersWithoutEncoding() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When
        final String encoded = codec.encode(SAFE_CHARS_STRING);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(SAFE_CHARS_STRING, encoded, 
            "Safe characters should not be encoded");
        assertEquals(SAFE_CHARS_STRING, decoded, 
            "Safe characters should decode unchanged");
    }

    @Test
    void shouldEncodeUnsafeCharacters() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When
        final String encoded = codec.encode(UNSAFE_CHARS_STRING);
        final String decoded = codec.decode(encoded);
        
        // Then
        assertEquals(UNSAFE_ENCODED_EXPECTED, encoded, 
            "Unsafe characters should be encoded with =XX format");
        assertEquals(UNSAFE_CHARS_STRING, decoded, 
            "Should decode back to original unsafe characters");
    }

    // ========== Null Handling Tests ==========

    @Test
    void shouldHandleNullInputsGracefully() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When & Then
        assertNull(codec.encode((String) null), 
            "Encoding null string should return null");
        assertNull(codec.decode((String) null), 
            "Decoding null string should return null");
        assertNull(codec.encode((byte[]) null), 
            "Encoding null byte array should return null");
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null), 
            "Static decode with null should return null");
    }

    @Test
    void shouldHandleNullStringWithCharset() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When & Then
        assertNull(codec.encode(null, "UTF-8"), 
            "Encoding null string with charset should return null");
        assertNull(codec.decode(null, "UTF-8"), 
            "Decoding null string with charset should return null");
    }

    // ========== Object Type Handling Tests ==========

    @Test
    void shouldEncodeStringAndByteArrayObjects() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String testString = "1+1 = 2";
        final String expectedEncoded = "1+1 =3D 2";
        
        // When - encode String object
        String encodedFromString = (String) codec.encode((Object) testString);
        
        // When - encode byte array object
        final byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedBytes = (byte[]) codec.encode((Object) testBytes);
        String encodedFromBytes = new String(encodedBytes);
        
        // Then
        assertEquals(expectedEncoded, encodedFromString, 
            "Should encode String object correctly");
        assertEquals(expectedEncoded, encodedFromBytes, 
            "Should encode byte array object correctly");
        assertNull(codec.encode((Object) null), 
            "Should return null for null object");
    }

    @Test
    void shouldDecodeStringAndByteArrayObjects() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String encodedString = "1+1 =3D 2";
        final String expectedDecoded = "1+1 = 2";
        
        // When - decode String object
        String decodedFromString = (String) codec.decode((Object) encodedString);
        
        // When - decode byte array object
        final byte[] encodedBytes = encodedString.getBytes(StandardCharsets.UTF_8);
        final byte[] decodedBytes = (byte[]) codec.decode((Object) encodedBytes);
        String decodedFromBytes = new String(decodedBytes);
        
        // Then
        assertEquals(expectedDecoded, decodedFromString, 
            "Should decode String object correctly");
        assertEquals(expectedDecoded, decodedFromBytes, 
            "Should decode byte array object correctly");
        assertNull(codec.decode((Object) null), 
            "Should return null for null object");
    }

    @Test
    void shouldRejectUnsupportedObjectTypes() {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final Double unsupportedObject = Double.valueOf(3.0d);
        
        // When & Then
        assertThrows(EncoderException.class, 
            () -> codec.encode(unsupportedObject), 
            "Should throw exception when encoding unsupported object type");
        assertThrows(DecoderException.class, 
            () -> codec.decode(unsupportedObject), 
            "Should throw exception when decoding unsupported object type");
    }

    // ========== Error Handling Tests ==========

    @Test
    void shouldRejectInvalidEncodedData() {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When & Then
        assertThrows(DecoderException.class, () -> codec.decode("="), 
            "Should reject incomplete escape sequence");
        assertThrows(DecoderException.class, () -> codec.decode("=A"), 
            "Should reject incomplete hex pair");
        assertThrows(DecoderException.class, () -> codec.decode("=WW"), 
            "Should reject invalid hex characters");
    }

    @Test
    void shouldRejectInvalidCharsetName() {
        // When & Then
        assertThrows(UnsupportedCharsetException.class, 
            () -> new QuotedPrintableCodec("INVALID_CHARSET"), 
            "Should reject invalid charset name");
    }

    // ========== Charset and Encoding Tests ==========

    @Test
    void shouldUseDefaultCharsetConsistently() throws Exception {
        // Given
        final String testString = "Hello there!";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec("UnicodeBig");
        
        // When
        codec.encode(testString); // Workaround for Java 1.2.2 quirk
        final String encodedWithExplicitCharset = codec.encode(testString, "UnicodeBig");
        final String encodedWithDefaultCharset = codec.encode(testString);
        
        // Then
        assertEquals(encodedWithExplicitCharset, encodedWithDefaultCharset, 
            "Default charset should match explicitly specified charset");
    }

    @Test
    void shouldHandleUTF8EncodingRoundTrip() throws Exception {
        // Given
        final String russianText = createStringFromUnicodeChars(RUSSIAN_UNICODE_CHARS);
        final String swissGermanText = createStringFromUnicodeChars(SWISS_GERMAN_UNICODE_CHARS);
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // When & Then - Russian text
        final String encodedRussian = codec.encode(russianText, CharEncoding.UTF_8);
        assertEquals("=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82", 
            encodedRussian, "Should encode Russian text correctly");
        assertEquals(russianText, 
            codec.decode(encodedRussian, CharEncoding.UTF_8), 
            "Should decode Russian text back to original");
        
        // When & Then - Swiss German text
        final String encodedSwissGerman = codec.encode(swissGermanText, CharEncoding.UTF_8);
        assertEquals("Gr=C3=BCezi_z=C3=A4m=C3=A4", 
            encodedSwissGerman, "Should encode Swiss German text correctly");
        assertEquals(swissGermanText, 
            codec.decode(encodedSwissGerman, CharEncoding.UTF_8), 
            "Should decode Swiss German text back to original");
    }

    // ========== Strict Mode Tests (Line Breaking) ==========

    @Test
    void shouldHandleSoftLineBreaksInStrictMode() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        final String inputWithSoftBreaks = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
        final String expectedDecoded = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        
        // When
        final String decoded = strictCodec.decode(inputWithSoftBreaks);
        final String reencoded = strictCodec.encode(expectedDecoded);
        
        // Then
        assertEquals(expectedDecoded, decoded, 
            "Should handle soft line breaks correctly");
        assertEquals(expectedDecoded, strictCodec.decode(reencoded), 
            "Should maintain consistency after re-encoding");
    }

    @Test
    void shouldCreateSoftLineBreaksWhenEncoding() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        final String longText = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        final String expectedWithSoftBreaks = "If you believe that truth=3Dbeauty, then surely mathematics is the most b=\r\neautiful branch of philosophy.";
        
        // When
        final String encoded = strictCodec.encode(longText);
        final String decoded = strictCodec.decode(encoded);
        
        // Then
        assertEquals(expectedWithSoftBreaks, encoded, 
            "Should insert soft line breaks for long lines");
        assertEquals(expectedWithSoftBreaks, strictCodec.encode(decoded), 
            "Should maintain soft line breaks after decode/encode cycle");
    }

    @Test
    void shouldHandleTrailingSpecialCharacters() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        
        // Test with trailing equals sign
        String textWithEquals = "This is a example of a quoted-printable text file. This might contain sp=cial chars.";
        String expectedEquals = "This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.";
        assertEquals(expectedEquals, strictCodec.encode(textWithEquals), 
            "Should handle trailing equals sign with soft line break");
        
        // Test with trailing tab
        String textWithTab = "This is a example of a quoted-printable text file. This might contain ta\tbs as well.";
        String expectedTab = "This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.";
        assertEquals(expectedTab, strictCodec.encode(textWithTab), 
            "Should handle trailing tab with soft line break");
    }

    @Test
    void shouldHandleWhitespaceAtLineEnd() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        
        // Test with trailing tab
        String textWithTrailingTab = "This is a example of a quoted-printable text file. There is no end to it\t";
        String expectedTab = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=09";
        assertEquals(expectedTab, strictCodec.encode(textWithTrailingTab), 
            "Should encode trailing tab before soft break");
        
        // Test with trailing space
        String textWithTrailingSpace = "This is a example of a quoted-printable text file. There is no end to it ";
        String expectedSpace = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=20";
        assertEquals(expectedSpace, strictCodec.encode(textWithTrailingSpace), 
            "Should encode trailing space before soft break");
    }

    @Test
    void shouldSkipCRLFInEncodedText() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        final String encodedWithCRLF = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
        final String expectedDecoded = "CRLF in an encoded text should be skipped in the decoding.";
        
        // When
        final String decoded = strictCodec.decode(encodedWithCRLF);
        final String reencoded = strictCodec.encode(expectedDecoded);
        
        // Then
        assertEquals(expectedDecoded, decoded, 
            "Should skip CRLF characters in encoded text");
        assertEquals(expectedDecoded, strictCodec.decode(reencoded), 
            "Should maintain consistency after re-encoding");
    }

    // ========== Edge Case Tests ==========

    @Test
    void shouldHandleEncodeWithNullBitSet() throws Exception {
        // Given
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String testString = "1+1 = 2";
        final String expectedEncoded = "1+1 =3D 2";
        
        // When
        final String encoded = new String(QuotedPrintableCodec.encodeQuotedPrintable(
            null, testString.getBytes(StandardCharsets.UTF_8)));
        
        // Then
        assertEquals(expectedEncoded, encoded, 
            "Should handle null BitSet parameter");
        assertEquals(testString, codec.decode(encoded), 
            "Should decode correctly after encoding with null BitSet");
    }

    @Test
    void shouldHandleShortByteArrayInStrictMode() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        
        // When
        final String result = strictCodec.encode("AA");
        
        // Then
        assertNull(result, "Should return null for short input in strict mode");
    }

    @Test
    void shouldNotEncodeWhitespaceInNonStrictMode() throws Exception {
        // Given
        final QuotedPrintableCodec nonStrictCodec = new QuotedPrintableCodec(false);
        final String textWithWhitespace = "This is a example of a quoted=printable text file. There is no tt";
        final String expectedEncoded = "This is a example of a quoted=3Dprintable text file. There is no tt";
        
        // When
        final String encoded = nonStrictCodec.encode(textWithWhitespace);
        
        // Then
        assertEquals(expectedEncoded, encoded, 
            "Should encode equals sign but not add line breaks in non-strict mode");
    }

    @Test
    void shouldEncodeWithLineBreaksInStrictMode() throws Exception {
        // Given
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);
        final String textWithWhitespace = "This is a example of a quoted=printable text file. There is no tt";
        final String expectedEncoded = "This is a example of a quoted=3Dprintable text file. There is no tt";
        
        // When
        final String encoded = strictCodec.encode(textWithWhitespace);
        
        // Then
        assertEquals(expectedEncoded, encoded, 
            "Should handle final bytes correctly in strict mode");
    }
}