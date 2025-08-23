package org.apache.commons.io.input;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class BoundedReaderTest {

    // Helper to reduce noise when constructing the SUT.
    private static BoundedReader bounded(final Reader target, final int maxChars) {
        return new BoundedReader(target, maxChars);
    }

    // -----------------------------
    // Basic read behavior and bounds
    // -----------------------------

    @Test
    public void readCharArray_stopsAtConfiguredBound() throws IOException {
        // Given a long input but a small bound
        final StringReader in = new StringReader("org.apache.commons.io.input.BoundedReader");
        final BoundedReader br = bounded(in, 3);

        final char[] buf = new char[7];

        // When reading into a fresh buffer
        final int read = br.read(buf);

        // Then we only read up to the configured maximum
        assertEquals(3, read);
        assertArrayEquals(new char[] {'o', 'r', 'g', '\u0000', '\u0000', '\u0000', '\u0000'}, buf);
    }

    @Test
    public void readSingleChar_withinBound_returnsFirstChar() throws IOException {
        final StringReader in = new StringReader("v4]>?/Q;dj|.O1#4");
        final BoundedReader br = bounded(in, 1);

        assertEquals('v', br.read());
    }

    @Test
    public void readSingleChar_onEmptyStream_returnsEOF() throws IOException {
        final BoundedReader br = bounded(new StringReader(""), 1805);

        // Repeated reads on empty input always return EOF
        assertEquals(-1, br.read());
        br.mark(1268);
        assertEquals(-1, br.read());
    }

    @Test
    public void readWithNegativeBoundAndNullTarget_immediatelyReturnsEOF() throws IOException {
        // If maxChars is negative, the bound prevents any read, so EOF is returned even if target is null.
        assertEquals(-1, bounded(null, -1).read());
    }

    // -----------------------------
    // Reading into arrays and offsets
    // -----------------------------

    @Test
    public void readArray_lenZero_doesNotValidateOffsets_returnsZero() throws IOException {
        // Standard Reader contract: len == 0 returns 0 without touching the offset.
        final BoundedReader br = bounded(new StringReader(""), 1);
        assertEquals(0, br.read(new char[3], -743, 0));
    }

    @Test
    public void readArray_readsIntoGivenOffset() throws IOException {
        final BoundedReader br = bounded(new StringReader(".''L5DuTEy{jV3"), 1729);

        final char[] buf = new char[8];
        final int read = br.read(buf, 1, 1);

        assertEquals(1, read);
        assertArrayEquals(new char[] {'\u0000', '.', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buf);
    }

    @Test
    public void readArray_withOffsetOutOfBounds_throwsArrayIndexOutOfBoundsException() {
        final BoundedReader br = bounded(new StringReader("4s"), 179);
        final char[] empty = new char[0];

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> br.read(empty, 179, 179));
    }

    @Test
    public void readArray_onClosedUnderlyingReader_throwsIOExceptionBeforeBoundsCheck() throws IOException {
        // StringReader throws "Stream closed" before validating offsets.
        final StringReader in = new StringReader("");
        in.close();

        final BoundedReader br = bounded(in, 214);

        assertThrows(IOException.class, () -> br.read(new char[0], 214, 214));
    }

    @Test
    public void readArray_nullBufferAndNegativeLen_returnsEOF() throws IOException {
        // Note: This is a somewhat unusual edge case observed in the legacy tests.
        // If the requested length is negative, the implementation returns EOF without touching the buffer.
        final BoundedReader br = bounded(new StringReader(""), 1805);
        assertEquals(-1, br.read(null, 1805, -1));
    }

    // -----------------------------
    // mark/reset and skip behavior
    // -----------------------------

    @Test
    public void reset_withoutPriorMark_isNoOpForStringReader() throws IOException {
        // StringReader supports reset to position 0 even without a prior mark.
        final BoundedReader br = bounded(new StringReader(""), 1);
        br.reset(); // should not throw
    }

    @Test
    public void markThenRead_toEOFOnEmptyInput() throws IOException {
        final BoundedReader br = bounded(new StringReader(""), 1805);
        br.mark(1);
        br.read();
        assertEquals(-1, br.read());
    }

    @Test
    public void skip_respectsReadAheadLimitSetByMark() throws IOException {
        // Given input of 2 chars and a max bound much larger than input
        final BoundedReader br = bounded(new StringReader("wa"), 10);

        // When we mark with readAheadLimit=1, skip cannot move past the marked read-ahead limit
        br.mark(1);
        assertEquals(1L, br.skip(10));
    }

    @Test
    public void mark_withNegativeReadAheadLimit_throwsIllegalArgumentException() {
        final BoundedReader br = bounded(new StringReader(""), -431);
        assertThrows(IllegalArgumentException.class, () -> br.mark(-431));
    }

    @Test
    public void mark_onClosedUnderlyingReader_throwsIOException() throws IOException {
        final StringReader in = new StringReader("");
        in.close();

        final BoundedReader br = bounded(in, 198);
        assertThrows(IOException.class, () -> br.mark(198));
    }

    // -----------------------------
    // Error handling: null or closed underlying Reader
    // -----------------------------

    @Test
    public void read_onNullTargetAndPositiveBound_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bounded(null, 1).read());
    }

    @Test
    public void read_onClosedUnderlyingReader_throwsIOException() throws IOException {
        final StringReader in = new StringReader("");
        in.close();

        assertThrows(IOException.class, () -> bounded(in, 202).read());
    }

    @Test
    public void readArray_onNullTarget_throwsNullPointerException() {
        final BoundedReader br = bounded(null, 1);
        assertThrows(NullPointerException.class, () -> br.read(new char[1], 1, 1));
    }

    @Test
    public void reset_onNullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bounded(null, 1).reset());
    }

    @Test
    public void close_onNullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bounded(null, -1235).close());
    }

    @Test
    public void reset_onClosedUnderlyingReader_throwsIOException() throws IOException {
        final StringReader in = new StringReader("org.apache.commons.io.input.BoundedReader");
        final BoundedReader br = bounded(in, 0);

        br.close(); // close underlying StringReader
        assertThrows(IOException.class, br::reset);
    }
}