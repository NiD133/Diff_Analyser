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
 * Test suite for the {@link SequenceReader} class.
 */
class SequenceReaderTest {

    /**
     * CustomReader is a simple Reader implementation used for testing.
     * It simulates a reader that can be closed and throws an exception if read after being closed.
     */
    private static class CustomReader extends Reader {

        private boolean closed;

        private void ensureOpen() throws IOException {
            if (closed) {
                throw new IOException("Reader is already closed");
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
        public int read(final char[] buffer, final int offset, final int length) throws IOException {
            ensureOpen();
            close(); // Simulate reading and then closing
            return EOF;
        }
    }

    private static final char NULL_CHAR = 0;

    /**
     * Asserts that two character arrays are equal.
     */
    private void assertCharArrayEquals(final char[] expected, final char[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "Character mismatch at index " + i);
        }
    }

    /**
     * Reads characters from the reader and compares them to the expected string.
     */
    private void assertReaderContentEquals(final Reader reader, final String expectedContent) throws IOException {
        for (int i = 0; i < expectedContent.length(); i++) {
            assertEquals(expectedContent.charAt(i), (char) reader.read(), "Mismatch at position " + i);
        }
    }

    /**
     * Asserts that the reader returns EOF for a number of reads.
     */
    private void assertReaderIsAtEof(final Reader reader) throws IOException {
        for (int i = 0; i < 10; i++) {
            assertEquals(EOF, reader.read(), "Reader should be at EOF");
        }
    }

    @Test
    void testAutoCloseReader() throws IOException {
        try (Reader reader = new SequenceReader(new CharSequenceReader("FooBar"))) {
            assertReaderContentEquals(reader, "Foo");
            reader.close();
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testManualCloseReader() throws IOException {
        final Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));
        assertReaderContentEquals(reader, "Foo");
        reader.close();
        assertReaderIsAtEof(reader);
    }

    @Test
    void testCloseAllReaders() throws IOException {
        final CustomReader reader1 = new CustomReader();
        final CustomReader reader2 = new CustomReader() {
            private final char[] content = {'A'};
            private int position;

            @Override
            public int read(final char[] buffer, final int offset, final int length) throws IOException {
                ensureOpen();

                if (offset < 0 || length < 0 || length > buffer.length - offset) {
                    throw new IndexOutOfBoundsException("Invalid offset or length");
                }

                if (position > 0) {
                    return EOF;
                }

                buffer[offset] = content[0];
                position++;
                return 1;
            }
        };

        try (SequenceReader sequenceReader = new SequenceReader(reader2, reader1)) {
            assertEquals('A', sequenceReader.read());
            assertEquals(EOF, sequenceReader.read());
        } finally {
            assertTrue(reader2.isClosed());
            assertTrue(reader1.isClosed());
        }
    }

    @Test
    void testMarkNotSupported() throws Exception {
        try (Reader reader = new SequenceReader()) {
            assertFalse(reader.markSupported(), "Mark should not be supported");
        }
    }

    @Test
    void testSequentialRead() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            assertEquals('F', reader.read());
            assertEquals('o', reader.read());
            assertEquals('o', reader.read());
            assertEquals('B', reader.read());
            assertEquals('a', reader.read());
            assertEquals('r', reader.read());
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testReadIntoCharArray() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            char[] buffer = new char[2];
            assertEquals(2, reader.read(buffer));
            assertCharArrayEquals(new char[] {'F', 'o'}, buffer);

            buffer = new char[3];
            assertEquals(3, reader.read(buffer));
            assertCharArrayEquals(new char[] {'o', 'B', 'a'}, buffer);

            buffer = new char[3];
            assertEquals(1, reader.read(buffer));
            assertCharArrayEquals(new char[] {'r', NULL_CHAR, NULL_CHAR}, buffer);

            assertEquals(EOF, reader.read(buffer));
        }
    }

    @Test
    void testReadIntoCharArrayPortion() throws IOException {
        final char[] buffer = new char[10];
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            assertEquals(3, reader.read(buffer, 3, 3));
            assertCharArrayEquals(new char[] {NULL_CHAR, NULL_CHAR, NULL_CHAR, 'F', 'o', 'o'}, buffer);

            assertEquals(3, reader.read(buffer, 0, 3));
            assertCharArrayEquals(new char[] {'B', 'a', 'r', 'F', 'o', 'o', NULL_CHAR}, buffer);

            assertEquals(EOF, reader.read(buffer));
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 10, 10));
            assertThrows(NullPointerException.class, () -> reader.read(null, 0, 10));
        }
    }

    @Test
    void testReadAfterClose() throws IOException {
        final Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));
        reader.close();
        assertReaderIsAtEof(reader);
    }

    @Test
    void testReadFromCollection() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testReadFromIterable() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        final Iterable<Reader> iterable = readers;
        try (Reader reader = new SequenceReader(iterable)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testReadEmptyReaders() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader(StringUtils.EMPTY),
            new StringReader(StringUtils.EMPTY),
            new StringReader(StringUtils.EMPTY))) {
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testReadSingleCharacterReaders() throws IOException {
        try (Reader reader = new SequenceReader(
            new StringReader("0"),
            new StringReader("1"),
            new StringReader("2"),
            new StringReader("3"))) {
            assertEquals('0', reader.read());
            assertEquals('1', reader.read());
            assertEquals('2', reader.read());
            assertEquals('3', reader.read());
        }
    }

    @Test
    void testReadFromList() throws IOException {
        final List<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertReaderIsAtEof(reader);
        }
    }

    @Test
    void testSkipCharacters() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("Foo"), new StringReader("Bar"))) {
            assertEquals(3, reader.skip(3));
            assertReaderContentEquals(reader, "Bar");
            assertEquals(0, reader.skip(3));
        }
    }
}