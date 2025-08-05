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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link RFC1522Codec} functionality.
 * 
 * <p>Tests RFC 1522 compliant decoding/encoding behavior including:
 * <ul>
 *   <li>Handling of null inputs</li>
 *   <li>Invalid input formats that should throw exceptions</li>
 * </ul>
 */
class RFC1522CodecTest {

    /**
     * Test implementation of RFC1522Codec for validation purposes.
     * 
     * <p>This test codec uses UTF-8 encoding and returns input bytes
     * unchanged during encoding/decoding operations.
     */
    static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            return bytes;
        }

        @Override
        protected String getEncoding() {
            return "T";
        }
    }

    /**
     * Tests that invalid input patterns throw DecoderException during decoding.
     * 
     * @param input Invalid input string to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "whatever",     // No RFC1522 markers
        "=?",           // Incomplete prefix
        "?=",           // Incomplete suffix
        "==",           // Invalid format
        "=??=",         // Missing charset and encoding
        "=?stuff?=",    // Invalid charset format
        "=?UTF-8??=",   // Missing encoding type
        "=?UTF-8?stuff?=",  // Invalid encoding specifier
        "=?UTF-8?T?stuff",  // Missing closing marker
        "=??T?stuff?=", // Missing charset
        "=?UTF-8??stuff?=", // Missing encoding type marker
        "=?UTF-8?W?stuff?=" // Invalid encoding type
    })
    void testDecodeTextThrowsDecoderExceptionForInvalidInputs(String input) {
        final RFC1522TestCodec testCodec = new RFC1522TestCodec();
        assertThrows(DecoderException.class, () -> testCodec.decodeText(input));
    }

    /**
     * Tests that null input returns null during decoding.
     */
    @Test
    void testDecodeTextReturnsNullForNullInput() throws Exception {
        final RFC1522TestCodec testCodec = new RFC1522TestCodec();
        assertNull(testCodec.decodeText(null));
    }

    /**
     * Tests that null input returns null during encoding.
     */
    @Test
    void testEncodeTextReturnsNullForNullInput() throws Exception {
        final RFC1522TestCodec testCodec = new RFC1522TestCodec();
        assertNull(testCodec.encodeText(null, CharEncoding.UTF_8));
    }
}