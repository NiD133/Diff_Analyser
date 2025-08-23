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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    private static final char NUL = 0;
    private static final String FOO = "Foo";
    private static final String BAR = "Bar";
    private static final String FOOBAR = FOO + BAR;

    /**
     * A Reader that closes itself on first read() and then behaves as EOF.
     * Useful to verify that SequenceReader copes with already-closed readers.
     */
    private static class SelfClosingEofReader extends Reader {
        private boolean closed;

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            ensureOpen();
            close(); // Immediately close itself on first read
            return EOF;
        }

        @Override
        public void close() {
            closed = true;
        }

        boolean isClosed() {
            return closed;
        }

        private void ensureOpen() throws IOException {
            if (closed) {
                throw new IOException("Reader already closed");
            }
        }
    }

    /**
     * A Reader that returns a single 'A' and then EOF.
     * Tracks whether close() was called.
     */
    private static class OneCharReader extends Reader {
        private final char[] content = {'A'};
        private int position;
        private boolean closed;

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            if (closed) {
                throw new IOException("Reader already closed");
            }
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

        @Override
        public void close() {
            closed = true;
        }

        boolean isClosed() {
            return closed;
        }
    }

    // ----------------------------
    // Helper assertions and utils.
    // ----------------------------

    private static void assertReadsExactly(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), (char) reader.read(), "Read[" + i + "] of '" + expected + "'");
        }
    }

    private static void assertEofRepeatedly(final Reader reader) throws IOException {
        // Call read() multiple times to ensure stable EOF behavior.
        for (int i = 0; i < 10; i++) {
            assertEquals(EOF, reader.read());
        }
    }

    // ----------------------------
    // Close behavior
    // ----------------------------

    @Test
    @DisplayName("Closing inside try-with-resources keeps reader at EOF afterwards")
    void testAutoClose() throws IOException {
        try (Reader reader = new SequenceReader(new CharSequenceReader(FOOBAR))) {
            assertReadsExactly(reader, FOO);
            reader.close();
            assertEofRepeatedly(reader);
        }
    }

    @Test
    @DisplayName("Closing an explicitly managed SequenceReader keeps it at EOF afterwards")
    void testClose() throws IOException {
        final Reader reader = new SequenceReader(new CharSequenceReader(FOOBAR));
        assertReadsExactly(reader, FOO);
        reader.close();
        assertEofRepeatedly(reader);
    }

    @Test
    @DisplayName("SequenceReader closes underlying readers as they are exhausted")
    void testCloseUnderlyingReaders() throws IOException {
        final SelfClosingEofReader willSelfCloseOnFirstRead = new SelfClosingEofReader();
        final OneCharReader singleCharReader = new OneCharReader();

        try (SequenceReader sequenceReader = new SequenceReader(singleCharReader, willSelfCloseOnFirstRead)) {
            assertEquals('A', sequenceReader.read());
            assertEquals(EOF, sequenceReader.read());
        } finally {
            assertTrue(singleCharReader.isClosed(), "First reader should be closed");
            assertTrue(willSelfCloseOnFirstRead.isClosed(), "Second reader should be closed");
        }
        // Double-check after try-with-resources block
        assertTrue(singleCharReader.isClosed());
        assertTrue(willSelfCloseOnFirstRead.isClosed());
    }

    // ----------------------------
    // Reader capabilities
    // ----------------------------

    @Test
    @DisplayName("markSupported() is false")
    void testMarkSupportedIsFalse() throws IOException {
        try (Reader reader = new SequenceReader()) {
            assertFalse(reader.markSupported());
        }
    }

    // ----------------------------
    // read(): single chars, arrays, offsets
    // ----------------------------

    @Test
    @DisplayName("read() traverses multiple readers in order")
    void testReadSingleChars() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(FOO), new StringReader(BAR))) {
            assertEquals('F', reader.read());
            assertEquals('o', reader.read());
            assertEquals('o', reader.read());
            assertEquals('B', reader.read());
            assertEquals('a', reader.read());
            assertEquals('r', reader.read());
            assertEofRepeatedly(reader);
        }
    }

    @Test
    @DisplayName("read(char[]) fills arrays across reader boundaries")
    void testReadCharArray() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(FOO), new StringReader(BAR))) {
            char[] buf = new char[2];
            assertEquals(2, reader.read(buf));
            assertArrayEquals(new char[] {'F', 'o'}, buf);

            buf = new char[3];
            assertEquals(3, reader.read(buf));
            assertArrayEquals(new char[] {'o', 'B', 'a'}, buf);

            buf = new char[3];
            assertEquals(1, reader.read(buf));
            assertArrayEquals(new char[] {'r', NUL, NUL}, buf);

            assertEquals(EOF, reader.read(buf));
        }
    }

    @Test
    @DisplayName("read(char[], off, len) handles offsets, lengths, and exceptions")
    void testReadCharArrayPortion() throws IOException {
        final char[] buf = new char[10];
        try (Reader reader = new SequenceReader(new StringReader(FOO), new StringReader(BAR))) {
            assertEquals(3, reader.read(buf, 3, 3));
            assertArrayEquals(new char[] {NUL, NUL, NUL, 'F', 'o', 'o', NUL, NUL, NUL, NUL}, buf);

            assertEquals(3, reader.read(buf, 0, 3));
            assertArrayEquals(new char[] {'B', 'a', 'r', 'F', 'o', 'o', NUL, NUL, NUL, NUL}, buf);

            assertEquals(EOF, reader.read(buf));

            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 10, 10));
            assertThrows(NullPointerException.class, () -> reader.read(null, 0, 10));
        }
    }

    @Test
    @DisplayName("Reading after close() returns EOF")
    void testReadAfterCloseReturnsEof() throws IOException {
        @SuppressWarnings("resource")
        final Reader reader = new SequenceReader(new CharSequenceReader(FOOBAR));
        reader.close();
        assertEofRepeatedly(reader);
    }

    // ----------------------------
    // Constructors: Iterable, Collection, List
    // ----------------------------

    @Test
    @DisplayName("Construct with Collection<Reader>")
    void testConstructWithCollection() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertEofRepeatedly(reader);
        }
    }

    @Test
    @DisplayName("Construct with Iterable<Reader>")
    void testConstructWithIterable() throws IOException {
        final Collection<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        final Iterable<Reader> iterable = readers;
        try (Reader reader = new SequenceReader(iterable)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertEofRepeatedly(reader);
        }
    }

    @Test
    @DisplayName("Construct with List<Reader>")
    void testConstructWithList() throws IOException {
        final List<Reader> readers = new ArrayList<>();
        readers.add(new StringReader("F"));
        readers.add(new StringReader("B"));
        try (Reader reader = new SequenceReader(readers)) {
            assertEquals('F', reader.read());
            assertEquals('B', reader.read());
            assertEofRepeatedly(reader);
        }
    }

    // ----------------------------
    // Edge cases with empty and single-char readers
    // ----------------------------

    @Test
    @DisplayName("Reading from all-empty readers returns EOF")
    void testReadAllEmptyReaders() throws IOException {
        try (Reader reader = new SequenceReader(
                new StringReader(StringUtils.EMPTY),
                new StringReader(StringUtils.EMPTY),
                new StringReader(StringUtils.EMPTY))) {
            assertEofRepeatedly(reader);
        }
    }

    @Test
    @DisplayName("Reading across multiple single-character readers preserves order")
    void testReadSingleCharReaders() throws IOException {
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

    // ----------------------------
    // Skipping
    // ----------------------------

    @Test
    @DisplayName("skip() skips across reader boundaries")
    void testSkip() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader(FOO), new StringReader(BAR))) {
            assertEquals(3, reader.skip(3));
            assertReadsExactly(reader, BAR);
            assertEquals(0, reader.skip(3));
        }
    }
}