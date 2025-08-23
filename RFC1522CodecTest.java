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

/**
 * Test suite for the RFC 1522 compliant codec.
 */
class RFC1522CodecTest {

    /**
     * A test implementation of RFC1522Codec for testing purposes.
     */
    static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            // No actual decoding logic for test purposes
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            // No actual encoding logic for test purposes
            return bytes;
        }

        @Override
        protected String getEncoding() {
            return "T";
        }
    }

    /**
     * Helper method to assert that decoding a given string throws a DecoderException.
     *
     * @param input the input string to decode
     */
    private void assertDecoderExceptionThrown(final String input) {
        assertThrows(DecoderException.class, () -> new RFC1522TestCodec().decodeText(input));
    }

    /**
     * Tests decoding of various invalid input strings.
     */
    @Test
    void testDecodeInvalidInputs() throws Exception {
        // Invalid encoded strings that should throw DecoderException
        String[] invalidInputs = {
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
        };

        for (String input : invalidInputs) {
            assertDecoderExceptionThrown(input);
        }
    }

    /**
     * Tests encoding and decoding with null input.
     */
    @Test
    void testNullInputHandling() throws Exception {
        final RFC1522TestCodec testCodec = new RFC1522TestCodec();
        
        // Ensure null input returns null without exceptions
        assertNull(testCodec.decodeText(null));
        assertNull(testCodec.encodeText(null, CharEncoding.UTF_8));
    }
}