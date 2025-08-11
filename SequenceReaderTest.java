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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link SequenceReader}.
 */
class SequenceReaderTest {

    private static final char NUL = 0;
    private static final String READER_1_CONTENT = "Foo";
    private static final String READER_2_CONTENT = "Bar";
    private static final String COMBINED_CONTENT = "FooBar";

    @Test
    void autoClose_afterPartialRead_returnsEOF() throws IOException {
        try (Reader reader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT))) {
            readCharByChar(reader, READER_1_CONTENT);
            reader.close();
            assertReadsEOF(reader);
        }
    }

    @Test
    void close_afterPartialRead_returnsEOF() throws IOException {
        final Reader reader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT));
        readCharByChar(reader, READER_1_CONTENT);
        reader.close();
        assertReadsEOF(reader);
    }

    @Test
    void close_closesAllReaders() throws IOException {
        final CustomReader reader0 = new CustomReader();
        final CustomReader reader1 = new CustomReader() {

            private final char[] content = {'A'};
            private int position;

            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                checkOpen();

                if (off < 0) {
                    throw new IndexOutOfBoundsException("off is negative");
                }
                if (len < 0) {
                    throw new IndexOutOfBoundsException("len is negative");
                }
                if (len > cbuf.length - off) {
                    throw new IndexOutOfBoundsException("len is greater than cbuf.length - off");
                }

                if (position > 0) {
                    return EOF;
                }

                cbuf[off] = content[0];
                position++;
                return 1;
            }

        };

        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader0)) {
            assertEquals('A', sequenceReader.read());
            assertEquals(EOF, sequenceReader.read());
        }
        assertTrue(reader1.isClosed(), "Reader1 should be closed");
        assertTrue(reader0.isClosed(), "Reader0 should be closed");
    }

    @Test
    void markSupported_always_returnsFalse() throws Exception {
        try (Reader reader = new SequenceReader()) {
            assertFalse(reader.markSupported(), "markSupported should return false");
        }
    }

    @Test
    void read_charByChar_readsFromMultipleReaders() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader(READER_1_CONTENT), 
            new StringReader(READER_2_CONTENT))) {
            readCharByChar(reader, COMBINED_CONTENT);
            assertReadsEOF(reader);
        }
    }

    @Test
    void read_intoCharArray_readsFromMultipleReaders() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader(READER_1_CONTENT), 
            new StringReader(READER_2_CONTENT))) {
            
            char[] buffer = new char[2];
            assertEquals(2, reader.read(buffer), "Should read first two characters");
            assertArrayEquals(new char[] {'F', 'o'}, buffer, "Buffer content after first read");

            buffer = new char[3];
            assertEquals(3, reader.read(buffer), "Should read next three characters");
            assertArrayEquals(new char[] {'o', 'B', 'a'}, buffer, "Buffer content after second read");

            buffer = new char[3];
            assertEquals(1, reader.read(buffer), "Should read last character");
            assertArrayEquals(new char[] {'r', NUL, NUL}, buffer, "Buffer content after third read");

            assertEquals(EOF, reader.read(buffer), "Should return EOF at end");
        }
    }

    @Test
    void read_intoCharArrayPortion_readsSpecifiedRange() throws IOException {
        final char[] buffer = new char[10];
        try (Reader reader = new SequenceReader(
            new StringReader(READER_1_CONTENT), 
            new StringReader(READER_2_CONTENT))) {
            
            assertEquals(3, reader.read(buffer, 3, 3), "Should read three characters into offset 3");
            assertArrayEquals(new char[] {NUL, NUL, NUL, 'F', 'o', 'o', NUL, NUL, NUL, NUL}, 
                buffer, "Buffer after first read");

            assertEquals(3, reader.read(buffer, 0, 3), "Should read next three characters into offset 0");
            assertArrayEquals(new char[] {'B', 'a', 'r', 'F', 'o', 'o', NUL, NUL, NUL, NUL}, 
                buffer, "Buffer after second read");
        }
    }

    @Test
    void read_intoCharArrayPortion_invalidOffset_throwsIndexOutOfBounds() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(""))) {
            char[] buffer = new char[10];
            assertThrows(IndexOutOfBoundsException.class, 
                () -> reader.read(buffer, -1, 1), "Negative offset should throw");
            assertThrows(IndexOutOfBoundsException.class, 
                () -> reader.read(buffer, 11, 1), "Offset beyond buffer length should throw");
        }
    }

    @Test
    void read_intoCharArrayPortion_invalidLength_throwsIndexOutOfBounds() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(""))) {
            char[] buffer = new char[10];
            assertThrows(IndexOutOfBoundsException.class, 
                () -> reader.read(buffer, 0, -1), "Negative length should throw");
            assertThrows(IndexOutOfBoundsException.class, 
                () -> reader.read(buffer, 0, 11), "Length beyond buffer size should throw");
            assertThrows(IndexOutOfBoundsException.class, 
                () -> reader.read(buffer, 5, 6), "Length + offset beyond buffer size should throw");
        }
    }

    @Test
    void read_intoCharArrayPortion_nullBuffer_throwsNullPointer() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(""))) {
            assertThrows(NullPointerException.class, 
                () -> reader.read(null, 0, 1), "Null buffer should throw");
        }
    }

    @Test
    void read_afterClose_returnsEOF() throws IOException {
        final Reader reader = new SequenceReader(new CharSequenceReader(COMBINED_CONTENT));
        reader.close();
        assertReadsEOF(reader);
    }

    @Test
    void read_fromReaderCollection_readsAllContent() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            readCharByChar(reader, "FB");
            assertReadsEOF(reader);
        }
    }

    @Test
    void read_fromReaderIterable_readsAllContent() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader((Iterable<Reader>) readers)) {
            readCharByChar(reader, "FB");
            assertReadsEOF(reader);
        }
    }

    @Test
    void read_fromEmptyReaders_returnsEOF() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader(StringUtils.EMPTY),
            new StringReader(StringUtils.EMPTY),
            new StringReader(StringUtils.EMPTY))) {
            assertReadsEOF(reader);
        }
    }

    @Test
    void read_fromSingleCharReaders_readsAllContent() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader("0"),
            new StringReader("1"),
            new StringReader("2"),
            new StringReader("3"))) {
            readCharByChar(reader, "0123");
        }
    }

    @Test
    void read_fromReaderList_readsAllContent() throws IOException {
        final List<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            readCharByChar(reader, "FB");
            assertReadsEOF(reader);
        }
    }

    @Test
    void skip_jumpsOverCharacters() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader(READER_1_CONTENT), 
            new StringReader(READER_2_CONTENT))) {
            assertEquals(3, reader.skip(3), "Should skip first three characters");
            readCharByChar(reader, READER_2_CONTENT);
            assertEquals(0, reader.skip(3), "Skip at EOF should return 0");
        }
    }

    // Helper Methods
    private void readCharByChar(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            final char expectedChar = expected.charAt(i);
            final int readChar = reader.read();
            assertEquals(expectedChar, (char) readChar, 
                String.format("Character mismatch at position %d", i));
        }
    }

    private void assertReadsEOF(final Reader reader) throws IOException {
        for (int i = 0; i < 10; i++) {
            assertEquals(EOF, reader.read(), "Should return EOF after close/end");
        }
    }

    // Helper Classes
    private static class CustomReader extends Reader {

        boolean closed;

        protected void checkOpen() throws IOException {
            if (closed) {
                throw new IOException("Reader already closed");
            }
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            checkOpen();
            close();
            return EOF;
        }
    }
}