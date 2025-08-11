package org.apache.commons.io.input;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentModificationException;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for SequenceReader.
 *
 * These tests avoid EvoSuite scaffolding and focus on:
 * - Concatenating multiple Readers.
 * - Parameter validation and error handling.
 * - Skipping, closing, and end-of-stream behavior.
 * - Interaction with the underlying Iterable (fail-fast iterations).
 */
public class SequenceReaderTest {

    // ----------------------------
    // Happy-path concatenation
    // ----------------------------

    @Test
    public void concatenatesMultipleReaders_withReadBuffer() throws IOException {
        SequenceReader sr = new SequenceReader(
            new StringReader("abc"),
            new StringReader("DEF")
        );

        char[] buf = new char[6];
        int n = sr.read(buf, 0, buf.length);

        assertEquals(6, n);
        assertArrayEquals("abcDEF".toCharArray(), buf);
        assertEquals(-1, sr.read()); // EOF after reading all content
    }

    @Test
    public void concatenatesMultipleReaders_withReadSingleChars() throws IOException {
        SequenceReader sr = new SequenceReader(
            new StringReader("12"),
            new StringReader("34"),
            new StringReader("5")
        );

        StringBuilder out = new StringBuilder();
        int ch;
        while ((ch = sr.read()) != -1) {
            out.append((char) ch);
        }

        assertEquals("12345", out.toString());
    }

    @Test
    public void skipSkipsAcrossAllReaders() throws IOException {
        SequenceReader sr = new SequenceReader(
            new StringReader("!DROK>c"), // 7
            new StringReader("org.apache.commons.io.filefilter.CanExecuteFileFilter") // 53
        );

        long skipped = sr.skip(204); // More than available; should skip everything.
        assertEquals(60L, skipped);
        assertEquals(-1, sr.read()); // All skipped, now EOF.
    }

    @Test
    public void ignoresNullReadersInIterable() throws IOException {
        List<Reader> readers = Arrays.asList(
            new StringReader("A"),
            null,
            new StringReader("B")
        );

        SequenceReader sr = new SequenceReader(readers);

        char[] buf = new char[2];
        int n = sr.read(buf, 0, buf.length);

        assertEquals(2, n);
        assertArrayEquals(new char[] {'A', 'B'}, buf);
        assertEquals(-1, sr.read());
    }

    // ----------------------------
    // End-of-stream behavior
    // ----------------------------

    @Test
    public void emptyIterable_returnsEOF() throws IOException {
        SequenceReader sr = new SequenceReader(new ArrayList<Reader>());
        assertEquals(-1, sr.read());
    }

    // ----------------------------
    // Error and parameter validation
    // ----------------------------

    @Test
    public void readIntoNullBuffer_throwsNPE() throws IOException {
        SequenceReader sr = new SequenceReader(new ArrayList<Reader>());
        NullPointerException ex = assertThrows(NullPointerException.class, () -> sr.read(null, 0, 1));
        // The implementation uses Objects.requireNonNull(cbuf, "cbuf")
        assertTrue(String.valueOf(ex.getMessage()).contains("cbuf"));
    }

    @Test
    public void readWithNegativeOffset_throwsIndexOutOfBounds() throws IOException {
        SequenceReader sr = new SequenceReader(new ArrayList<Reader>());
        char[] buf = new char[4];

        assertThrows(IndexOutOfBoundsException.class, () -> sr.read(buf, -1, 1));
    }

    @Test
    public void readWithNegativeLength_throwsIndexOutOfBounds() throws IOException {
        SequenceReader sr = new SequenceReader(new ArrayList<Reader>());
        char[] buf = new char[4];

        assertThrows(IndexOutOfBoundsException.class, () -> sr.read(buf, 0, -1));
    }

    @Test
    public void readWithOffsetPlusLengthExceedingArray_throwsIndexOutOfBounds() throws IOException {
        SequenceReader sr = new SequenceReader(new ArrayList<Reader>());
        char[] buf = new char[4];

        assertThrows(IndexOutOfBoundsException.class, () -> sr.read(buf, 2, 3));
    }

    @Test
    public void constructor_withNullVarargs_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new SequenceReader((Reader[]) null));
    }

    @Test
    public void constructor_withNullIterable_throwsNPEWithMessage() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new SequenceReader((Iterable<? extends Reader>) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("readers"));
    }

    // ----------------------------
    // Resource management
    // ----------------------------

    @Test
    public void close_closesAllUnderlyingReaders_andFurtherReadsReturnEOF() throws IOException {
        CloseTrackingReader r1 = new CloseTrackingReader("a");
        CloseTrackingReader r2 = new CloseTrackingReader("bcd");
        CloseTrackingReader r3 = new CloseTrackingReader("");

        SequenceReader sr = new SequenceReader(r1, r2, r3);
        sr.close();

        assertTrue(r1.closed);
        assertTrue(r2.closed);
        assertTrue(r3.closed);

        // After close, SequenceReader returns EOF.
        assertEquals(-1, sr.read());
    }

    // ----------------------------
    // Behavior with reused Reader instances
    // ----------------------------

    @Test
    public void reusingSameReaderTwice_causesIOExceptionOnSecondUse() {
        // StringReader throws "Stream closed" once closed and used again.
        StringReader closedOnce = new StringReader("");

        SequenceReader sr = new SequenceReader(closedOnce, closedOnce);

        IOException ex = assertThrows(IOException.class, () -> {
            char[] buf = new char[1];
            // First empty reader advances; second is the same instance but now closed, so read() fails.
            sr.read(buf, 0, 1);
        });
        assertTrue(String.valueOf(ex.getMessage()).contains("closed"));
    }

    // ----------------------------
    // Lazy iteration over the Iterable
    // ----------------------------

    @Test
    public void modifyingBackedIterableAfterConstruction_mayThrowConcurrentModificationOnAdvance() {
        // Build a fail-fast iterable (ArrayDeque) and modify it after construction.
        ArrayDeque<Reader> deque = new ArrayDeque<>();
        deque.add(new StringReader(""));             // Empty: forces SequenceReader to advance immediately.
        deque.add(new StringReader("x"));

        SequenceReader sr = new SequenceReader(deque);

        // Modify after construction; next attempt to advance should observe the modification.
        deque.add(new StringReader("new"));

        assertThrows(ConcurrentModificationException.class, () -> sr.read());
    }

    // ----------------------------
    // Helpers
    // ----------------------------

    /**
     * A minimal Reader that tracks whether close() was called.
     */
    private static final class CloseTrackingReader extends Reader {
        private final String data;
        private int pos = 0;
        boolean closed = false;

        CloseTrackingReader(String data) {
            this.data = data != null ? data : "";
        }

        @Override
        public int read() throws IOException {
            ensureOpen();
            if (pos >= data.length()) {
                return -1;
            }
            return data.charAt(pos++);
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            ensureOpen();
            if (len == 0) {
                return 0;
            }
            if (pos >= data.length()) {
                return -1;
            }
            int toRead = Math.min(len, data.length() - pos);
            data.getChars(pos, pos + toRead, cbuf, off);
            pos += toRead;
            return toRead;
        }

        @Override
        public void close() {
            closed = true;
        }

        private void ensureOpen() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            }
        }
    }
}