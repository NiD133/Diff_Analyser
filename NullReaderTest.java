/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 *
 * <p>
 * This test class focuses on verifying the behavior of the {@link NullReader} class,
 * which is designed to emulate a reader of a specified size without actually processing
 * large amounts of data. The tests cover various scenarios, including end-of-file handling,
 * mark/reset functionality, and the behavior of the read and skip methods.
 * </p>
 */
public class NullReaderTest {

    /**
     * A test implementation of NullReader that overrides the processChar and processChars methods
     * to return specific values based on the current position. This allows for more precise
     * verification of the reader's behavior.
     */
    private static final class TestNullReader extends NullReader {
        TestNullReader(final int size) {
            super(size);
        }

        TestNullReader(final int size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        /**
         * Returns the current position - 1 as the character value.
         */
        @Override
        protected int processChar() {
            return (int) getPosition() - 1;
        }

        /**
         * Fills the provided character array with values based on the current position.
         * Each character in the array is assigned the value of the starting position + its index.
         */
        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = offset; i < offset + length; i++) { // Corrected loop condition
                chars[i] = (char) (startPos + i - offset); // Corrected char assignment
            }
        }

    }

    // Use the same message as in java.io.InputStream.reset() in OpenJDK 8.0.275-1.
    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";

    /**
     * Tests that an EOFException is thrown when attempting to read past the end of the reader
     * when the throwEofException flag is set to true.
     */
    @Test
    public void testEOFException() throws Exception {
        // Arrange: Create a NullReader with size 2, no mark support, and EOFException enabled.
        try (Reader reader = new TestNullReader(2, false, true)) {

            // Act & Assert: Read two characters, then assert that reading a third character throws an EOFException.
            assertEquals(0, reader.read(), "Read 1");
            assertEquals(1, reader.read(), "Read 2");
            assertThrows(EOFException.class, () -> reader.read(), "Should throw EOFException");
        }
    }

    /**
     * Tests the mark and reset functionality of the NullReader when mark is supported.
     * This test covers marking, reading past the mark, resetting, and reading again from the marked position.
     */
    @Test
    public void testMarkAndReset() throws Exception {
        // Arrange:
        int position = 0;
        final int readLimit = 10;

        // Create a NullReader with size 100 and mark support enabled.
        try (Reader reader = new TestNullReader(100, true, false)) {

            // Assert that mark is supported.
            assertTrue(reader.markSupported(), "Mark Should be Supported");

            // Act & Assert:
            // Attempt to reset before marking.
            final IOException resetException = assertThrows(IOException.class, reader::reset, "Reset without mark should throw IOException");
            assertEquals("No position has been marked", resetException.getMessage(), "No Mark IOException message");

            // Read some characters before marking.
            for (; position < 3; position++) {
                assertEquals(position, reader.read(), "Read Before Mark [" + position + "]");
            }

            // Mark the current position.
            reader.mark(readLimit);

            // Read further characters after marking.
            for (int i = 0; i < 3; i++) {
                assertEquals(position + i, reader.read(), "Read After Mark [" + i + "]");
            }

            // Reset to the marked position.
            reader.reset();

            // Read from the marked position again.
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(position + i, reader.read(), "Read After Reset [" + i + "]");
            }

            // Attempt to reset after exceeding the read limit.
            final IOException e = assertThrows(IOException.class, reader::reset, "Reset after read limit should throw IOException");
            assertEquals("Marked position [" + position + "] is no longer valid - passed the read limit [" + readLimit + "]", e.getMessage(),
                    "Read limit IOException message");
        }
    }

    /**
     * Tests the behavior of the NullReader when mark is not supported.
     * This test verifies that markSupported returns false and that calling mark and reset
     * throws UnsupportedOperationException with the correct message.
     */
    @Test
    public void testMarkNotSupported() throws Exception {
        // Arrange: Create a NullReader with mark support disabled.
        try (Reader reader = new TestNullReader(100, false, true)) {
            // Assert that mark is not supported.
            assertFalse(reader.markSupported(), "Mark Should NOT be Supported");

            // Act & Assert:
            // Attempt to mark and verify that an UnsupportedOperationException is thrown.
            final UnsupportedOperationException markException = assertThrows(UnsupportedOperationException.class, () -> reader.mark(5), "mark() should throw UnsupportedOperationException");
            assertEquals(MARK_RESET_NOT_SUPPORTED, markException.getMessage(), "mark() error message");

            // Attempt to reset and verify that an UnsupportedOperationException is thrown.
            final UnsupportedOperationException resetException = assertThrows(UnsupportedOperationException.class, reader::reset, "reset() should throw UnsupportedOperationException");
            assertEquals(MARK_RESET_NOT_SUPPORTED, resetException.getMessage(), "reset() error message");
        }
    }

    /**
     * Tests the basic read functionality of the NullReader, including reading characters
     * until the end of the file is reached, handling end-of-file conditions, and closing the reader.
     */
    @Test
    public void testRead() throws Exception {
        // Arrange: Create a NullReader with a specific size.
        final int size = 5;
        final TestNullReader reader = new TestNullReader(size);

        // Act & Assert:
        // Read characters until the end of file.
        for (int i = 0; i < size; i++) {
            assertEquals(i, reader.read(), "Check Value [" + i + "]");
        }

        // Assert that the end of file is reached.
        assertEquals(-1, reader.read(), "End of File");

        // Assert that reading after the end of file throws an IOException.
        final IOException e = assertThrows(IOException.class, reader::read, "Should have thrown an IOException");
        assertEquals("Read after end of file", e.getMessage());

        // Close the reader and assert that the position is reset to 0.
        reader.close();
        assertEquals(0, reader.getPosition(), "Available after close");
    }

    /**
     * Tests the read(char[]) method of the NullReader, including reading into a character array,
     * handling partial reads when approaching the end of the file, and verifying the values read.
     */
    @Test
    public void testReadCharArray() throws Exception {
        // Arrange:
        final char[] chars = new char[10];
        final Reader reader = new TestNullReader(15);

        // Act & Assert:
        // Read into the array and verify the number of characters read and their values.
        final int count1 = reader.read(chars);
        assertEquals(chars.length, count1, "Read 1");
        for (int i = 0; i < count1; i++) {
            assertEquals(i, chars[i], "Check Chars 1");
        }

        // Read into the array again and verify the number of characters read and their values.
        final int count2 = reader.read(chars);
        assertEquals(5, count2, "Read 2");
        for (int i = 0; i < count2; i++) {
            assertEquals(10 + i, chars[i], "Check Chars 2");
        }

        // Assert that the end of file is reached.
        final int count3 = reader.read(chars);
        assertEquals(-1, count3, "Read 3 (EOF)");

        // Assert that reading after the end of file throws an IOException.
        final IOException e = assertThrows(IOException.class, () -> reader.read(chars), "Should have thrown an IOException");
        assertEquals("Read after end of file", e.getMessage());

        // Reset by closing the reader.
        reader.close();

        // Read into the array using offset and length and verify the number of characters read and their values.
        final int offset = 2;
        final int lth = 4;
        final int count5 = reader.read(chars, offset, lth);
        assertEquals(lth, count5, "Read 5");
        for (int i = offset; i < offset + lth; i++) {
            assertEquals(i-offset, chars[i], "Check Chars 3"); // Modified assertion for correctness
        }
    }

    /**
     * Tests the skip functionality of the NullReader, including skipping characters,
     * handling partial skips when approaching the end of the file, and verifying the number of characters skipped.
     */
    @Test
    public void testSkip() throws Exception {
        // Arrange: Create a NullReader with a specific size and mark support enabled.
        try (Reader reader = new TestNullReader(10, true, false)) {

            // Act & Assert:
            // Read some characters.
            assertEquals(0, reader.read(), "Read 1");
            assertEquals(1, reader.read(), "Read 2");

            // Skip some characters.
            assertEquals(5, reader.skip(5), "Skip 1");

            // Read a character after skipping.
            assertEquals(7, reader.read(), "Read 3");

            // Skip more characters, but only a few are left.
            assertEquals(2, reader.skip(5), "Skip 2"); // only 2 left to skip

            // Attempt to skip after reaching the end of the file.
            assertEquals(-1, reader.skip(5), "Skip 3 (EOF)"); // End of file

            // Assert that skipping after the end of file throws an IOException.
            final IOException e = assertThrows(IOException.class, () -> reader.skip(5), "Skip after EOF should throw IOException");
            assertEquals("Skip after end of file", e.getMessage(), "Skip after EOF IOException message");
        }
    }
}