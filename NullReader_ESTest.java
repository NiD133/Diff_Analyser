package org.apache.commons.io.input;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.IOException;

import org.junit.Test;

public class NullReaderTest {

    // -----------------------------
    // Basic read() and position
    // -----------------------------

    @Test
    public void readSingleCharsAdvancesPositionUntilEof() throws IOException {
        // Given a reader of size 2
        NullReader reader = new NullReader(2);

        // When reading two characters
        assertEquals(0, reader.read()); // processChar() returns 0
        assertEquals(1, reader.getPosition());
        assertEquals(0, reader.read());
        assertEquals(2, reader.getPosition());

        // Then the next read is EOF (-1) because throwEofException=false by default
        assertEquals(-1, reader.read());

        // And any read after EOF throws an IOException ("Read after end of file")
        assertThrows(IOException.class, reader::read);
    }

    @Test
    public void getSizeAndPositionReflectProgress() throws IOException {
        NullReader reader = new NullReader(3);
        assertEquals(3, reader.getSize());
        assertEquals(0, reader.getPosition());

        reader.read();
        assertEquals(1, reader.getPosition());

        reader.read();
        assertEquals(2, reader.getPosition());
    }

    // -----------------------------
    // Reading into arrays
    // -----------------------------

    @Test
    public void readIntoArrayReadsUpToRemaining() throws IOException {
        // Given a reader with exactly 3 characters
        NullReader reader = new NullReader(3);
        char[] buf = new char[5];

        // When reading more than available
        int n = reader.read(buf, 0, buf.length);

        // Then we read only what remains
        assertEquals(3, n);
        assertEquals(3, reader.getPosition());

        // EOF now: next read returns -1
        assertEquals(-1, reader.read(buf));
        // After EOF, attempting to read again throws IOException
        assertThrows(IOException.class, () -> reader.read(buf));
    }

    @Test
    public void readZeroLengthArrayReturnsZeroAndDoesNotAdvance() throws IOException {
        NullReader reader = new NullReader(10);
        char[] empty = new char[0];

        int n = reader.read(empty);

        assertEquals(0, n);
        assertEquals(0, reader.getPosition());
    }

    @Test
    public void readNullArrayThrowsNpe() {
        NullReader reader = new NullReader(1);

        assertThrows(NullPointerException.class, () -> reader.read((char[]) null));
        assertThrows(NullPointerException.class, () -> reader.read(null, 0, 1));
    }

    // -----------------------------
    // Skipping
    // -----------------------------

    @Test
    public void skipAdvancesPositionAndStopsAtEof() throws IOException {
        NullReader reader = new NullReader(5);

        // Skip within bounds
        assertEquals(2, reader.skip(2));
        assertEquals(2, reader.getPosition());

        // Skip beyond bounds clamps to size
        assertEquals(3, reader.skip(10));
        assertEquals(5, reader.getPosition()); // at EOF

        // First skip at EOF returns -1 (since throwEofException is false)
        assertEquals(-1, reader.skip(1));

        // Any skip after EOF returns IOException
        assertThrows(IOException.class, () -> reader.skip(1));
    }

    // -----------------------------
    // mark/reset behavior
    // -----------------------------

    @Test
    public void markAndResetRestorePositionWhenSupported() throws IOException {
        // markSupported = true
        NullReader reader = new NullReader(10, true, false);

        reader.read(); // pos=1
        reader.read(); // pos=2
        assertEquals(2, reader.getPosition());

        reader.mark(100); // mark at pos=2

        reader.read(); // pos=3
        reader.read(); // pos=4
        assertEquals(4, reader.getPosition());

        // Reset should restore marked position (2)
        reader.reset();
        assertEquals(2, reader.getPosition());

        // Further reads continue from the restored position
        char[] buf = new char[2];
        assertEquals(2, reader.read(buf, 0, 2));
        assertEquals(4, reader.getPosition());
    }

    @Test
    public void resetWithoutMarkThrowsIOException() {
        NullReader reader = new NullReader(1, true, false);
        assertThrows(IOException.class, reader::reset);
    }

    @Test
    public void markUnsupportedThrowsUnsupportedOperationException() {
        // markSupported = false
        NullReader reader = new NullReader(1, false, false);

        assertFalse(reader.markSupported());
        assertThrows(UnsupportedOperationException.class, () -> reader.mark(1));
        assertThrows(UnsupportedOperationException.class, reader::reset);
    }

    // -----------------------------
    // EOF modes
    // -----------------------------

    @Test
    public void throwEofExceptionModeThrowsOnFirstEof() {
        // size=0 means we are already at EOF
        NullReader reader = new NullReader(0, true, true);

        // In this mode, EOF is signaled with EOFException (not -1)
        assertThrows(EOFException.class, reader::read);
        assertThrows(EOFException.class, () -> reader.read(new char[1]));
        assertThrows(EOFException.class, () -> reader.read(new char[2], 0, 2));
        assertThrows(EOFException.class, () -> reader.skip(1));
    }

    @Test
    public void nonThrowingEofModeReturnsMinusOneThenIOExceptionAfterwards() throws IOException {
        NullReader reader = new NullReader(1, true, false);

        // Read the single available char
        assertEquals(0, reader.read());

        // First EOF encounter returns -1
        assertEquals(-1, reader.read());

        // Subsequent read attempts throw IOException
        assertThrows(IOException.class, reader::read);
    }

    // -----------------------------
    // close() behavior
    // -----------------------------

    @Test
    public void closeResetsInternalState() throws IOException {
        NullReader reader = new NullReader(2);

        // Consume to EOF
        assertEquals(0, reader.read());
        assertEquals(0, reader.read());
        assertEquals(-1, reader.read());
        assertEquals(2, reader.getPosition());

        // Close resets to initial state (position=0, not at EOF)
        reader.close();
        assertEquals(0, reader.getPosition());

        // We can read again from the start
        assertEquals(0, reader.read());
        assertEquals(1, reader.getPosition());
    }
}