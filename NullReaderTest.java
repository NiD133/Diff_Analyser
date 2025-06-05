package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NullReader}.
 */
public class NullReaderTest {

    /**
     * A test implementation of NullReader for testing purposes.
     */
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
    public void testEOFException() throws Exception {
        try (Reader reader = new TestNullReader(2, false, true)) {
            assertEquals(0, reader.read(), "First read should return 0");
            assertEquals(1, reader.read(), "Second read should return 1");
            assertThrows(EOFException.class, reader::read, "Reading past EOF should throw EOFException");
        }
    }

    @Test
    public void testMarkAndReset() throws Exception {
        final int readLimit = 10;
        try (Reader reader = new TestNullReader(100, true, false)) {
            assertTrue(reader.markSupported(), "Mark should be supported");

            // Attempt to reset without marking
            IOException resetException = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", resetException.getMessage(), "Expected no mark exception");

            // Read and mark
            for (int position = 0; position < 3; position++) {
                assertEquals(position, reader.read(), "Read before mark at position " + position);
            }
            reader.mark(readLimit);

            // Read after mark
            for (int i = 0; i < 3; i++) {
                assertEquals(3 + i, reader.read(), "Read after mark at position " + i);
            }

            // Reset and read again
            reader.reset();
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(3 + i, reader.read(), "Read after reset at position " + i);
            }

            // Reset after exceeding read limit
            IOException e = assertThrows(IOException.class, reader::reset);
            assertEquals("Marked position [3] is no longer valid - passed the read limit [10]", e.getMessage(),
                    "Expected read limit exception");
        }
    }

    @Test
    public void testMarkNotSupported() throws Exception {
        try (Reader reader = new TestNullReader(100, false, true)) {
            assertFalse(reader.markSupported(), "Mark should NOT be supported");

            UnsupportedOperationException markException = assertThrows(UnsupportedOperationException.class, () -> reader.mark(5));
            assertEquals(MARK_RESET_NOT_SUPPORTED, markException.getMessage(), "Expected mark not supported exception");

            UnsupportedOperationException resetException = assertThrows(UnsupportedOperationException.class, reader::reset);
            assertEquals(MARK_RESET_NOT_SUPPORTED, resetException.getMessage(), "Expected reset not supported exception");
        }
    }

    @Test
    public void testRead() throws Exception {
        final int size = 5;
        try (TestNullReader reader = new TestNullReader(size)) {
            for (int i = 0; i < size; i++) {
                assertEquals(i, reader.read(), "Read value at position " + i);
            }

            // Check End of File
            assertEquals(-1, reader.read(), "Read should return -1 at EOF");

            // Test reading after EOF
            IOException e = assertThrows(IOException.class, reader::read);
            assertEquals("Read after end of file", e.getMessage(), "Expected read after EOF exception");

            // Close and check reset
            reader.close();
            assertEquals(0, reader.getPosition(), "Position should reset to 0 after close");
        }
    }

    @Test
    public void testReadCharArray() throws Exception {
        final char[] chars = new char[10];
        try (Reader reader = new TestNullReader(15)) {
            // Read into array
            int count1 = reader.read(chars);
            assertEquals(chars.length, count1, "First read should fill the array");
            for (int i = 0; i < count1; i++) {
                assertEquals(i, chars[i], "Check character at index " + i);
            }

            // Read into array again
            int count2 = reader.read(chars);
            assertEquals(5, count2, "Second read should fill remaining space");
            for (int i = 0; i < count2; i++) {
                assertEquals(count1 + i, chars[i], "Check character at index " + i);
            }

            // End of File
            int count3 = reader.read(chars);
            assertEquals(-1, count3, "Read should return -1 at EOF");

            // Test reading after EOF
            IOException e = assertThrows(IOException.class, () -> reader.read(chars));
            assertEquals("Read after end of file", e.getMessage(), "Expected read after EOF exception");

            // Reset by closing
            reader.close();

            // Read into array using offset & length
            int offset = 2;
            int length = 4;
            int count5 = reader.read(chars, offset, length);
            assertEquals(length, count5, "Read should fill specified length");
            for (int i = offset; i < offset + length; i++) {
                assertEquals(i, chars[i], "Check character at index " + i);
            }
        }
    }

    @Test
    public void testSkip() throws Exception {
        try (Reader reader = new TestNullReader(10, true, false)) {
            assertEquals(0, reader.read(), "First read should return 0");
            assertEquals(1, reader.read(), "Second read should return 1");
            assertEquals(5, reader.skip(5), "Skip should move position by 5");
            assertEquals(7, reader.read(), "Read should return 7 after skip");
            assertEquals(2, reader.skip(5), "Skip should move position by 2 (remaining)");
            assertEquals(-1, reader.skip(5), "Skip should return -1 at EOF");

            IOException e = assertThrows(IOException.class, () -> reader.skip(5));
            assertEquals("Skip after end of file", e.getMessage(), "Expected skip after EOF exception");
        }
    }
}