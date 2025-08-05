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

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the common functionality of the abstract RFC1522Codec class.
 */
@DisplayName("Tests for RFC1522Codec abstract class")
class RFC1522CodecTest {

    /**
     * A minimal, concrete implementation of RFC1522Codec for testing purposes.
     * <p>
     * This stub uses 'T' as its encoding identifier and performs no actual
     * encoding or decoding; it simply passes the byte arrays through. This allows
     * for testing the parsing and formatting logic of the abstract class in
     * isolation.
     * </p>
     */
    private static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected String getEncoding() {
            // 'T' for Test
            return "T";
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            // Identity function for testing purposes
            return bytes;
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            // Identity function for testing purposes
            return bytes;
        }
    }

    private RFC1522TestCodec codec;

    @BeforeEach
    void setUp() {
        codec = new RFC1522TestCodec();
    }

    @Test
    @DisplayName("decodeText() should return null when input is null")
    void decodeTextWithNullInputShouldReturnNull() {
        assertNull(codec.decodeText(null), "Decoding a null string should result in null.");
    }

    @Test
    @DisplayName("encodeText() should return null when input is null")
    void encodeTextWithNullInputShouldReturnNull() {
        assertNull(codec.encodeText(null, StandardCharsets.UTF_8), "Encoding a null string should result in null.");
    }

    @DisplayName("decodeText() should throw DecoderException for malformed input")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @ValueSource(strings = {
        "whatever",          // Does not start with '=?' or end with '?='
        "=?",                // Too short, missing required components
        "?=",                // Does not start with '=?'
        "==",                // Does not start with '=?'
        "=??=",              // Missing charset, encoding, and text
        "=?stuff?=",         // Missing encoding type and text
        "=?UTF-8??=",        // Missing encoding type and text
        "=?UTF-8?stuff?=",   // Missing encoding type
        "=?UTF-8?T?stuff",   // Missing closing '?=' suffix
        "=??T?stuff?=",     // Missing charset
        "=?UTF-8??stuff?=",  // Missing encoding type
        "=?UTF-8?W?stuff?="  // Incorrect encoding type (expected 'T', got 'W')
    })
    void decodeTextWithMalformedInputShouldThrowDecoderException(final String malformedText) {
        assertThrows(DecoderException.class,
            () -> codec.decodeText(malformedText),
            () -> "Decoding malformed string \"" + malformedText + "\" should throw DecoderException."
        );
    }
}