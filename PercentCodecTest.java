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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PercentCodec} class.
 */
class PercentCodecTest {

    /**
     * Tests that the default codec does not encode safe ASCII characters.
     */
    @Test
    void encodeAndDecode_shouldBeSymmetrical_forSafeAscii() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        final String plainText = "abcdABCD";

        // Act
        final byte[] encodedBytes = percentCodec.encode(plainText.getBytes(StandardCharsets.UTF_8));
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);

        // Assert
        assertEquals(plainText, new String(encodedBytes, StandardCharsets.UTF_8), "Safe characters should not be encoded");
        assertEquals(plainText, new String(decodedBytes, StandardCharsets.UTF_8), "Decoding should restore the original string");
    }

    /**
     * Tests that unsafe and multi-byte characters are correctly encoded and decoded.
     * The default codec encodes non-ASCII chars and the '%' char, but not space.
     */
    @Test
    void encodeAndDecode_shouldHandleUnsafeAndMultiByteCharacters() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        final String plainText = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% "; // Greek letters, percent, space
        final String expectedEncodedText = "%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ";

        // Act
        final byte[] encodedBytes = percentCodec.encode(plainText.getBytes(StandardCharsets.UTF_8));
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);

        // Assert
        assertEquals(expectedEncodedText, new String(encodedBytes, StandardCharsets.UTF_8));
        assertEquals(plainText, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    /**
     * Tests that the codec can be configured to encode spaces as '+' instead of '%20'.
     */
    @Test
    void encodeAndDecode_shouldUsePlusForSpace_whenConfigured() throws Exception {
        // Arrange
        final String plainText = "a b c d";
        final String expectedEncodedText = "a+b+c+d";
        final PercentCodec percentCodec = new PercentCodec(null, true); // plusForSpace = true

        // Act
        final byte[] encodedBytes = percentCodec.encode(plainText.getBytes(StandardCharsets.UTF_8));
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);

        // Assert
        assertEquals(expectedEncodedText, new String(encodedBytes, StandardCharsets.UTF_8));
        assertEquals(plainText, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    /**
     * Tests that the codec can be configured to encode spaces as '%20'.
     * This replaces the original disabled test and clarifies the codec's behavior.
     */
    @Test
    void encode_shouldUsePercent20ForSpace_whenConfiguredAsUnsafe() throws Exception {
        // Arrange
        final byte[] unsafeChars = {' '};
        final PercentCodec percentCodec = new PercentCodec(unsafeChars, false); // plusForSpace = false
        final String plainText = "a b c";
        final byte[] expectedEncodedBytes = "a%20b%20c".getBytes(StandardCharsets.UTF_8);

        // Act
        final byte[] encodedBytes = percentCodec.encode(plainText.getBytes(StandardCharsets.UTF_8));

        // Assert
        assertArrayEquals(expectedEncodedBytes, encodedBytes);
    }

    /**
     * Tests that the codec can be configured with a custom set of characters to always encode.
     */
    @Test
    void encode_shouldProcessCustomUnsafeCharacters() throws Exception {
        // Arrange
        // Input contains chars to be encoded ('a','b','c'), safe chars ('123_-.*'), and non-ASCII chars ('α','β')
        final String plainText = "abc123_-.*\u03B1\u03B2";
        final byte[] alwaysEncode = "abcdef".getBytes(StandardCharsets.UTF_8);
        final PercentCodec percentCodec = new PercentCodec(alwaysEncode, false);

        // 'a','b','c' are in alwaysEncode. '123_-.*' are safe. 'α','β' are non-ASCII.
        final String expectedEncodedText = "%61%62%63" + "123_-.*" + "%CE%B1%CE%B2";

        // Act
        final byte[] encodedBytes = percentCodec.encode(plainText.getBytes(StandardCharsets.UTF_8));
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);

        // Assert
        assertEquals(expectedEncodedText, new String(encodedBytes, StandardCharsets.UTF_8));
        assertEquals(plainText, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    /**
     * Tests that encoding and decoding handle null and empty inputs gracefully.
     */
    @Test
    void encodeAndDecode_shouldHandleNullAndEmptyInputs() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec(null, true);
        final byte[] emptyBytes = {};

        // Act & Assert for null
        assertNull(percentCodec.encode(null), "Encoding null should return null");
        assertNull(percentCodec.decode(null), "Decoding null should return null");

        // Act & Assert for empty
        assertArrayEquals(emptyBytes, percentCodec.encode(emptyBytes), "Encoding an empty array should return an empty array");
        assertArrayEquals(emptyBytes, percentCodec.decode(emptyBytes), "Decoding an empty array should return an empty array");
    }

    /**
     * Tests that decoding an incomplete escape sequence (e.g., '%' at the end of the array)
     * throws a DecoderException.
     */
    @Test
    void decode_shouldThrowDecoderException_forIncompleteEscapeSequence() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        final String multiByteString = "\u03B1\u03B2"; // Each char is 2 bytes, e.g., %CE%B1
        final byte[] encodedBytes = percentCodec.encode(multiByteString.getBytes(StandardCharsets.UTF_8));
        final byte[] incompleteData = Arrays.copyOf(encodedBytes, encodedBytes.length - 1); // Truncate last byte

        // Act & Assert
        final DecoderException e = assertThrows(DecoderException.class, () -> percentCodec.decode(incompleteData));
        assertInstanceOf(ArrayIndexOutOfBoundsException.class, e.getCause(), "Cause should be AIOOBE for incomplete sequence");
    }

    /**
     * Tests the Object-based encode/decode methods with safe ASCII characters.
     */
    @Test
    void encodeAndDecodeObject_shouldBeSymmetrical_forSafeAsciiBytes() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec(null, true);
        final String plainText = "abc123_-.*";
        final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);

        // Act
        final Object encodedObject = percentCodec.encode((Object) plainBytes);
        final Object decodedObject = percentCodec.decode(encodedObject);

        // Assert
        assertEquals(plainText, new String((byte[]) encodedObject, StandardCharsets.UTF_8));
        assertEquals(plainText, new String((byte[]) decodedObject, StandardCharsets.UTF_8));
    }

    /**
     * Tests that passing a non-byte[] object to encode(Object) throws an EncoderException.
     */
    @Test
    void encodeObject_shouldThrowEncoderException_forUnsupportedType() {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();

        // Act & Assert
        assertThrows(EncoderException.class, () -> percentCodec.encode("This is a string, not a byte[]"));
    }

    /**
     * Tests that passing a non-byte[] object to decode(Object) throws a DecoderException.
     */
    @Test
    void decodeObject_shouldThrowDecoderException_forUnsupportedType() {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();

        // Act & Assert
        assertThrows(DecoderException.class, () -> percentCodec.decode("This is a string, not a byte[]"));
    }

    /**
     * Tests that passing null to encode(Object) returns null.
     */
    @Test
    void encodeObject_shouldReturnNull_forNullInput() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();

        // Act & Assert
        assertNull(percentCodec.encode((Object) null));
    }

    /**
     * Tests that passing null to decode(Object) returns null.
     */
    @Test
    void decodeObject_shouldReturnNull_forNullInput() throws Exception {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();

        // Act & Assert
        assertNull(percentCodec.decode((Object) null));
    }

    /**
     * Tests that the constructor throws an IllegalArgumentException if the "always encode"
     * set contains non-ASCII characters.
     */
    @Test
    void constructor_shouldThrowIllegalArgumentException_forNonAsciiInAlwaysEncodeSet() {
        // Arrange
        final byte[] invalidUnsafeChars = { (byte) 200, (byte) 'A' }; // 200 is non-ASCII

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PercentCodec(invalidUnsafeChars, true));
    }
}