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
 * Percent codec test cases.
 */
class PercentCodecTest {

    @Test
    void testBasicEncodeAndDecode() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String original = "abcdABCD";

        final byte[] encoded = codec.encode(original.getBytes(StandardCharsets.UTF_8));
        final String encodedStr = new String(encoded, StandardCharsets.UTF_8);
        assertEquals(original, encodedStr, "Safe characters should not be encoded");

        final byte[] decoded = codec.decode(encoded);
        final String decodedStr = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(original, decodedStr, "Decoded output should match original input");
    }

    @Test
    @Disabled("TODO: Determine if this test should be kept or removed")
    void testEncodeSpaceCharacter() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final byte[] encoded = codec.encode(" ".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals("%20".getBytes(StandardCharsets.UTF_8), encoded, 
            "Space character should be encoded as %20");
    }

    @Test
    void testEncodeWithConfiguredSafeChars() throws Exception {
        final PercentCodec codec = new PercentCodec("abcdef".getBytes(StandardCharsets.UTF_8), false);
        final String input = "abc123_-.*\u03B1\u03B2";

        final byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedStr = new String(encoded, StandardCharsets.UTF_8);
        assertEquals("%61%62%63123_-.*%CE%B1%CE%B2", encodedStr, 
            "Characters in safe set should be encoded while others remain");

        final byte[] decoded = codec.decode(encoded);
        assertEquals(input, new String(decoded, StandardCharsets.UTF_8), 
            "Decoded output should match original input");
    }

    @Test
    void testDecodeWithInvalidLengthThrowsException() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String input = "\u03B1\u03B2";
        final byte[] encoded = codec.encode(input.getBytes(StandardCharsets.UTF_8));
        final byte[] invalidEncoded = Arrays.copyOf(encoded, encoded.length - 1);

        final DecoderException exception = assertThrows(DecoderException.class, 
            () -> codec.decode(invalidEncoded),
            "Decoding incomplete escape sequence should fail"
        );
        assertTrue(exception.getCause() instanceof ArrayIndexOutOfBoundsException,
            "Root cause should be array index issue");
    }

    @Test
    void testDecodeNullObjectReturnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec();
        assertNull(codec.decode((Object) null), "Null input should return null");
    }

    @Test
    void testDecodeUnsupportedObjectTypeThrowsException() {
        final PercentCodec codec = new PercentCodec();
        assertThrows(DecoderException.class, () -> codec.decode("test"),
            "Non-byte array input should throw DecoderException");
    }

    @Test
    void testEncodeNullObjectReturnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec();
        assertNull(codec.encode((Object) null), "Null input should return null");
    }

    @Test
    void testEncodeUnsupportedObjectTypeThrowsException() {
        final PercentCodec codec = new PercentCodec();
        assertThrows(EncoderException.class, () -> codec.encode("test"),
            "Non-byte array input should throw EncoderException");
    }

    @Test
    void testConstructorWithInvalidByteThrowsException() {
        final byte[] invalid = { (byte) -1, (byte) 'A' };
        assertThrows(IllegalArgumentException.class, 
            () -> new PercentCodec(invalid, true),
            "Invalid byte values should be rejected"
        );
    }

    @Test
    void testEncodeNullInputReturnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        assertNull(codec.encode(null), "Null input should return null");
    }

    @Test
    void testDecodeNullInputReturnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        assertNull(codec.decode(null), "Null input should return null");
    }

    @Test
    void testEncodeEmptyInputReturnsEmpty() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final byte[] empty = new byte[0];
        assertArrayEquals(empty, codec.encode(empty),
            "Empty input should return empty array");
    }

    @Test
    void testDecodeEmptyInputReturnsEmpty() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final byte[] empty = new byte[0];
        assertArrayEquals(empty, codec.decode(empty),
            "Empty input should return empty array");
    }

    @Test
    void testEncodeAndDecodeWithPlusForSpace() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final String original = "a b c d";

        final byte[] encoded = codec.encode(original.getBytes(StandardCharsets.UTF_8));
        final String encodedStr = new String(encoded, StandardCharsets.UTF_8);
        assertEquals("a+b+c+d", encodedStr, 
            "Spaces should be encoded as plus signs");

        final byte[] decoded = codec.decode(encoded);
        assertEquals(original, new String(decoded, StandardCharsets.UTF_8),
            "Plus signs should be decoded back to spaces");
    }

    @Test
    void testEncodeAndDecodeObjectWithSafeChars() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final String original = "abc123_-.*";
        final byte[] inputBytes = original.getBytes(StandardCharsets.UTF_8);

        final Object encodedObj = codec.encode((Object) inputBytes);
        assertTrue(encodedObj instanceof byte[], "Encoded object should be byte array");
        final String encodedStr = new String((byte[]) encodedObj, StandardCharsets.UTF_8);
        assertEquals(original, encodedStr, "Safe characters should remain unchanged");

        final Object decodedObj = codec.decode(encodedObj);
        assertTrue(decodedObj instanceof byte[], "Decoded object should be byte array");
        final String decodedStr = new String((byte[]) decodedObj, StandardCharsets.UTF_8);
        assertEquals(original, decodedStr, "Decoded output should match original");
    }

    @Test
    void testEncodeAndDecodeWithUnsafeChars() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String original = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";

        final byte[] encoded = codec.encode(original.getBytes(StandardCharsets.UTF_8));
        final String encodedStr = new String(encoded, StandardCharsets.UTF_8);
        assertEquals("%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ", encodedStr,
            "Unsafe characters should be percent-encoded");

        final byte[] decoded = codec.decode(encoded);
        assertEquals(original, new String(decoded, StandardCharsets.UTF_8),
            "Decoded output should match original input");
    }
}