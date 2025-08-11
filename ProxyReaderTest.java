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
package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link ProxyReader} to ensure it properly delegates method calls
 * to the underlying Reader and handles null parameters correctly.
 */
class ProxyReaderTest {

    /**
     * A custom NullReader that handles null parameters gracefully.
     * This is used to test ProxyReader's delegation behavior with null inputs.
     */
    private static final class NullParameterHandlingReader extends NullReader {
        
        NullParameterHandlingReader(final int length) {
            super(length);
        }

        /**
         * Returns 0 when char array is null, otherwise delegates to parent.
         */
        @Override
        public int read(final char[] chars) throws IOException {
            return chars == null ? 0 : super.read(chars);
        }

        /**
         * Returns 0 when CharBuffer is null, otherwise delegates to parent.
         */
        @Override
        public int read(final CharBuffer target) throws IOException {
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * Concrete implementation of ProxyReader for testing purposes.
     * Simply delegates all calls to the underlying Reader without modification.
     */
    private static final class TestableProxyReader extends ProxyReader {
        
        TestableProxyReader(final Reader underlyingReader) {
            super(underlyingReader);
        }
    }

    /**
     * Tests that ProxyReader correctly delegates read operations with null char arrays
     * to the underlying Reader. This verifies that the proxy doesn't interfere with
     * null parameter handling.
     */
    @Test
    void shouldDelegateReadOperationsWithNullCharArrayToUnderlyingReader() throws Exception {
        // Given: A ProxyReader wrapping a reader that handles null char arrays
        final Reader underlyingReader = new NullParameterHandlingReader(0);
        
        try (ProxyReader proxyReader = new TestableProxyReader(underlyingReader)) {
            // When: Reading with null char array parameters
            int resultFromNullArray = proxyReader.read((char[]) null);
            int resultFromNullArrayWithOffsets = proxyReader.read(null, 0, 0);
            
            // Then: The underlying reader's behavior is preserved (returns 0 for null)
            assertEquals(0, resultFromNullArray, 
                "ProxyReader should delegate null char array handling to underlying reader");
            assertEquals(0, resultFromNullArrayWithOffsets, 
                "ProxyReader should delegate null char array with offsets handling to underlying reader");
        }
    }

    /**
     * Tests that ProxyReader correctly delegates read operations with null CharBuffer
     * to the underlying Reader. This ensures the proxy pattern works correctly
     * for all read method variants.
     */
    @Test
    void shouldDelegateReadOperationsWithNullCharBufferToUnderlyingReader() throws Exception {
        // Given: A ProxyReader wrapping a reader that handles null CharBuffers
        final Reader underlyingReader = new NullParameterHandlingReader(0);
        
        try (ProxyReader proxyReader = new TestableProxyReader(underlyingReader)) {
            // When: Reading with null CharBuffer
            int result = proxyReader.read((CharBuffer) null);
            
            // Then: The underlying reader's behavior is preserved (returns 0 for null)
            assertEquals(0, result, 
                "ProxyReader should delegate null CharBuffer handling to underlying reader");
        }
    }
}