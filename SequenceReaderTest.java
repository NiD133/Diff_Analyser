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

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link SequenceReader}.
 * 
 * SequenceReader combines multiple Reader instances into a single sequential stream,
 * similar to how SequenceInputStream works for InputStreams.
 */
class SequenceReaderTest {

    // Test data constants
    private static final String FIRST_CONTENT = "Foo";
    private static final String SECOND_CONTENT = "Bar";
    private static final String COMBINED_CONTENT = FIRST_CONTENT + SECOND_CONTENT; // "FooBar"
    private static final char NULL_CHAR = 0;
    private static final int END_OF_STREAM = -1;

    /**
     * Custom Reader implementation for testing resource management.
     * This reader automatically closes itself after the first read operation.
     */
    private static class AutoClosingReader extends Reader {
        private boolean isClosed = false;

        @Override
        public int read(final char[] buffer, final int offset, final int length) throws IOException {
            verifyNotClosed();
            close(); // Auto-close after first read
            return EOF;
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

        private void verifyNotClosed() throws IOException {
            if (isClosed) {
                throw new IOException("Reader is already closed");
            }
        }
    }

    /**
     * Custom Reader that returns a single character before reaching EOF.
     * Used for testing proper resource cleanup.
     */
    private static class SingleCharacterReader extends AutoClosingReader {
        private static final char[] CONTENT = {'A'};
        private boolean hasBeenRead = false;

        @Override
        public int read(final char[] buffer, final int offset, final int length) throws IOException {
            verifyNotClosed();
            validateReadParameters(buffer, offset, length);

            if (hasBeenRead) {
                return EOF;
            }

            buffer[offset] = CONTENT[0];
            hasBeenRead = true;
            return 1;
        }

        private void validateReadParameters(final char[] buffer, final int offset, final int length) {
            if (offset < 0) {
                throw new IndexOutOfBoundsException("Offset cannot be negative");
            }
            if (length < 0) {
                throw new IndexOutOfBoundsException("Length cannot be negative");
            }
            if (length > buffer.length - offset) {
                throw new IndexOutOfBoundsException("Length exceeds available buffer space");
            }
        }
    }

    // Helper methods for common test operations

    /**
     * Verifies that the actual character array matches the expected array.
     */
    private void assertCharArrayEquals(final char[] expected, final char[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 
                String.format("Character mismatch at position %d", i));
        }
    }

    /**
     * Reads characters one by one and verifies they match the expected string.
     */
    private void assertReaderContains(final Reader reader, final String expectedContent) throws IOException {
        for (int i = 0; i < expectedContent.length(); i++) {
            char expectedChar = expectedContent.charAt(i);
            int actualChar = reader.read();
            assertEquals(expectedChar, (char) actualChar, 
                String.format("Character mismatch at position %d in '%s'", i, expectedContent));
        }
    }

    /**
     * Verifies that the reader has reached end-of-stream by attempting multiple reads.
     */
    private void assertReaderAtEndOfStream(final Reader reader) throws IOException {
        for (int attempt = 0; attempt < 10; attempt++) {
            assertEquals(END_OF_STREAM, reader.read(), 
                "Reader should return EOF consistently");
        }
    }

    // Basic functionality tests

    @Test
    void testSingleReaderSequence() throws IOException {
        try (Reader sequenceReader = new SequenceReader(new StringReader(COMBINED_CONTENT))) {
            assertReaderContains(sequenceReader, FIRST_CONTENT);
            assertReaderContains(sequenceReader, SECOND_CONTENT);
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    @Test
    void testMultipleReadersSequence() throws IOException {
        try (Reader sequenceReader = new SequenceReader(
                new StringReader(FIRST_CONTENT), 
                new StringReader(SECOND_CONTENT))) {
            
            // Verify individual character reading
            assertEquals('F', sequenceReader.read());
            assertEquals('o', sequenceReader.read());
            assertEquals('o', sequenceReader.read());
            assertEquals('B', sequenceReader.read());
            assertEquals('a', sequenceReader.read());
            assertEquals('r', sequenceReader.read());
            
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    // Array-based reading tests

    @Test
    void testReadIntoCharacterArray() throws IOException {
        try (Reader sequenceReader = new SequenceReader(
                new StringReader(FIRST_CONTENT), 
                new StringReader(SECOND_CONTENT))) {
            
            // Read first 2 characters
            char[] buffer = new char[2];
            assertEquals(2, sequenceReader.read(buffer));
            assertCharArrayEquals(new char[]{'F', 'o'}, buffer);
            
            // Read next 3 characters (crossing reader boundary)
            buffer = new char[3];
            assertEquals(3, sequenceReader.read(buffer));
            assertCharArrayEquals(new char[]{'o', 'B', 'a'}, buffer);
            
            // Read remaining character
            buffer = new char[3];
            assertEquals(1, sequenceReader.read(buffer));
            assertCharArrayEquals(new char[]{'r', NULL_CHAR, NULL_CHAR}, buffer);
            
            // Verify end of stream
            assertEquals(END_OF_STREAM, sequenceReader.read(buffer));
        }
    }

    @Test
    void testReadIntoCharacterArrayWithOffsetAndLength() throws IOException {
        final char[] buffer = new char[10];
        
        try (Reader sequenceReader = new SequenceReader(
                new StringReader(FIRST_CONTENT), 
                new StringReader(SECOND_CONTENT))) {
            
            // Read 3 characters starting at offset 3
            assertEquals(3, sequenceReader.read(buffer, 3, 3));
            assertCharArrayEquals(new char[]{NULL_CHAR, NULL_CHAR, NULL_CHAR, 'F', 'o', 'o'}, buffer);
            
            // Read 3 characters starting at offset 0
            assertEquals(3, sequenceReader.read(buffer, 0, 3));
            assertCharArrayEquals(new char[]{'B', 'a', 'r', 'F', 'o', 'o', NULL_CHAR}, buffer);
            
            // Verify end of stream
            assertEquals(END_OF_STREAM, sequenceReader.read(buffer));
            
            // Test boundary conditions
            assertThrows(IndexOutOfBoundsException.class, 
                () -> sequenceReader.read(buffer, 10, 10), 
                "Should throw exception for invalid offset");
            assertThrows(NullPointerException.class, 
                () -> sequenceReader.read(null, 0, 10), 
                "Should throw exception for null buffer");
        }
    }

    // Collection-based constructor tests

    @Test
    void testConstructorWithCollection() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        
        try (Reader sequenceReader = new SequenceReader(readers)) {
            assertEquals('F', sequenceReader.read());
            assertEquals('B', sequenceReader.read());
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    @Test
    void testConstructorWithList() throws IOException {
        final List<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        
        try (Reader sequenceReader = new SequenceReader(readers)) {
            assertEquals('F', sequenceReader.read());
            assertEquals('B', sequenceReader.read());
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    @Test
    void testConstructorWithIterable() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        final Iterable<Reader> iterable = readers;
        
        try (Reader sequenceReader = new SequenceReader(iterable)) {
            assertEquals('F', sequenceReader.read());
            assertEquals('B', sequenceReader.read());
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    // Edge case tests

    @Test
    void testEmptyReaders() throws IOException {
        try (Reader sequenceReader = new SequenceReader(
                new StringReader(StringUtils.EMPTY),
                new StringReader(StringUtils.EMPTY), 
                new StringReader(StringUtils.EMPTY))) {
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    @Test
    void testSingleCharacterReaders() throws IOException {
        try (Reader sequenceReader = new SequenceReader(
                new StringReader("0"),
                new StringReader("1"),
                new StringReader("2"),
                new StringReader("3"))) {
            
            assertEquals('0', sequenceReader.read());
            assertEquals('1', sequenceReader.read());
            assertEquals('2', sequenceReader.read());
            assertEquals('3', sequenceReader.read());
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    // Resource management tests

    @Test
    void testCloseOperation() throws IOException {
        final Reader sequenceReader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT));
        assertReaderContains(sequenceReader, FIRST_CONTENT);
        
        sequenceReader.close();
        assertReaderAtEndOfStream(sequenceReader);
    }

    @Test
    void testAutoCloseWithTryWithResources() throws IOException {
        try (Reader sequenceReader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT))) {
            assertReaderContains(sequenceReader, FIRST_CONTENT);
            sequenceReader.close();
            assertReaderAtEndOfStream(sequenceReader);
        }
    }

    @Test
    void testReadFromClosedReader() throws IOException {
        @SuppressWarnings("resource")
        final Reader sequenceReader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT));
        sequenceReader.close();
        assertReaderAtEndOfStream(sequenceReader);
    }

    @Test
    void testUnderlyingReadersAreClosed() throws IOException {
        final AutoClosingReader firstReader = new AutoClosingReader();
        final SingleCharacterReader secondReader = new SingleCharacterReader();

        try (SequenceReader sequenceReader = new SequenceReader(secondReader, firstReader)) {
            assertEquals('A', sequenceReader.read());
            assertEquals(EOF, sequenceReader.read());
        }

        // Verify both readers were properly closed
        assertTrue(firstReader.isClosed(), "First reader should be closed");
        assertTrue(secondReader.isClosed(), "Second reader should be closed");
    }

    // Feature support tests

    @Test
    void testMarkNotSupported() throws Exception {
        try (Reader sequenceReader = new SequenceReader()) {
            assertFalse(sequenceReader.markSupported(), 
                "SequenceReader should not support mark operations");
        }
    }

    @Test
    void testSkipOperation() throws IOException {
        try (Reader sequenceReader = new SequenceReader(
                new StringReader(FIRST_CONTENT), 
                new StringReader(SECOND_CONTENT))) {
            
            // Skip the first 3 characters ("Foo")
            assertEquals(3, sequenceReader.skip(3));
            
            // Read the remaining content ("Bar")
            assertReaderContains(sequenceReader, SECOND_CONTENT);
            
            // Attempt to skip when at end of stream
            assertEquals(0, sequenceReader.skip(3));
        }
    }
}