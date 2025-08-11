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
 * Quoted-printable codec test cases
 */
class QuotedPrintableCodecTest {

    // Test Data Constants
    private static final int[] SWISS_GERMAN_STUFF_UNICODE = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };
    private static final int[] RUSSIAN_STUFF_UNICODE = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435, 0x442 };
    private static final String BASIC_PLAIN_TEXT = "= Hello there =\r\n";
    private static final String BASIC_ENCODED_TEXT = "=3D Hello there =3D=0D=0A";
    private static final String SAFE_CHARS_STRING = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";
    private static final String EQUALS_CRLF_STRING = "=\r\n";
    private static final String EQUALS_CRLF_ENCODED = "=3D=0D=0A";
    private static final String FINAL_BYTES_TEXT = "This is a example of a quoted=printable text file. There is no tt";
    private static final String FINAL_BYTES_ENCODED = "This is a example of a quoted=3Dprintable text file. There is no tt";

    private String constructString(final int[] unicodeChars) {
        final StringBuilder buffer = new StringBuilder();
        if (unicodeChars != null) {
            for (final int unicodeChar : unicodeChars) {
                buffer.append((char) unicodeChar);
            }
        }
        return buffer.toString();
    }

    private QuotedPrintableCodec createCodec() {
        return new QuotedPrintableCodec();
    }

    private QuotedPrintableCodec createStrictCodec() {
        return new QuotedPrintableCodec(true);
    }

    // =====================
    // Basic Functionality Tests
    // =====================
    
    @Test
    void encodeDecodeBasicText_ShouldReturnOriginalText() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String encoded = codec.encode(BASIC_PLAIN_TEXT);
        assertEquals(BASIC_ENCODED_TEXT, encoded, "Basic text encoding");
        assertEquals(BASIC_PLAIN_TEXT, codec.decode(encoded), "Basic text decoding");
    }

    @Test
    void encodeDecodeSafeCharacters_ShouldNotModifyText() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String encoded = codec.encode(SAFE_CHARS_STRING);
        assertEquals(SAFE_CHARS_STRING, encoded, "Safe characters should not be encoded");
        assertEquals(SAFE_CHARS_STRING, codec.decode(encoded), "Safe characters should decode to original");
    }

    @Test
    void encodeUnsafeCharacters_ShouldEscapeCorrectly() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String encoded = codec.encode(EQUALS_CRLF_STRING);
        assertEquals(EQUALS_CRLF_ENCODED, encoded, "Unsafe characters should be properly escaped");
        assertEquals(EQUALS_CRLF_STRING, codec.decode(encoded), "Encoded unsafe characters should decode to original");
    }

    // =====================
    // Edge Case Tests
    // =====================

    @Test
    void encodeNullInput_ShouldReturnNull() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        assertNull(codec.encode((String) null), "Null string should return null when encoded");
        assertNull(codec.encode((byte[]) null), "Null byte array should return null when encoded");
    }

    @Test
    void decodeNullInput_ShouldReturnNull() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        assertNull(codec.decode((String) null), "Null string should return null when decoded");
        assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null), "Null byte array should return null when decoded");
    }

    @Test
    void decodeInvalidSequences_ShouldThrowDecoderException() {
        final QuotedPrintableCodec codec = createCodec();
        assertThrows(DecoderException.class, () -> codec.decode("="), 
            "Incomplete escape sequence (=) should fail");
        assertThrows(DecoderException.class, () -> codec.decode("=A"), 
            "Incomplete escape sequence (=A) should fail");
        assertThrows(DecoderException.class, () -> codec.decode("=WW"), 
            "Invalid escape sequence (=WW) should fail");
    }

    @Test
    void createCodecWithInvalidCharset_ShouldThrowException() {
        assertThrows(UnsupportedCharsetException.class, () -> new QuotedPrintableCodec("NONSENSE"),
            "Creating codec with invalid charset should throw exception");
    }

    // =====================
    // Object Handling Tests
    // =====================
    
    @Test
    void decodeObject_WithValidInput_ShouldDecodeCorrectly() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String encodedText = "1+1 =3D 2";
        
        // Test String input
        String decoded = (String) codec.decode((Object) encodedText);
        assertEquals("1+1 = 2", decoded, "String object decoding");
        
        // Test byte array input
        final byte[] encodedBytes = encodedText.getBytes(StandardCharsets.UTF_8);
        final byte[] decodedBytes = (byte[]) codec.decode((Object) encodedBytes);
        decoded = new String(decodedBytes);
        assertEquals("1+1 = 2", decoded, "Byte array object decoding");
    }

    @Test
    void decodeInvalidObjectType_ShouldThrowDecoderException() {
        final QuotedPrintableCodec codec = createCodec();
        assertThrows(DecoderException.class, () -> codec.decode(Double.valueOf(3.0d)),
            "Non-String/byte[] objects should cause exception");
    }

    @Test
    void encodeObject_WithValidInput_ShouldEncodeCorrectly() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String plainText = "1+1 = 2";
        
        // Test String input
        String encoded = (String) codec.encode((Object) plainText);
        assertEquals("1+1 =3D 2", encoded, "String object encoding");
        
        // Test byte array input
        final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedBytes = (byte[]) codec.encode((Object) plainBytes);
        encoded = new String(encodedBytes);
        assertEquals("1+1 =3D 2", encoded, "Byte array object encoding");
    }

    @Test
    void encodeInvalidObjectType_ShouldThrowEncoderException() {
        final QuotedPrintableCodec codec = createCodec();
        assertThrows(EncoderException.class, () -> codec.encode(Double.valueOf(3.0d)),
            "Non-String/byte[] objects should cause exception");
    }

    // =====================
    // Special Encoding Tests
    // =====================
    
    @Test
    void encodeWithStrictMode_ShouldHandleFinalBytesCorrectly() throws Exception {
        final QuotedPrintableCodec strictCodec = createStrictCodec();
        assertEquals(FINAL_BYTES_ENCODED, strictCodec.encode(FINAL_BYTES_TEXT),
            "Equals sign at end should be encoded in strict mode");
    }

    @Test
    void encodeWithStrictMode_ShouldHandleSpecialCharactersAtEnd() throws Exception {
        final QuotedPrintableCodec strictCodec = createStrictCodec();
        
        String plain = "This is a example of a quoted-printable text file. This might contain sp=cial chars.";
        String expected = "This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.";
        assertEquals(expected, strictCodec.encode(plain),
            "Special characters should be properly encoded and soft line breaks added");
        
        plain = "This is a example of a quoted-printable text file. This might contain ta\tbs as well.";
        expected = "This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.";
        assertEquals(expected, strictCodec.encode(plain),
            "Tab characters should be properly encoded and soft line breaks added");
    }

    @Test
    void encodeWithStrictMode_ShouldHandleSoftLineBreaks() throws Exception {
        final QuotedPrintableCodec strictCodec = createStrictCodec();
        final String qpdata = "If you believe that truth=3Dbeauty, then surely mathematics is the most b=\r\neautiful branch of philosophy.";
        final String expected = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        
        assertEquals(qpdata, strictCodec.encode(expected),
            "Encoding with strict mode should insert soft line breaks");
        assertEquals(expected, strictCodec.decode(qpdata),
            "Decoding should handle soft line breaks correctly");
    }

    @Test
    void decode_ShouldHandleSoftLineBreaks() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String qpdata = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
        final String expected = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
        
        assertEquals(expected, codec.decode(qpdata),
            "Decoding should handle soft line breaks and escaped characters");
    }

    @Test
    void decode_ShouldSkipUnencodedCRLFInMiddleOfEncodedSequence() throws Exception {
        final QuotedPrintableCodec strictCodec = createStrictCodec();
        final String qpdata = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
        final String expected = "CRLF in an encoded text should be skipped in the decoding.";
        
        assertEquals(expected, strictCodec.decode(qpdata),
            "CRLF in middle of encoded sequence should be skipped during decoding");
    }

    @Test
    void encodeWithStrictMode_ShouldHandleLineEndingsCorrectly() throws Exception {
        final QuotedPrintableCodec strictCodec = createStrictCodec();
        
        String plain = "This is a example of a quoted-printable text file. There is no end to it\t";
        String expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=09";
        assertEquals(expected, strictCodec.encode(plain),
            "Tab at end of line should be properly handled");
        
        plain = "This is a example of a quoted-printable text file. There is no end to it ";
        expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=20";
        assertEquals(expected, strictCodec.encode(plain),
            "Space at end of line should be properly handled");
    }

    // =====================
    // Character Set Tests
    // =====================
    
    @Test
    void encodeDecodeWithDefaultCharset_ShouldUseSpecifiedEncoding() throws Exception {
        final String plain = "Hello there!";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec("UnicodeBig");
        codec.encode(plain); // Workaround for Java 1.2.2 quirk
        
        final String encoded1 = codec.encode(plain, "UnicodeBig");
        final String encoded2 = codec.encode(plain);
        assertEquals(encoded1, encoded2, "Default charset should be used when not specified");
    }

    @Test
    void encodeDecodeUTF8Text_ShouldRoundTripCorrectly() throws Exception {
        final QuotedPrintableCodec codec = createCodec();
        final String russianText = constructString(RUSSIAN_STUFF_UNICODE);
        final String swissGermanText = constructString(SWISS_GERMAN_STUFF_UNICODE);

        // Test encoding
        assertEquals("=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82", 
            codec.encode(russianText, CharEncoding.UTF_8),
            "Russian text should be properly encoded");
        assertEquals("Gr=C3=BCezi_z=C3=A4m=C3=A4", 
            codec.encode(swissGermanText, CharEncoding.UTF_8),
            "Swiss German text should be properly encoded");

        // Test round trip
        assertEquals(russianText, 
            codec.decode(codec.encode(russianText, CharEncoding.UTF_8), CharEncoding.UTF_8),
            "Russian text should round trip correctly");
        assertEquals(swissGermanText, 
            codec.decode(codec.encode(swissGermanText, CharEncoding.UTF_8), CharEncoding.UTF_8),
            "Swiss German text should round trip correctly");
    }
}