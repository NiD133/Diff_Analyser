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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Quoted-printable codec test cases, restructured for clarity and maintainability.
 */
class QuotedPrintableCodecTest {

    @Nested
    @DisplayName("Non-Strict Encoding Tests")
    class EncodingTests {
        private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        @Test
        @DisplayName("Should not encode safe printable ASCII characters")
        void shouldNotEncodeSafeCharacters() throws EncoderException {
            final String plainText = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";
            assertEquals(plainText, codec.encode(plainText));
        }

        @Test
        @DisplayName("Should encode special ASCII characters like '=', CR, and LF")
        void shouldEncodeSpecialAsciiCharacters() throws EncoderException {
            final String plainText = "= Hello there =\r\n";
            final String expectedEncoded = "=3D Hello there =3D=0D=0A";
            assertEquals(expectedEncoded, codec.encode(plainText));
        }

        @Test
        @DisplayName("Should encode a string with a bitset that allows all characters")
        void shouldEncodeWithPermissiveBitSet() {
            final String plainText = "1+1 = 2";
            final String expectedEncoded = "1+1 =3D 2";
            final byte[] encodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, plainText.getBytes(StandardCharsets.UTF_8));
            assertEquals(expectedEncoded, new String(encodedBytes, StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("Non-Strict Decoding Tests")
    class DecodingTests {
        private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        @Test
        @DisplayName("Should decode a basic Quoted-Printable string")
        void shouldDecodeBasicString() throws DecoderException {
            final String encodedText = "=3D Hello there =3D=0D=0A";
            final String expectedDecoded = "= Hello there =\r\n";
            assertEquals(expectedDecoded, codec.decode(encodedText));
        }

        @Test
        @DisplayName("Should decode text with soft line breaks (=\\r\\n)")
        void shouldDecodeTextWithSoftLineBreaks() throws DecoderException {
            final String encodedText = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
            final String expectedDecoded = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
            assertEquals(expectedDecoded, codec.decode(encodedText));
        }

        @Test
        @DisplayName("Should decode safe printable ASCII characters")
        void shouldDecodeSafeCharacters() throws DecoderException {
            final String plainText = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";
            assertEquals(plainText, codec.decode(plainText));
        }
    }

    @Nested
    @DisplayName("Strict Mode (soft line breaks enabled)")
    class StrictModeTests {
        private final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);

        @Test
        @DisplayName("Should encode with soft line breaks to respect line length limit")
        void shouldEncodeWithSoftLineBreaks() throws EncoderException {
            final String plainText = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";
            final String expectedEncoded = "If you believe that truth=3Dbeauty, then surely mathematics is the most b=\r\neautiful branch of philosophy.";
            assertEquals(expectedEncoded, strictCodec.encode(plainText));
        }

        @ParameterizedTest
        @CsvSource({
            "'This is a example of a quoted-printable text file. This might contain sp=cial chars.', 'This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.'",
            "'This is a example of a quoted-printable text file. This might contain ta\tbs as well.', 'This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.'"
        })
        @DisplayName("Should encode trailing special characters before a soft line break")
        void shouldEncodeTrailingSpecialCharacters(String plainText, String expectedEncoded) throws EncoderException {
            assertEquals(expectedEncoded, strictCodec.encode(plainText));
        }

        @ParameterizedTest
        @CsvSource({
            "'This is a example of a quoted-printable text file. There is no end to it\t', 'This is a example of a quoted-printable text file. There is no end to i=\r\nt=09'",
            "'This is a example of a quoted-printable text file. There is no end to it ', 'This is a example of a quoted-printable text file. There is no end to i=\r\nt=20'",
            "'This is a example of a quoted-printable text file. There is no end to   ', 'This is a example of a quoted-printable text file. There is no end to=20=\r\n =20'",
            "'This is a example of a quoted-printable text file. There is no end to=  ', 'This is a example of a quoted-printable text file. There is no end to=3D=\r\n =20'"
        })
        @DisplayName("Should correctly encode trailing whitespace or special chars with soft line breaks")
        void shouldEncodeTrailingWhitespaceWithSoftLineBreaks(String plainText, String expectedEncoded) throws EncoderException {
            assertEquals(expectedEncoded, strictCodec.encode(plainText));
        }

        @Test
        @DisplayName("Should encode an equals sign at the 76th position correctly")
        void shouldEncodeEqualsSignAtLineLimit() throws EncoderException {
            final String plainText = "This is a example of a quoted=printable text file. There is no tt";
            final String expectedEncoded = "This is a example of a quoted=3Dprintable text file. There is no tt";
            assertEquals(expectedEncoded, strictCodec.encode(plainText));
        }

        @Test
        @DisplayName("Should skip unencoded CR and LF characters during decoding")
        void shouldSkipUnencodedCrLfWhenDecoding() throws DecoderException {
            final String encodedText = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
            final String expectedDecoded = "CRLF in an encoded text should be skipped in the decoding.";
            assertEquals(expectedDecoded, strictCodec.decode(encodedText));
        }

        @Test
        @DisplayName("Should return null when encoding a byte array shorter than 3 bytes")
        void shouldReturnNullWhenEncodingShortByteArrayInStrictMode() throws EncoderException {
            // This test covers a peculiar behavior of the strict encoder where it returns
            // null for byte arrays with length < 3. This is due to an internal constant MIN_BYTES.
            final String shortString = "AA"; // Results in a 2-byte array
            assertNull(strictCodec.encode(shortString));
        }
    }

    @Nested
    @DisplayName("Object API Tests")
    class ObjectApiTests {
        private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        @Test
        @DisplayName("Should encode a String object")
        void shouldEncodeStringObject() throws EncoderException {
            final String plainText = "1+1 = 2";
            final String expectedEncoded = "1+1 =3D 2";
            assertEquals(expectedEncoded, codec.encode((Object) plainText));
        }

        @Test
        @DisplayName("Should encode a byte array object")
        void shouldEncodeByteArrayObject() throws EncoderException {
            final byte[] plainBytes = "1+1 = 2".getBytes(StandardCharsets.UTF_8);
            final byte[] expectedEncoded = "1+1 =3D 2".getBytes(StandardCharsets.UTF_8);
            final byte[] actualEncoded = (byte[]) codec.encode((Object) plainBytes);
            assertEquals(new String(expectedEncoded), new String(actualEncoded));
        }

        @Test
        @DisplayName("Should decode a String object")
        void shouldDecodeStringObject() throws DecoderException {
            final String encodedText = "1+1 =3D 2";
            final String expectedDecoded = "1+1 = 2";
            assertEquals(expectedDecoded, codec.decode((Object) encodedText));
        }

        @Test
        @DisplayName("Should decode a byte array object")
        void shouldDecodeByteArrayObject() throws DecoderException {
            final byte[] encodedBytes = "1+1 =3D 2".getBytes(StandardCharsets.UTF_8);
            final String expectedDecoded = "1+1 = 2";
            final byte[] decodedBytes = (byte[]) codec.decode((Object) encodedBytes);
            assertEquals(expectedDecoded, new String(decodedBytes));
        }

        @Test
        @DisplayName("Should throw EncoderException when encoding an unsupported object type")
        void shouldThrowExceptionWhenEncodingUnsupportedObject() {
            assertThrows(EncoderException.class, () -> codec.encode(3.0d));
        }

        @Test
        @DisplayName("Should throw DecoderException when decoding an unsupported object type")
        void shouldThrowExceptionWhenDecodingUnsupportedObject() {
            assertThrows(DecoderException.class, () -> codec.decode(3.0d));
        }
    }

    @Nested
    @DisplayName("Null and Empty Input Handling")
    class NullAndEmptyInputTests {
        private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        @Test
        @DisplayName("Should return null when encoding a null String")
        void shouldReturnNullWhenEncodingNullString() throws EncoderException {
            assertNull(codec.encode((String) null));
        }

        @Test
        @DisplayName("Should return null when decoding a null String")
        void shouldReturnNullWhenDecodingNullString() throws DecoderException {
            assertNull(codec.decode((String) null));
        }

        @Test
        @DisplayName("Should return null when encoding a null byte array")
        void shouldReturnNullWhenEncodingNullByteArray() throws EncoderException {
            assertNull(codec.encode((byte[]) null));
        }

        @Test
        @DisplayName("Should return null when decoding a null byte array")
        void shouldReturnNullWhenDecodingNullByteArray() throws DecoderException {
            assertNull(QuotedPrintableCodec.decodeQuotedPrintable(null));
        }

        @Test
        @DisplayName("Should return null when encoding a null Object")
        void shouldReturnNullWhenEncodingNullObject() throws EncoderException {
            assertNull(codec.encode((Object) null));
        }

        @Test
        @DisplayName("Should return null when decoding a null Object")
        void shouldReturnNullWhenDecodingNullObject() throws DecoderException {
            assertNull(codec.decode((Object) null));
        }

        @Test
        @DisplayName("Should return null when encoding a null string with a specified charset")
        void shouldReturnNullWhenEncodingNullStringWithCharset() throws EncoderException {
            assertNull(codec.encode(null, "UTF-8"));
        }

        @Test
        @DisplayName("Should return null when decoding a null string with a specified charset")
        void shouldReturnNullWhenDecodingNullStringWithCharset() throws DecoderException {
            assertNull(codec.decode(null, "UTF-8"));
        }
    }

    @Nested
    @DisplayName("Invalid Input Handling")
    class InvalidInputTests {
        private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        @ParameterizedTest
        @ValueSource(strings = {"=", "=A", "=WW"})
        @DisplayName("Should throw DecoderException for malformed sequences")
        void shouldThrowDecoderExceptionForMalformedSequences(String invalidInput) {
            assertThrows(DecoderException.class, () -> codec.decode(invalidInput));
        }

        @Test
        @DisplayName("Should throw UnsupportedCharsetException for an invalid charset name")
        void shouldThrowExceptionForInvalidCharsetName() {
            assertThrows(UnsupportedCharsetException.class, () -> new QuotedPrintableCodec("NONSENSE"));
        }
    }



    @Nested
    @DisplayName("Charset Behavior")
    class CharsetTests {
        @Test
        @DisplayName("Should correctly encode and decode a UTF-8 string with Russian characters")
        void shouldRoundTripRussianStringWithUtf8() throws EncoderException, DecoderException {
            final QuotedPrintableCodec codec = new QuotedPrintableCodec();
            final String plainText = "Всем_привет"; // "Hello everyone" in Russian
            final String expectedEncoded = "=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82";

            final String actualEncoded = codec.encode(plainText, StandardCharsets.UTF_8);
            assertEquals(expectedEncoded, actualEncoded, "Encoding failed");

            final String actualDecoded = codec.decode(actualEncoded, StandardCharsets.UTF_8);
            assertEquals(plainText, actualDecoded, "Decoding failed");
        }

        @Test
        @DisplayName("Should correctly encode and decode a UTF-8 string with Swiss-German characters")
        void shouldRoundTripSwissGermanStringWithUtf8() throws EncoderException, DecoderException {
            final QuotedPrintableCodec codec = new QuotedPrintableCodec();
            final String plainText = "Grüezi_zämä"; // "Hello together" in Swiss-German
            final String expectedEncoded = "Gr=C3=BCezi_z=C3=A4m=C3=A4";

            final String actualEncoded = codec.encode(plainText, StandardCharsets.UTF_8);
            assertEquals(expectedEncoded, actualEncoded, "Encoding failed");

            final String actualDecoded = codec.decode(actualEncoded, StandardCharsets.UTF_8);
            assertEquals(plainText, actualDecoded, "Decoding failed");
        }

        @Test
        @DisplayName("Should use the charset provided in the constructor as the default")
        void shouldUseCharsetFromConstructorAsDefault() throws EncoderException {
            final String plainText = "Hello there!";
            // Using a non-default charset to ensure the constructor setting is respected.
            final QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-16BE");

            final String encodedWithExplicitCharset = codec.encode(plainText, "UTF-16BE");
            final String encodedWithDefaultCharset = codec.encode(plainText);

            assertEquals(encodedWithExplicitCharset, encodedWithDefaultCharset);
        }
    }
}