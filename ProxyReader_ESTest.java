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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;

/**
 * Tests for the {@link ProxyReader} class.
 * This suite verifies that method calls are correctly proxied to the underlying reader
 * and that hooks and exception handling function as expected.
 */
public class ProxyReaderTest {

    private static final String TEST_STRING = "Hello, World!";

    /**
     * A custom ProxyReader implementation to verify that the beforeRead() and afterRead()
     * hooks are invoked correctly during read operations.
     */
    private static class TestProxyReader extends ProxyReader {
        int beforeReadCount = 0;
        int afterReadCount = 0;
        int lastBeforeReadSize = -1;
        int lastAfterReadSize = -1;

        TestProxyReader(final Reader proxy) {
            super(proxy);
        }

        @Override
        protected void beforeRead(final int n) {
            this.beforeReadCount++;
            this.lastBeforeReadSize = n;
        }

        @Override
        protected void afterRead(final int n) {
            this.afterReadCount++;
            this.lastAfterReadSize = n;
        }
    }

    /**
     * A reader that allows verification of whether its close() method has been called.
     */
    private static class VerifiableCloseReader extends StringReader {
        private boolean isClosed = false;

        VerifiableCloseReader(final String s) {
            super(s);
        }

        @Override
        public void close() {
            super.close();
            this.isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }
    }

    // --- Read Method Tests ---

    @Test
    public void read_whenReadingSingleChar_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            assertEquals('H', proxy.read());
        }
    }

    @Test
    public void read_whenStreamIsEmpty_returnsEOF() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(""))) {
            assertEquals(-1, proxy.read());
        }
    }

    @Test
    public void read_intoCharArray_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            final char[] buffer = new char[5];
            final int charsRead = proxy.read(buffer);
            assertEquals(5, charsRead);
            assertArrayEquals("Hello".toCharArray(), buffer);
        }
    }

    @Test
    public void read_intoCharArrayPortion_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            final char[] buffer = new char[5];
            final int charsRead = proxy.read(buffer, 1, 3);
            assertEquals(3, charsRead);
            assertArrayEquals(new char[]{'\0', 'H', 'e', 'l', '\0'}, buffer);
        }
    }

    @Test
    public void read_intoCharBuffer_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            final CharBuffer buffer = CharBuffer.allocate(5);
            final int charsRead = proxy.read(buffer);
            assertEquals(5, charsRead);
            buffer.flip();
            assertEquals("Hello", buffer.toString());
        }
    }

    @Test
    public void read_withZeroLength_returnsZero() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            assertEquals(0, proxy.read(new char[5], 0, 0));
        }
    }
    
    @Test
    public void read_intoEmptyCharArray_returnsZero() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            assertEquals(0, proxy.read(new char[0]));
        }
    }

    // --- Skip Method Tests ---

    @Test
    public void skip_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            final long skipped = proxy.skip(7);
            assertEquals(7, skipped);
            assertEquals('W', proxy.read());
        }
    }

    @Test
    public void skip_pastEndOfStream_skipsOnlyRemainingChars() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            final long skipped = proxy.skip(100);
            assertEquals(TEST_STRING.length(), skipped);
            assertEquals(-1, proxy.read());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void skip_withNegativeValue_throwsIllegalArgumentException() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            // The Reader.skip() contract specifies an IllegalArgumentException for negative input.
            proxy.skip(-1);
        }
    }

    // --- Mark and Reset Tests ---

    @Test
    public void markAndReset_whenSupported_proxiesCallsCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            assertTrue("StringReader should support mark", proxy.markSupported());
            proxy.skip(6);
            proxy.mark(10);
            assertEquals(',', proxy.read());
            assertEquals(' ', proxy.read());
            proxy.reset();
            assertEquals(',', proxy.read());
        }
    }

    @Test
    public void markSupported_whenNotSupported_returnsFalse() {
        // PipedReader does not support mark/reset
        try (Reader proxy = new TaggedReader(new PipedReader())) {
            assertFalse(proxy.markSupported());
        } catch (IOException e) {
            // Ignore close exception
        }
    }

    @Test(expected = IOException.class)
    public void mark_whenNotSupported_throwsIOException() throws IOException {
        try (Reader proxy = new TaggedReader(new PipedReader())) {
            proxy.mark(1);
        }
    }

    @Test(expected = IOException.class)
    public void reset_whenNotSupported_throwsIOException() throws IOException {
        try (Reader proxy = new TaggedReader(new PipedReader())) {
            proxy.reset();
        }
    }

    // --- State and Lifecycle Tests ---

    @Test
    public void ready_proxiesCallCorrectly() throws IOException {
        try (Reader proxy = new TaggedReader(new StringReader(TEST_STRING))) {
            assertTrue(proxy.ready());
        }
    }

    @Test
    public void close_proxiesCallCorrectly() throws IOException {
        final VerifiableCloseReader underlyingReader = new VerifiableCloseReader(TEST_STRING);
        final Reader proxy = new TaggedReader(underlyingReader);

        assertFalse("Reader should not be closed initially", underlyingReader.isClosed());
        proxy.close();
        assertTrue("Reader should be closed after proxy is closed", underlyingReader.isClosed());
    }

    // --- Hook Method Tests ---

    @Test
    public void readMethods_invokeBeforeAndAfterReadHooks() throws IOException {
        final TestProxyReader proxy = new TestProxyReader(new StringReader("abc"));

        // Test read()
        final int charRead = proxy.read();
        assertEquals('a', charRead);
        assertEquals(1, proxy.beforeReadCount);
        assertEquals(1, proxy.afterReadCount);
        assertEquals(1, proxy.lastBeforeReadSize); // As per Reader.read() javadoc
        assertEquals(1, proxy.lastAfterReadSize);

        // Test read(char[])
        final char[] buffer = new char[2];
        final int charsRead = proxy.read(buffer);
        assertEquals(2, charsRead);
        assertEquals(2, proxy.beforeReadCount);
        assertEquals(2, proxy.afterReadCount);
        assertEquals(2, proxy.lastBeforeReadSize);
        assertEquals(2, proxy.lastAfterReadSize);

        // Test read at EOF
        proxy.read(buffer); // Read past end
        assertEquals(-1, proxy.read());
        assertEquals(4, proxy.beforeReadCount);
        assertEquals(4, proxy.afterReadCount);
        assertEquals(-1, proxy.lastAfterReadSize);
    }

    // --- Exception Handling Tests ---

    @Test(expected = IOException.class)
    public void handleIOException_rethrowsExceptionByDefault() throws IOException {
        final IOException testException = new IOException("Test Exception");
        // Use a simple anonymous subclass to test the protected method
        final ProxyReader proxy = new ProxyReader(new StringReader("")) {
            public void test() throws IOException {
                handleIOException(testException);
            }
        };
        // This is a bit of a workaround to call a protected method
        ((TestProxyReader) (Object) proxy).test();
    }

    @Test(expected = IOException.class)
    public void read_whenUnderlyingReaderFails_throwsIOException() throws IOException {
        // An unconnected PipedReader will throw an IOException on read
        try (Reader proxy = new TaggedReader(new PipedReader())) {
            proxy.read();
        }
    }
}