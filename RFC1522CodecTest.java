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
 * Test cases for RFC1522Codec - verifies proper handling of invalid input and null values.
 * 
 * RFC 1522 defines the format for encoded words in email headers as:
 * =?charset?encoding?encoded-text?=
 */
class RFC1522CodecTest {

    /**
     * Test implementation of RFC1522Codec that performs no actual encoding/decoding.
     * This allows us to test the base RFC1522Codec functionality without the complexity
     * of specific encoding algorithms like Base64 or Quoted-Printable.
     */
    static class TestableRFC1522Codec extends RFC1522Codec {

        TestableRFC1522Codec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            // Pass-through implementation for testing base class behavior
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            // Pass-through implementation for testing base class behavior
            return bytes;
        }

        @Override
        protected String getEncoding() {
            // Return "T" as a test encoding identifier
            return "T";
        }
    }

    private final TestableRFC1522Codec codec = new TestableRFC1522Codec();

    /**
     * Helper method to verify that decoding the given string throws a DecoderException.
     * This reduces code duplication in the invalid input tests.
     */
    private void assertDecodingThrowsException(final String invalidInput) {
        assertThrows(DecoderException.class, 
                    () -> codec.decodeText(invalidInput),
                    "Expected DecoderException for invalid input: " + invalidInput);
    }

    /**
     * Tests that various malformed RFC1522 encoded strings are properly rejected.
     * 
     * Valid RFC1522 format is: =?charset?encoding?encoded-text?=
     * These tests verify that incomplete, malformed, or invalid formats throw DecoderException.
     */
    @Test
    void testDecodeInvalidFormats() throws Exception {
        // Test completely invalid strings
        assertDecodingThrowsException("whatever");  // No RFC1522 markers at all
        
        // Test incomplete RFC1522 markers
        assertDecodingThrowsException("=?");        // Only start marker
        assertDecodingThrowsException("?=");        // Only end marker
        assertDecodingThrowsException("==");        // Wrong markers
        
        // Test malformed RFC1522 structure
        assertDecodingThrowsException("=??=");      // Missing required fields
        assertDecodingThrowsException("=?stuff?="); // Only one field instead of three
        
        // Test missing required components
        assertDecodingThrowsException("=?UTF-8??=");        // Missing encoding and text
        assertDecodingThrowsException("=?UTF-8?stuff?=");   // Invalid structure
        assertDecodingThrowsException("=?UTF-8?T?stuff");   // Missing end marker
        
        // Test empty required fields
        assertDecodingThrowsException("=??T?stuff?=");      // Empty charset
        assertDecodingThrowsException("=?UTF-8??stuff?=");  // Empty encoding
        
        // Test unsupported encoding
        assertDecodingThrowsException("=?UTF-8?W?stuff?="); // 'W' is not a valid encoding (should be B, Q, or T for test)
    }

    /**
     * Tests that null inputs are handled gracefully by returning null rather than throwing exceptions.
     * This follows the common pattern in Apache Commons Codec of treating null as a no-op.
     */
    @Test
    void testNullInputHandling() throws Exception {
        // Verify that null inputs return null rather than throwing exceptions
        assertNull(codec.decodeText(null), 
                  "decodeText should return null for null input");
        
        assertNull(codec.encodeText(null, CharEncoding.UTF_8), 
                  "encodeText should return null for null input");
    }
}