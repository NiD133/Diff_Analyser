/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link BoundedReader} class.
 */
public class BoundedReaderTest {

    private static final String TEST_DATA = "ABCDEFG";

    // =================================================================
    // read() Tests
    // =================================================================

    @Test
    public void testReadSingleCharWithinBounds() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 3);
        assertEquals('A', boundedReader.read());
        assertEquals('B', boundedReader.read());
        assertEquals('C', boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadPastBoundaryReturnsEOF() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 2);
        assertEquals('A', boundedReader.read());
        assertEquals('B', boundedReader.read());
        assertEquals("Should return EOF when boundary is reached", -1, boundedReader.read());
        assertEquals("Should continue to return EOF", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadWithZeroLimitReturnsEOF() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 0);
        assertEquals("A reader with a zero limit should immediately return EOF", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadPastUnderlyingStreamEndReturnsEOF() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader("ABC"), 5);
        assertEquals('A', boundedReader.read());
        assertEquals('B', boundedReader.read());
        assertEquals('C', boundedReader.read());
        assertEquals("Should return EOF when underlying stream ends", -1, boundedReader.read());
        boundedReader.close();
    }

    // =================================================================
    // read(char[], int, int) Tests
    // =================================================================

    @Test
    public void testReadIntoCharArrayStopsAtBoundary() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 3);
        char[] buffer = new char[5];

        int charsRead = boundedReader.read(buffer, 0, buffer.length);

        assertEquals("Should read up to the boundary", 3, charsRead);
        assertArrayEquals(new char[]{'A', 'B', 'C', '\0', '\0'}, buffer);
        assertEquals("Subsequent read should be EOF", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadIntoCharArrayStopsAtUnderlyingStreamEnd() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader("AB"), 5);
        char[] buffer = new char[5];

        int charsRead = boundedReader.read(buffer, 0, buffer.length);

        assertEquals("Should read until the underlying stream ends", 2, charsRead);
        assertArrayEquals(new char[]{'A', 'B', '\0', '\0', '\0'}, buffer);
        assertEquals("Subsequent read should be EOF", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadIntoCharArrayWithOffset() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 5);
        char[] buffer = new char[5];

        int charsRead = boundedReader.read(buffer, 2, 3);

        assertEquals("Should read the specified number of characters", 3, charsRead);
        assertArrayEquals(new char[]{'\0', '\0', 'A', 'B', 'C'}, buffer);
        boundedReader.close();
    }

    @Test
    public void testReadIntoCharArrayWithZeroLengthReturnsZero() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 5);
        assertEquals(0, boundedReader.read(new char[5], 0, 0));
        boundedReader.close();
    }

    // =================================================================
    // mark() and reset() Tests
    // =================================================================

    @Test
    public void testMarkAndReset() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 10);
        boundedReader.mark(5);

        boundedReader.read(); // A
        boundedReader.read(); // B

        boundedReader.reset();
        assertEquals("Should have reset to the mark", 'A', boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testResetDoesNotExceedOriginalBoundary() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 3);
        boundedReader.mark(5); // Mark is supported by StringReader

        assertEquals('A', boundedReader.read());
        boundedReader.reset();

        assertEquals('A', boundedReader.read());
        assertEquals('B', boundedReader.read());
        assertEquals('C', boundedReader.read());
        assertEquals("Should still hit boundary after reset", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test(expected = IOException.class)
    public void testResetWithoutMarkThrowsIOException() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 10)) {
            boundedReader.reset(); // Should throw "Resetting to invalid mark"
        }
    }

    // =================================================================
    // skip() Tests
    // =================================================================

    @Test
    public void testSkipStopsAtBoundary() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 3);
        long skipped = boundedReader.skip(10); // Attempt to skip past the boundary

        assertEquals("Should only skip up to the boundary", 3, skipped);
        assertEquals("Reader should be at EOF after skipping to boundary", -1, boundedReader.read());
        boundedReader.close();
    }

    // =================================================================
    // close() Tests
    // =================================================================

    /** A helper reader to verify that the close() method is propagated. */
    private static class CloseSensingReader extends StringReader {
        private boolean closed = false;
        public CloseSensingReader(String s) {
            super(s);
        }
        @Override
        public void close() {
            this.closed = true;
            super.close();
        }
        public boolean isClosed() {
            return closed;
        }
    }

    @Test
    public void testClosePropagatesToUnderlyingReader() throws IOException {
        CloseSensingReader underlyingReader = new CloseSensingReader(TEST_DATA);
        BoundedReader boundedReader = new BoundedReader(underlyingReader, 10);

        boundedReader.close();

        assertTrue("Close should be called on the underlying reader", underlyingReader.isClosed());
    }

    // =================================================================
    // Exception and Edge Case Tests
    // =================================================================

    @Test(expected = IOException.class)
    public void testReadOnClosedReaderThrowsIOException() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), 10);
        boundedReader.close();
        boundedReader.read(); // Should throw "Stream closed"
    }

    @Test
    public void testConstructorWithNegativeLimitMeansNoLimit() throws IOException {
        // A negative limit indicates that there is no boundary.
        BoundedReader boundedReader = new BoundedReader(new StringReader(TEST_DATA), -1);
        char[] buffer = new char[TEST_DATA.length()];
        
        int charsRead = boundedReader.read(buffer);
        
        assertEquals("Should read the entire underlying stream", TEST_DATA.length(), charsRead);
        assertArrayEquals(TEST_DATA.toCharArray(), buffer);
        assertEquals("Should be at the end of the stream", -1, boundedReader.read());
        boundedReader.close();
    }

    @Test
    public void testReadWithNullUnderlyingReaderThrowsNPE() throws IOException {
        BoundedReader boundedReader = new BoundedReader(null, 10);
        try {
            boundedReader.read();
            fail("Should have thrown a NullPointerException");
        } catch (final NullPointerException e) {
            // Expected
        } finally {
            boundedReader.close();
        }
    }
}