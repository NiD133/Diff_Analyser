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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    /**
     * A test-specific StringReader that tracks if it has been closed.
     */
    private static class CloseableStringReader extends StringReader {
        private boolean closed;

        CloseableStringReader(final String s) {
            super(s);
        }

        @Override
        public void close() {
            super.close();
            this.closed = true;
        }

        boolean isClosed() {
            return closed;
        }
    }

    /**
     * Asserts that the reader's content matches the expected string.
     *
     * @param reader   The reader to test.
     * @param expected The expected string content.
     * @throws IOException If an I/O error occurs.
     */
    private void assertReadsExpectedString(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), (char) reader.read(), "Character at index " + i + " should match");
        }
    }

    @Test
    @DisplayName("A try-with-resources statement should close the reader automatically")
    void tryWithResourcesClosesReaderAutomatically() throws IOException {
        Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));
        try (reader) {
            assertReadsExpectedString(reader, "Foo");
        }
        // After the try-with-resources block, the reader should be closed and return EOF.
        assertEquals(EOF, reader.read(), "Reader should be at EOF after close");
    }

    @Test
    @DisplayName("Calling close() should prevent further reads")
    void closePreventsFurtherReads() throws IOException {
        // Arrange
        final Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));

        // Act
        assertReadsExpectedString(reader, "Foo");
        reader.close();

        // Assert
        assertEquals(EOF, reader.read(), "Reader should be at EOF after close");
    }

    @Test
    @DisplayName("Closing the SequenceReader should close all underlying readers")
    void closeClosesAllUnderlyingReaders() throws IOException {
        // Arrange
        final CloseableStringReader reader1 = new CloseableStringReader("A");
        final CloseableStringReader reader2 = new CloseableStringReader("B");
        assertFalse(reader1.isClosed());
        assertFalse(reader2.isClosed());

        // Act
        try (Reader sequenceReader = new SequenceReader(reader1, reader2)) {
            while (sequenceReader.read() != EOF) {
                // consume all content
            }
        }

        // Assert
        assertTrue(reader1.isClosed(), "Reader 1 should be closed");
        assertTrue(reader2.isClosed(), "Reader 2 should be closed");
    }

    @Test
    @DisplayName("markSupported() should return false")
    void markIsUnsupported() throws IOException {
        try (Reader reader = new SequenceReader()) {
            assertFalse(reader.markSupported());
        }
    }

    @Test
    @DisplayName("read() should return characters sequentially from all readers")
    void readSequentiallyFromMultipleReaders() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            assertReadsExpectedString(reader, "FooBar");
            assertEquals(EOF, reader.read(), "Should be at the end of the stream");
        }
    }

    @Test
    @DisplayName("read(char[]) should fill the array, crossing reader boundaries if necessary")
    void readIntoCharArrayFillsAcrossReaderBoundaries() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            // Read first part
            char[] buffer1 = new char[2];
            assertEquals(2, reader.read(buffer1));
            assertArrayEquals(new char[]{'F', 'o'}, buffer1);

            // Read across boundary
            char[] buffer2 = new char[3];
            assertEquals(3, reader.read(buffer2));
            assertArrayEquals(new char[]{'o', 'B', 'a'}, buffer2);

            // Read final part
            char[] buffer3 = new char[3];
            assertEquals(1, reader.read(buffer3));
            assertArrayEquals(new char[]{'r', '\0', '\0'}, buffer3);

            // Assert EOF
            assertEquals(EOF, reader.read(buffer3));
        }
    }

    @Test
    @DisplayName("read(char[], int, int) should fill the buffer portion correctly")
    void readIntoCharArrayPortionFillsBufferCorrectly() throws IOException {
        // Arrange
        final char[] buffer = new char[10];
        final String content1 = "Foo";
        final String content2 = "Bar";
        try (Reader reader = new SequenceReader(new StringReader(content1), new StringReader(content2))) {
            // Act 1: Read "Foo" into the middle of the buffer
            assertEquals(3, reader.read(buffer, 3, 3));

            // Assert 1: Check if "Foo" is in the correct place
            final char[] expected1 = new char[10];
            System.arraycopy(content1.toCharArray(), 0, expected1, 3, 3);
            assertArrayEquals(expected1, buffer);

            // Act 2: Read "Bar" into the start of the buffer
            assertEquals(3, reader.read(buffer, 0, 3));

            // Assert 2: Check if "Bar" is at the start and "Foo" is still there
            final char[] expected2 = new char[10];
            System.arraycopy(content1.toCharArray(), 0, expected2, 3, 3); // "Foo"
            System.arraycopy(content2.toCharArray(), 0, expected2, 0, 3); // "Bar"
            assertArrayEquals(expected2, buffer);

            // Assert EOF
            assertEquals(EOF, reader.read(buffer));
        }
    }

    @Test
    @DisplayName("read(char[], int, int) with invalid parameters should throw an exception")
    void readIntoCharArrayWithInvalidParametersThrowsException() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"))) {
            final char[] buffer = new char[10];
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, -1, 1), "Negative offset");
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 0, -1), "Negative length");
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 0, 11), "Length too large");
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 10, 1), "Offset too large");
            assertThrows(NullPointerException.class, () -> reader.read(null, 0, 1), "Null buffer");
        }
    }

    @Test
    @DisplayName("read() on an explicitly closed reader should return EOF")
    void readOnClosedReaderReturnsEof() throws IOException {
        // Arrange
        @SuppressWarnings("resource") // Intentionally not using try-with-resources to test explicit close
        final Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));

        // Act
        reader.close();

        // Assert
        assertEquals(EOF, reader.read(), "Reader should be at EOF after close");
    }

    private static Stream<Arguments> readerCollectionProvider() {
        final List<Reader> readerList = List.of(new StringReader("F"), new StringReader("B"));
        final Collection<Reader> readerCollection = new ArrayList<>(readerList);
        return Stream.of(
            Arguments.of(new SequenceReader(readerList), "List"),
            Arguments.of(new SequenceReader(readerCollection), "Collection"),
            Arguments.of(new SequenceReader(new StringReader("F"), new StringReader("B")), "Varargs")
        );
    }

    @ParameterizedTest(name = "from {1}")
    @MethodSource("readerCollectionProvider")
    @DisplayName("Constructor should accept various reader sources")
    void constructorAcceptsVariousReaderSources(final Reader reader, final String type) throws IOException {
        try (reader) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertEquals(EOF, reader.read());
        }
    }

    @Test
    @DisplayName("read() should return EOF when all readers are empty")
    void readReturnsEofForEmptyReaders() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(StringUtils.EMPTY),
            new StringReader(StringUtils.EMPTY), new StringReader(StringUtils.EMPTY))) {
            assertEquals(EOF, reader.read());
        }
    }

    @Test
    @DisplayName("Should read sequentially from multiple single-character readers")
    void readFromMultipleSingleCharReaders() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader("0"),
            new StringReader("1"),
            new StringReader("2"),
            new StringReader("3"))) {

            assertEquals('0', reader.read());
            assertEquals('1', reader.read());
            assertEquals('2', reader.read());
            assertEquals('3', reader.read());
            assertEquals(EOF, reader.read());
        }
    }

    @Test
    @DisplayName("skip() should advance the position across reader boundaries")
    void skipAdvancesPositionAcrossReaderBoundaries() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            // Act & Assert
            assertEquals(3, reader.skip(3));
            assertReadsExpectedString(reader, "Bar");

            // Skip when already at the end
            assertEquals(0, reader.skip(3));
            assertEquals(EOF, reader.read());
        }
    }
}