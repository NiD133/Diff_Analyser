/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link NullReader}.
 */
class NullReaderTest {

    // Test constants
    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";
    private static final int END_OF_FILE = -1;
    
    /**
     * Test implementation of NullReader that returns predictable character values.
     * Characters returned are based on the current position in the stream.
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
            // Return character based on current position (0-based)
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            // Fill array with characters based on their position in the stream
            final int startPosition = (int) getPosition() - length;
            for (int i = offset; i < length; i++) {
                chars[i] = (char) (startPosition + i);
            }
        }
    }

    @Test
    void testEOFException_WhenThrowEofExceptionIsEnabled() throws Exception {
        final int readerSize = 2;
        final boolean markSupported = false;
        final boolean throwEofException = true;
        
        try (Reader reader = new TestNullReader(readerSize, markSupported, throwEofException)) {
            // Read all available characters
            assertEquals(0, reader.read(), "First character should be 0");
            assertEquals(1, reader.read(), "Second character should be 1");
            
            // Attempting to read beyond EOF should throw EOFException
            assertThrows(EOFException.class, reader::read, 
                "Reading beyond EOF should throw EOFException when throwEofException is true");
        }
    }

    @Test
    void testMarkAndReset_WithMarkSupported() throws Exception {
        final int readerSize = 100;
        final boolean markSupported = true;
        final boolean throwEofException = false;
        final int readLimit = 10;
        
        try (Reader reader = new TestNullReader(readerSize, markSupported, throwEofException)) {
            assertTrue(reader.markSupported(), "Reader should support mark/reset operations");

            // Test reset without mark should fail
            IOException resetException = assertThrows(IOException.class, reader::reset,
                "Reset without mark should throw IOException");
            assertEquals("No position has been marked", resetException.getMessage());

            // Read some characters before marking
            int charactersReadBeforeMark = 3;
            for (int i = 0; i < charactersReadBeforeMark; i++) {
                assertEquals(i, reader.read(), "Character at position " + i + " should match expected value");
            }

            // Mark current position
            reader.mark(readLimit);
            int markedPosition = charactersReadBeforeMark;

            // Read some characters after marking
            int charactersToReadAfterMark = 3;
            for (int i = 0; i < charactersToReadAfterMark; i++) {
                int expectedChar = markedPosition + i;
                assertEquals(expectedChar, reader.read(), 
                    "Character " + i + " after mark should be " + expectedChar);
            }

            // Reset to marked position
            reader.reset();

            // Read from marked position - should get same values as before
            for (int i = 0; i < readLimit + 1; i++) {
                int expectedChar = markedPosition + i;
                assertEquals(expectedChar, reader.read(), 
                    "Character " + i + " after reset should be " + expectedChar);
            }

            // Reset after exceeding read limit should fail
            IOException readLimitException = assertThrows(IOException.class, reader::reset,
                "Reset after exceeding read limit should throw IOException");
            String expectedMessage = "Marked position [" + markedPosition + 
                "] is no longer valid - passed the read limit [" + readLimit + "]";
            assertEquals(expectedMessage, readLimitException.getMessage());
        }
    }

    @Test
    void testMarkNotSupported_ThrowsUnsupportedOperationException() throws Exception {
        final int readerSize = 100;
        final boolean markSupported = false;
        final boolean throwEofException = true;
        
        try (Reader reader = new TestNullReader(readerSize, markSupported, throwEofException)) {
            assertFalse(reader.markSupported(), "Reader should not support mark/reset operations");

            // Test mark() throws UnsupportedOperationException
            UnsupportedOperationException markException = assertThrows(
                UnsupportedOperationException.class, 
                () -> reader.mark(5),
                "mark() should throw UnsupportedOperationException when not supported"
            );
            assertEquals(MARK_RESET_NOT_SUPPORTED, markException.getMessage());

            // Test reset() throws UnsupportedOperationException
            UnsupportedOperationException resetException = assertThrows(
                UnsupportedOperationException.class, 
                reader::reset,
                "reset() should throw UnsupportedOperationException when not supported"
            );
            assertEquals(MARK_RESET_NOT_SUPPORTED, resetException.getMessage());
        }
    }

    @Test
    void testRead_SingleCharacter() throws Exception {
        final int readerSize = 5;
        final TestNullReader reader = new TestNullReader(readerSize);
        
        // Read all available characters
        for (int expectedChar = 0; expectedChar < readerSize; expectedChar++) {
            assertEquals(expectedChar, reader.read(), 
                "Character at position " + expectedChar + " should match expected value");
        }

        // Reading at EOF should return -1
        assertEquals(END_OF_FILE, reader.read(), "Reading at EOF should return -1");

        // Reading after EOF should throw IOException
        IOException afterEofException = assertThrows(IOException.class, reader::read,
            "Reading after EOF should throw IOException");
        assertEquals("Read after end of file", afterEofException.getMessage());

        // Close should reset the reader
        reader.close();
        assertEquals(0, reader.getPosition(), "Position should be reset to 0 after close");
    }

    @Test
    void testReadCharArray_FullArrayAndPartialReads() throws Exception {
        final int bufferSize = 10;
        final int readerSize = 15;
        final char[] buffer = new char[bufferSize];
        
        try (Reader reader = new TestNullReader(readerSize)) {
            // First read - should fill entire buffer
            int firstReadCount = reader.read(buffer);
            assertEquals(bufferSize, firstReadCount, "First read should fill entire buffer");
            verifyBufferContents(buffer, 0, firstReadCount, "First read");

            // Second read - should read remaining characters
            int secondReadCount = reader.read(buffer);
            int expectedSecondReadCount = readerSize - bufferSize; // 5 characters remaining
            assertEquals(expectedSecondReadCount, secondReadCount, "Second read should read remaining characters");
            verifyBufferContents(buffer, bufferSize, secondReadCount, "Second read");

            // Third read - should return EOF
            int thirdReadCount = reader.read(buffer);
            assertEquals(END_OF_FILE, thirdReadCount, "Third read should return EOF");

            // Reading after EOF should throw IOException
            IOException afterEofException = assertThrows(IOException.class, () -> reader.read(buffer),
                "Reading after EOF should throw IOException");
            assertEquals("Read after end of file", afterEofException.getMessage());
        }
    }

    @Test
    void testReadCharArray_WithOffsetAndLength() throws Exception {
        final char[] buffer = new char[10];
        final int readerSize = 15;
        
        try (Reader reader = new TestNullReader(readerSize)) {
            // Reset reader by closing and creating new one
            reader.close();
            
            // Read into specific portion of array
            final int offset = 2;
            final int length = 4;
            int readCount = reader.read(buffer, offset, length);
            
            assertEquals(length, readCount, "Should read exactly the requested length");
            
            // Verify only the specified portion was filled
            for (int i = offset; i < offset + length; i++) {
                assertEquals(i - offset, buffer[i], 
                    "Buffer position " + i + " should contain character " + (i - offset));
            }
        }
    }

    @Test
    void testSkip_VariousScenarios() throws Exception {
        final int readerSize = 10;
        
        try (Reader reader = new TestNullReader(readerSize, true, false)) {
            // Read first two characters
            assertEquals(0, reader.read(), "First character should be 0");
            assertEquals(1, reader.read(), "Second character should be 1");
            
            // Skip 5 characters
            long skippedCount1 = reader.skip(5);
            assertEquals(5, skippedCount1, "Should skip exactly 5 characters");
            
            // Next read should be at position 7
            assertEquals(7, reader.read(), "Next character should be at position 7");
            
            // Try to skip 5 more, but only 2 remain
            long skippedCount2 = reader.skip(5);
            assertEquals(2, skippedCount2, "Should only skip 2 remaining characters");
            
            // Skip at EOF should return -1
            long skippedAtEof = reader.skip(5);
            assertEquals(END_OF_FILE, skippedAtEof, "Skip at EOF should return -1");

            // Skip after EOF should throw IOException
            IOException skipAfterEofException = assertThrows(IOException.class, () -> reader.skip(5),
                "Skip after EOF should throw IOException");
            assertEquals("Skip after end of file", skipAfterEofException.getMessage());
        }
    }

    /**
     * Helper method to verify buffer contents match expected character sequence.
     */
    private void verifyBufferContents(char[] buffer, int startPosition, int length, String readDescription) {
        for (int i = 0; i < length; i++) {
            int expectedChar = startPosition + i;
            assertEquals(expectedChar, buffer[i], 
                readDescription + ": buffer[" + i + "] should contain character " + expectedChar);
        }
    }
}