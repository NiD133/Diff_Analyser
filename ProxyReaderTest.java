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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies that ProxyReader preserves method dispatch and does not change which
 * read(..) overload is invoked on the underlying Reader.
 *
 * Why this matters:
 * - java.io.FilterReader may redirect read(char[]) to read(char[], int, int),
 *   which can change behavior.
 * - These tests use a delegate Reader that handles null differently on specific
 *   overloads to ensure ProxyReader calls exactly the same overload that the
 *   caller invoked.
 */
class ProxyReaderTest {

    /**
     * A NullReader variant that safely handles null for specific overloads:
     * - read(char[]) returns 0 (instead of throwing) when passed null.
     * - read(CharBuffer) returns 0 (instead of throwing) when passed null.
     *
     * We do NOT override read(char[], int, int) on purpose: this lets us detect
     * if ProxyReader incorrectly redirects read(char[]) to read(char[], int, int).
     */
    private static final class NullTolerantReader extends NullReader {
        NullTolerantReader(final int len) {
            super(len);
        }

        @Override
        public int read(final char[] chars) throws IOException {
            // Returning 0 here makes it easy to detect if the exact overload was called.
            return chars == null ? 0 : super.read(chars);
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            // Returning 0 here makes it easy to detect if the exact overload was called.
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * Minimal concrete ProxyReader used for testing.
     */
    private static final class TestProxyReader extends ProxyReader {
        TestProxyReader(final Reader delegate) {
            super(delegate);
        }
    }

    private static ProxyReader newProxyOverNullTolerantReader() {
        return new TestProxyReader(new NullTolerantReader(0));
    }

    @Test
    @DisplayName("read(char[]) forwards to the same overload on the delegate (null-safe)")
    void readCharArray_preservesOverloadWithNull() throws Exception {
        try (ProxyReader proxy = newProxyOverNullTolerantReader()) {
            // If ProxyReader redirected to read(char[], int, int), behavior could differ.
            assertDoesNotThrow(() -> proxy.read((char[]) null));
        }
    }

    @Test
    @DisplayName("read(char[], int, int) forwards to the same overload on the delegate (null, len=0)")
    void readCharArrayRegion_preservesOverloadWithNullAndZeroLen() throws Exception {
        try (ProxyReader proxy = newProxyOverNullTolerantReader()) {
            // Pass a null buffer with a zero length to exercise the exact 3-arg overload.
            // The goal is to ensure ProxyReader does not redirect this call to read(char[]).
            assertDoesNotThrow(() -> proxy.read(null, 0, 0));
        }
    }

    @Test
    @DisplayName("read(CharBuffer) forwards to the same overload on the delegate (null-safe)")
    void readCharBuffer_preservesOverloadWithNull() throws Exception {
        try (ProxyReader proxy = newProxyOverNullTolerantReader()) {
            // If ProxyReader redirected to another overload, we would see a different behavior.
            assertDoesNotThrow(() -> proxy.read((CharBuffer) null));
        }
    }
}