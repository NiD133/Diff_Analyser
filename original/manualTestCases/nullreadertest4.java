package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TestNullReader}. This class focuses on verifying the
 * `read()` method and its behavior, including end-of-file handling and post-close operations.
 */
public class GeneratedTestCase {

    /**
     * Tests the `read()` method of {@link TestNullReader} to ensure it returns the expected
     * sequence of numbers, handles end-of-file (EOF) correctly, throws an exception
     * when reading after EOF, and resets its position upon closing.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    public void testRead() throws IOException {
        // 1. Setup: Create a TestNullReader with a specified size (number of readable characters).
        final int size = 5;
        final TestNullReader reader = new TestNullReader(size);

        // 2. Action & Assertion:  Read characters from the reader and verify they match the expected sequence.
        //    The TestNullReader is designed to return the sequence 0, 1, 2, 3, ... up to 'size' - 1.
        for (int i = 0; i < size; i++) {
            int actualValue = reader.read();
            assertEquals(i, actualValue, "Check Value [" + i + "]: The reader should return sequential numbers.");
        }

        // 3. Action & Assertion: Verify that the reader returns -1 after reaching the end of the stream (EOF).
        assertEquals(-1, reader.read(), "End of File: The reader should return -1 when the end of the stream is reached.");

        // 4. Action & Assertion: Verify that attempting to read after EOF throws an IOException with a specific message.
        assertThrows(IOException.class, () -> {
            reader.read(); // Attempt to read after EOF.
        }, "Reading after EOF should throw an IOException.");

        try {
            reader.read();
            fail("Should have thrown an IOException, indicating an attempt to read beyond the end of the file.");
        } catch (IOException e) {
            assertEquals("Read after end of file", e.getMessage(), "Exception message should indicate reading beyond EOF.");
        }

        // 5. Action & Assertion: Close the reader and verify that its internal position is reset to 0.
        reader.close();
        assertEquals(0, reader.getPosition(), "Available after close: Reader's position should reset to 0 after closing.");
    }

    /**
     * A custom Reader implementation for testing purposes. It returns sequential integers as characters,
     * simulating a stream of data.  It provides methods to track the current position and reset it.
     */
    private static class TestNullReader extends Reader {
        private final int size;
        private int position = 0;
        private boolean closed = false;

        /**
         * Constructs a new TestNullReader with the specified size.
         *
         * @param size The number of characters the reader will simulate.
         */
        public TestNullReader(int size) {
            this.size = size;
        }

        /**
         * Reads a single character from the reader.
         *
         * @return The next character in the sequence, or -1 if the end of the stream has been reached.
         * @throws IOException If an I/O error occurs, such as reading after the reader has been closed
         *                     or attempting to read beyond the end of the file.
         */
        @Override
        public int read() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            }
            if (position < size) {
                return position++;
            } else {
                if (position > size) { // Guard against multiple reads after EOF
                    throw new IOException("Read after end of file");
                }
                position++; // Increment position to ensure only one EOF is returned
                return -1;
            }
        }

        /**
         * Reads characters into a portion of an array.  This implementation throws UnsupportedOperationException.
         */
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new UnsupportedOperationException("This method is not implemented for this test class.");
        }

        /**
         * Closes the reader, resetting its position to 0.
         *
         * @throws IOException If an I/O error occurs.
         */
        @Override
        public void close() throws IOException {
            closed = true;
            position = 0;
        }

        /**
         * Returns the current position of the reader.
         *
         * @return The current position.
         */
        public int getPosition() {
            return position;
        }

        @Override
        public boolean ready() throws IOException {
            return !closed;
        }
    }
}