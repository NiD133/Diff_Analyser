package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link NullReader}.
 */
public class NullReaderTest {

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
        // Test that an EOFException is thrown when reading past the end of the file
        try (Reader reader = new TestNullReader(2, false, true)) {
            assertEquals(0, reader.read(), "Read 1");
            assertEquals(1, reader.read(), "Read 2");
            assertThrows(EOFException.class, () -> reader.read());
        }
    }

    @Test
    public void testMarkAndReset() throws Exception {
        // Test mark and reset functionality
        int position = 0;
        final int readLimit = 10;
        try (Reader reader = new TestNullReader(100, true, false)) {
            // Check that mark is supported
            assertTrue(reader.markSupported(), "Mark Should be Supported");

            // Check that reset throws an exception when no mark has been set
            final IOException resetException = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", resetException.getMessage(), "No Mark IOException message");

            // Read some characters
            for (; position < 3; position++) {
                assertEquals(position, reader.read(), "Read Before Mark [" + position + "]");
            }

            // Set a mark
            reader.mark(readLimit);

            // Read some more characters
            for (int i = 0; i < 3; i++) {
                assertEquals(position + i, reader.read(), "Read After Mark [" + i + "]");
            }

            // Reset to the marked position
            reader.reset();

            // Read from the marked position
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(position + i, reader.read(), "Read After Reset [" + i + "]");
            }

            // Check that reset throws an exception when the read limit has been exceeded
            final IOException e = assertThrows(IOException.class, reader::reset);
            assertEquals("Marked position [" + position + "] is no longer valid - passed the read limit [" + readLimit + "]", e.getMessage(),
                    "Read limit IOException message");
        }
    }

    @Test
    public void testMarkNotSupported() throws Exception {
        // Test that mark and reset throw exceptions when not supported
        final Reader reader = new TestNullReader(100, false, true);
        assertFalse(reader.markSupported(), "Mark Should NOT be Supported");

        try {
            reader.mark(5);
            assertThrows(UnsupportedOperationException.class, () -> reader.mark(5));
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "mark() error message");
        }

        try {
            reader.reset();
            assertThrows(UnsupportedOperationException.class, reader::reset);
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "reset() error message");
        }
        reader.close();
    }

    @Test
    public void testRead() throws Exception {
        // Test reading from the NullReader
        final int size = 5;
        final TestNullReader reader = new TestNullReader(size);
        for (int i = 0; i < size; i++) {
            assertEquals(i, reader.read(), "Check Value [" + i + "]");
        }

        // Check that -1 is returned when the end of the file is reached
        assertEquals(-1, reader.read(), "End of File");

        // Check that an IOException is thrown when trying to read past the end of the file
        try {
            final int result = reader.read();
            assertThrows(IOException.class, () -> reader.read());
        } catch (final IOException e) {
            assertEquals("Read after end of file", e.getMessage());
        }

        // Check that the reader is reset after closing
        reader.close();
        assertEquals(0, reader.getPosition(), "Available after close");
    }

    @Test
    public void testReadCharArray() throws Exception {
        // Test reading into a character array
        final char[] chars = new char[10];
        final Reader reader = new TestNullReader(15);

        // Read into the array
        final int count1 = reader.read(chars);
        assertEquals(chars.length, count1, "Read 1");
        for (int i = 0; i < count1; i++) {
            assertEquals(i, chars[i], "Check Chars 1");
        }

        // Read into the array again
        final int count2 = reader.read(chars);
        assertEquals(5, count2, "Read 2");
        for (int i = 0; i < count2; i++) {
            assertEquals(count1 + i, chars[i], "Check Chars 2");
        }

        // Check that -1 is returned when the end of the file is reached
        final int count3 = reader.read(chars);
        assertEquals(-1, count3, "Read 3 (EOF)");

        // Check that an IOException is thrown when trying to read past the end of the file
        try {
            final int count4 = reader.read(chars);
            assertThrows(IOException.class, () -> reader.read(chars));
        } catch (final IOException e) {
            assertEquals("Read after end of file", e.getMessage());
        }

        // Reset the reader by closing it
        reader.close();

        // Read into the array using an offset and length
        final int offset = 2;
        final int length = 4;
        final int count5 = reader.read(chars, offset, length);
        assertEquals(length, count5, "Read 5");
        for (int i = offset; i < offset + length; i++) {
            assertEquals(i, chars[i], "Check Chars 3");
        }
    }

    @Test
    public void testSkip() throws Exception {
        // Test skipping characters
        try (Reader reader = new TestNullReader(10, true, false)) {
            assertEquals(0, reader.read(), "Read 1");
            assertEquals(1, reader.read(), "Read 2");
            assertEquals(5, reader.skip(5), "Skip 1");
            assertEquals(7, reader.read(), "Read 3");
            assertEquals(2, reader.skip(5), "Skip 2"); // only 2 left to skip
            assertEquals(-1, reader.skip(5), "Skip 3 (EOF)"); // End of file

            // Check that an IOException is thrown when trying to skip past the end of the file
            final IOException e = assertThrows(IOException.class, () -> reader.skip(5));
            assertEquals("Skip after end of file", e.getMessage(), "Skip after EOF IOException message");
        }
    }
}