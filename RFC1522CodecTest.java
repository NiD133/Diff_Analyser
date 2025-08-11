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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for RFC1522Codec behavior.
 * Uses a pass-through codec stub so assertions focus on the RFC 1522 envelope parsing (not the inner encoding).
 */
class RFC1522CodecTest {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Minimal pass-through implementation:
     * - Uses UTF-8 as the default charset.
     * - Returns the bytes unchanged for doEncoding/doDecoding.
     * - Reports a dummy encoding token ("T").
     */
    private static final class PassThroughRFC1522Codec extends RFC1522Codec {
        PassThroughRFC1522Codec() {
            super(DEFAULT_CHARSET);
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

    private final RFC1522Codec codec = new PassThroughRFC1522Codec();

    private void assertDecodeFails(final String text) {
        assertThrows(DecoderException.class, () -> codec.decodeText(text));
    }

    @DisplayName("decodeText: rejects malformed encoded-words per RFC 1522")
    @ParameterizedTest(name = "[{index}] \"{0}\"")
    @ValueSource(strings = {
        "whatever",
        "=?",
        "?=",
        "==",
        "=??=",
        "=?stuff?=",
        "=?UTF-8??=",
        "=?UTF-8?stuff?=",
        "=?UTF-8?T?stuff",
        "=??T?stuff?=",
        "=?UTF-8??stuff?=",
        "=?UTF-8?W?stuff?="
    })
    void decodeText_rejectsMalformedEncodedWords(final String invalidEncodedWord) {
        assertDecodeFails(invalidEncodedWord);
    }

    @Test
    void decodeText_returnsNull_whenInputIsNull() throws Exception {
        assertNull(codec.decodeText(null));
    }

    @Test
    void encodeText_returnsNull_whenInputIsNull_charsetName() throws Exception {
        assertNull(codec.encodeText(null, CharEncoding.UTF_8));
    }

    @Test
    void encodeText_returnsNull_whenInputIsNull_charsetObject() throws Exception {
        assertNull(codec.encodeText(null, DEFAULT_CHARSET));
    }
}