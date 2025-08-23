package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NullReader}.
 */
class NullReaderTest {

    // Custom NullReader for testing purposes
    private static final class TestNullReader extends NullReader {
        TestNullReader(final int size) {
            super(size);
        }

        TestNullReader(final int size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        @Override
        protected int processChar() {
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = offset; i < length; i++) {
                chars[i] = (char) (startPos + i);
            }
        }
    }

    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";

    @Test
    void testEOFException() throws Exception {
        try (Reader reader = new TestNullReader(2, false, true)) {
            assertEquals(0, reader.read(), "First character read");
            assertEquals(1, reader.read(), "Second character read");
            assertThrows(EOFException.class, reader::read, "Expected EOFException after reading past end");
        }
    }

    @Test
    void testMarkAndReset() throws Exception {
        final int readLimit = 10;
        try (Reader reader = new TestNullReader(100, true, false)) {
            assertTrue(reader.markSupported(), "Mark should be supported");

            // Attempt to reset without marking
            IOException resetException = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", resetException.getMessage(), "Expected no mark IOException");

            // Read initial characters
            for (int position = 0; position < 3; position++) {
                assertEquals(position, reader.read(), "Read before mark at position " + position);
            }

            // Mark current position
            reader.mark(readLimit);

            // Read more characters
            for (int i = 0; i < 3; i++) {
                assertEquals(i + 3, reader.read(), "Read after mark at position " + i);
            }

            // Reset to marked position
            reader.reset();

            // Read from marked position
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(i + 3, reader.read(), "Read after reset at position " + i);
            }

            // Attempt to reset after read limit exceeded
            IOException e = assertThrows(IOException.class, reader::reset);
            assertEquals("Marked position [3] is no longer valid - passed the read limit [10]", e.getMessage(),
                    "Expected read limit exceeded IOException");
        }
    }

    @Test
    void testMarkNotSupported() throws Exception {
        try (Reader reader = new TestNullReader(100, false, true)) {
            assertFalse(reader.markSupported(), "Mark should NOT be supported");

            assertThrows(UnsupportedOperationException.class, () -> reader.mark(5), "mark() should throw exception");
            assertThrows(UnsupportedOperationException.class, reader::reset, "reset() should throw exception");
        }
    }

    @Test
    void testRead() throws Exception {
        final int size = 5;
        try (TestNullReader reader = new TestNullReader(size)) {
            for (int i = 0; i < size; i++) {
                assertEquals(i, reader.read(), "Read character at position " + i);
            }

            // Check End of File
            assertEquals(-1, reader.read(), "Expected EOF");

            // Test reading after EOF
            IOException e = assertThrows(IOException.class, reader::read);
            assertEquals("Read after end of file", e.getMessage(), "Expected IOException after EOF");

            // Close and reset
            reader.close();
            assertEquals(0, reader.getPosition(), "Position should reset after close");
        }
    }

    @Test
    void testReadCharArray() throws Exception {
        final char[] buffer = new char[10];
        try (Reader reader = new TestNullReader(15)) {
            // Read into array
            int count1 = reader.read(buffer);
            assertEquals(buffer.length, count1, "Expected full buffer read");
            for (int i = 0; i < count1; i++) {
                assertEquals(i, buffer[i], "Expected character at buffer position " + i);
            }

            // Read remaining characters
            int count2 = reader.read(buffer);
            assertEquals(5, count2, "Expected partial buffer read");
            for (int i = 0; i < count2; i++) {
                assertEquals(i + 10, buffer[i], "Expected character at buffer position " + i);
            }

            // End of File
            assertEquals(-1, reader.read(buffer), "Expected EOF");

            // Test reading after EOF
            IOException e = assertThrows(IOException.class, () -> reader.read(buffer));
            assertEquals("Read after end of file", e.getMessage(), "Expected IOException after EOF");

            // Reset by closing
            reader.close();

            // Read with offset and length
            int offset = 2;
            int length = 4;
            int count3 = reader.read(buffer, offset, length);
            assertEquals(length, count3, "Expected partial buffer read with offset");
            for (int i = offset; i < offset + length; i++) {
                assertEquals(i, buffer[i], "Expected character at buffer position " + i);
            }
        }
    }

    @Test
    void testSkip() throws Exception {
        try (Reader reader = new TestNullReader(10, true, false)) {
            assertEquals(0, reader.read(), "First character read");
            assertEquals(1, reader.read(), "Second character read");
            assertEquals(5, reader.skip(5), "Skipped 5 characters");
            assertEquals(7, reader.read(), "Read character after skip");
            assertEquals(2, reader.skip(5), "Skipped remaining characters");
            assertEquals(-1, reader.skip(5), "Expected EOF on skip");

            IOException e = assertThrows(IOException.class, () -> reader.skip(5));
            assertEquals("Skip after end of file", e.getMessage(), "Expected IOException after EOF on skip");
        }
    }
}