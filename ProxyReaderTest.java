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
 * Tests the {@link ProxyReader} to ensure it correctly delegates method calls
 * to the underlying reader.
 */
class ProxyReaderTest {

    /**
     * A test-specific stub that extends {@link NullReader} to provide non-standard
     * behavior for testing purposes.
     * <p>
     * This reader is designed to return {@code 0} when a {@code null} buffer is
     * passed to its read methods, instead of throwing a {@link NullPointerException}.
     * This allows us to verify that {@link ProxyReader} correctly delegates calls
     * with null arguments, rather than handling them itself.
     * </p>
     */
    private static final class CustomNullReader extends NullReader {
        CustomNullReader(final int len) {
            super(len);
        }

        @Override
        public int read(final char[] chars) throws IOException {
            // Return 0 for a null array, otherwise delegate to standard NullReader behavior.
            return chars == null ? 0 : super.read(chars);
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            // Return 0 for a null buffer, otherwise delegate to standard NullReader behavior.
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * A minimal concrete implementation of the abstract {@link ProxyReader} for testing.
     */
    private static final class ProxyReaderImpl extends ProxyReader {
        ProxyReaderImpl(final Reader proxy) {
            super(proxy);
        }
    }

    @Test
    void readWithNullCharArrayShouldDelegateCall() throws IOException {
        // Create a custom reader that returns 0 for a null char array.
        final Reader customReader = new CustomNullReader(0);
        try (final ProxyReader proxy = new ProxyReaderImpl(customReader)) {
            // Verify that the proxy delegates the call with the null array
            // and returns the value from our custom reader (0), instead of
            // throwing a NullPointerException itself.
            assertEquals(0, proxy.read((char[]) null), "read(char[]) should be delegated");
            assertEquals(0, proxy.read(null, 0, 0), "read(char[], int, int) should be delegated");
        }
    }

    @Test
    void readWithNullCharBufferShouldDelegateCall() throws IOException {
        // Create a custom reader that returns 0 for a null CharBuffer.
        final Reader customReader = new CustomNullReader(0);
        try (final ProxyReader proxy = new ProxyReaderImpl(customReader)) {
            // Verify that the proxy delegates the call with the null buffer
            // and returns the value from our custom reader (0), instead of
            // throwing a NullPointerException itself.
            assertEquals(0, proxy.read((CharBuffer) null), "read(CharBuffer) should be delegated");
        }
    }
}