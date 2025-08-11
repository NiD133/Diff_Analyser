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

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ProxyReader} class.
 */
class ProxyReaderTest {

    /**
     * A custom implementation of NullReader that handles null inputs gracefully.
     */
    private static final class CustomNullReader extends NullReader {
        CustomNullReader(final int length) {
            super(length);
        }

        @Override
        public int read(final char[] chars) throws IOException {
            // Return 0 if the input array is null, otherwise delegate to the superclass.
            return chars == null ? 0 : super.read(chars);
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            // Return 0 if the input CharBuffer is null, otherwise delegate to the superclass.
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * A simple implementation of ProxyReader for testing purposes.
     */
    private static final class TestProxyReader extends ProxyReader {
        TestProxyReader(final Reader delegate) {
            super(delegate);
        }
    }

    /**
     * Test reading from a ProxyReader with a null character array.
     */
    @Test
    void testReadWithNullCharArray() throws Exception {
        try (ProxyReader proxyReader = new TestProxyReader(new CustomNullReader(0))) {
            // Test reading with a null character array
            proxyReader.read((char[]) null);
            proxyReader.read(null, 0, 0);
        }
    }

    /**
     * Test reading from a ProxyReader with a null CharBuffer.
     */
    @Test
    void testReadWithNullCharBuffer() throws Exception {
        try (ProxyReader proxyReader = new TestProxyReader(new CustomNullReader(0))) {
            // Test reading with a null CharBuffer
            proxyReader.read((CharBuffer) null);
        }
    }
}