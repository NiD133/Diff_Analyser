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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test cases for PercentCodec, which implements percent-encoding (URL encoding) as per RFC 3986.
 */
class PercentCodecTest {

    // Test data constants
    private static final String ASCII_ALPHANUMERIC = "abcdABCD";
    private static final String GREEK_LETTERS = "\u03B1\u03B2";
    private static final String MIXED_SAFE_CHARS = "abc123_-.*";
    private static final String SPACE_SEPARATED_TEXT = "a b c d";
    private static final String GREEK_WITH_SPECIAL_CHARS = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";
    
    // Expected encoded results
    private static final String ENCODED_SPACE = "%20";
    private static final String ENCODED_GREEK_LETTERS = "%CE%B1%CE%B2";
    private static final String ENCODED_PLUS_SPACES = "a+b+c+d";
    private static final String ENCODED_GREEK_WITH_SPECIAL = "%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ";

    @Test
    void shouldEncodeAndDecodeBasicAsciiCharactersUnchanged() throws Exception {
        // Given: A codec with default settings and basic ASCII alphanumeric input
        PercentCodec codec = new PercentCodec();
        String input = ASCII_ALPHANUMERIC;
        
        // When: Encoding and then decoding the input
        byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encoded, StandardCharsets.UTF_8);
        
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        
        // Then: Both encoded and decoded results should match the original input
        assertEquals(input, encodedString, "ASCII alphanumeric characters should not be encoded");
        assertEquals(input, decodedString, "Decoded result should match original input");
    }

    @Test
    @Disabled("TODO: Should be removed? Space encoding behavior needs clarification")
    void shouldEncodeSpaceAsPercentTwenty() throws Exception {
        // Given: A codec with default settings
        PercentCodec codec = new PercentCodec();
        String spaceInput = " ";
        
        // When: Encoding a space character
        byte[] encoded = codec.encode(spaceInput.getBytes(StandardCharsets.UTF_8));
        
        // Then: Space should be encoded as %20
        assertArrayEquals(ENCODED_SPACE.getBytes(StandardCharsets.UTF_8), encoded);
    }

    @Test
    void shouldUseCustomSafeCharactersForEncoding() throws Exception {
        // Given: A codec configured to always encode 'abcdef' characters
        String input = MIXED_SAFE_CHARS + GREEK_LETTERS;
        byte[] customAlwaysEncodeChars = "abcdef".getBytes(StandardCharsets.UTF_8);
        PercentCodec codec = new PercentCodec(customAlwaysEncodeChars, false);
        
        // When: Encoding input containing both safe and unsafe characters
        byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encoded, StandardCharsets.UTF_8);
        
        // Then: Custom characters and non-ASCII should be encoded, others should remain unchanged
        String expectedEncoded = "%61%62%63123_-.*" + ENCODED_GREEK_LETTERS;
        assertEquals(expectedEncoded, encodedString, "Custom always-encode characters should be percent-encoded");
        
        // And: Decoding should restore the original input
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(input, decodedString, "Round-trip encoding/decoding should preserve original input");
    }

    @Test
    void shouldThrowDecoderExceptionWhenDecodingTruncatedInput() throws Exception {
        // Given: A codec and properly encoded Greek letters
        PercentCodec codec = new PercentCodec();
        String input = GREEK_LETTERS;
        byte[] validEncoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        
        // When: Attempting to decode truncated encoded data (missing last byte)
        byte[] truncatedEncoded = Arrays.copyOf(validEncoded, validEncoded.length - 1);
        
        // Then: Should throw DecoderException with ArrayIndexOutOfBoundsException as cause
        Exception exception = assertThrows(Exception.class, () -> codec.decode(truncatedEncoded));
        assertTrue(exception instanceof DecoderException, "Should throw DecoderException");
        assertTrue(exception.getCause() instanceof ArrayIndexOutOfBoundsException, 
                  "Should have ArrayIndexOutOfBoundsException as cause");
    }

    @Test
    void shouldReturnNullWhenDecodingNullObject() throws Exception {
        // Given: A codec instance
        PercentCodec codec = new PercentCodec();
        
        // When: Decoding null object
        Object result = codec.decode((Object) null);
        
        // Then: Should return null
        assertNull(result, "Decoding null object should return null");
    }

    @Test
    void shouldThrowDecoderExceptionForUnsupportedObjectType() {
        // Given: A codec instance
        PercentCodec codec = new PercentCodec();
        
        // When/Then: Attempting to decode unsupported object type should throw DecoderException
        assertThrows(DecoderException.class, () -> codec.decode("unsupported_string_input"),
                    "Should throw DecoderException for unsupported object type");
    }

    @Test
    void shouldReturnNullWhenEncodingNullObject() throws Exception {
        // Given: A codec instance
        PercentCodec codec = new PercentCodec();
        
        // When: Encoding null object
        Object result = codec.encode((Object) null);
        
        // Then: Should return null
        assertNull(result, "Encoding null object should return null");
    }

    @Test
    void shouldThrowEncoderExceptionForUnsupportedObjectType() {
        // Given: A codec instance
        PercentCodec codec = new PercentCodec();
        
        // When/Then: Attempting to encode unsupported object type should throw EncoderException
        assertThrows(EncoderException.class, () -> codec.encode("unsupported_string_input"),
                    "Should throw EncoderException for unsupported object type");
    }

    @Test
    void shouldRejectInvalidByteInAlwaysEncodeChars() {
        // Given: Invalid byte array containing negative byte value
        byte[] invalidBytes = { (byte) -1, (byte) 'A' };
        
        // When/Then: Constructor should throw IllegalArgumentException for invalid bytes
        assertThrows(IllegalArgumentException.class, () -> new PercentCodec(invalidBytes, true),
                    "Should reject invalid byte values in always-encode characters");
    }

    @Test
    void shouldHandleNullAndEmptyInputGracefully() throws Exception {
        // Given: A codec with null always-encode chars and plus-for-space enabled
        PercentCodec codec = new PercentCodec(null, true);
        byte[] emptyInput = "".getBytes(StandardCharsets.UTF_8);
        
        // When/Then: Null inputs should return null
        assertNull(codec.encode(null), "Encoding null input should return null");
        assertNull(codec.decode(null), "Decoding null input should return null");
        
        // When/Then: Empty inputs should return empty arrays
        assertArrayEquals(emptyInput, codec.encode(emptyInput), "Encoding empty input should return empty array");
        assertArrayEquals(emptyInput, codec.decode(emptyInput), "Decoding empty input should return empty array");
    }

    @Test
    void shouldEncodePlusForSpaceWhenConfigured() throws Exception {
        // Given: A codec configured to use plus signs for spaces
        PercentCodec codec = new PercentCodec(null, true);
        String input = SPACE_SEPARATED_TEXT;
        
        // When: Encoding text with spaces
        byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encoded, StandardCharsets.UTF_8);
        
        // Then: Spaces should be encoded as plus signs
        assertEquals(ENCODED_PLUS_SPACES, encodedString, "Spaces should be encoded as plus signs when configured");
        
        // And: Decoding should restore original input
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(input, decodedString, "Decoding should restore original spaced text");
    }

    @Test
    void shouldHandleSafeCharactersInObjectEncodeDecodeRoundTrip() throws Exception {
        // Given: A codec with plus-for-space enabled and input containing only safe characters
        PercentCodec codec = new PercentCodec(null, true);
        String input = MIXED_SAFE_CHARS;
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        
        // When: Encoding and decoding as objects
        Object encodedObject = codec.encode((Object) inputBytes);
        String encodedString = new String((byte[]) encodedObject, StandardCharsets.UTF_8);
        
        Object decodedObject = codec.decode(encodedObject);
        String decodedString = new String((byte[]) decodedObject, StandardCharsets.UTF_8);
        
        // Then: Safe characters should remain unchanged through the round trip
        assertEquals(input, encodedString, "Safe characters should not be encoded");
        assertEquals(input, decodedString, "Round-trip should preserve safe characters");
    }

    @Test
    void shouldEncodeUnsafeAndNonAsciiCharacters() throws Exception {
        // Given: A codec with default settings and input containing Greek letters and special characters
        PercentCodec codec = new PercentCodec();
        String input = GREEK_WITH_SPECIAL_CHARS;
        
        // When: Encoding the input
        byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        String encodedString = new String(encoded, StandardCharsets.UTF_8);
        
        // Then: Non-ASCII and percent characters should be encoded, space should remain unchanged
        assertEquals(ENCODED_GREEK_WITH_SPECIAL, encodedString, 
                    "Non-ASCII characters and percent sign should be encoded");
        
        // And: Decoding should restore the original input
        byte[] decoded = codec.decode(encoded);
        String decodedString = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(input, decodedString, "Decoding should restore original input with special characters");
    }
}