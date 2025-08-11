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
            for (int i = offset; i < offset + length; i++) {
                chars[i] = (char) (startPos + i - offset);
            }
        }
    }

    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";
    private static final int TEST_READER_SIZE = 5;

    @Test
    void readBeyondEndWithEofExceptionEnabled_ThrowsEofException() throws Exception {
        try (Reader reader = new TestNullReader(2, false, true)) {
            assertEquals(0, reader.read(), "First read should return 0");
            assertEquals(1, reader.read(), "Second read should return 1");
            assertThrows(EOFException.class, reader::read, 
                "Read beyond end should throw EOFException");
        }
    }

    @Test
    void markSupported_WhenConstructedWithMarkSupported_ReturnsTrue() throws Exception {
        try (Reader reader = new TestNullReader(100, true, false)) {
            assertTrue(reader.markSupported(), "Reader should support mark");
        }
    }

    @Test
    void resetWithoutMark_ThrowsExceptionWithExpectedMessage() throws Exception {
        try (Reader reader = new TestNullReader(100, true, false)) {
            IOException exception = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", exception.getMessage(),
                "Exception message should indicate no mark was set");
        }
    }

    @Test
    void markAndReset_ResetsToMarkedPosition() throws Exception {
        try (Reader reader = new TestNullReader(100, true, false)) {
            // Read initial characters
            assertEquals(0, reader.read(), "Character before mark");
            assertEquals(1, reader.read(), "Character before mark");
            assertEquals(2, reader.read(), "Character before mark");
            
            // Set mark at position 3
            reader.mark(10);
            
            // Read past mark
            assertEquals(3, reader.read(), "Character after mark");
            assertEquals(4, reader.read(), "Character after mark");
            assertEquals(5, reader.read(), "Character after mark");
            
            // Reset should return to marked position
            reader.reset();
            
            // Verify reading starts from marked position (3)
            assertEquals(3, reader.read(), "First character after reset");
            assertEquals(4, reader.read(), "Second character after reset");
        }
    }

    @Test
    void resetAfterExceedingReadLimit_ThrowsExceptionWithExpectedMessage() throws Exception {
        try (Reader reader = new TestNullReader(100, true, false)) {
            // Set mark at position 0 with read limit 3
            reader.mark(3);
            
            // Read beyond read limit
            for (int i = 0; i < 4; i++) {
                reader.read();
            }
            
            // Reset after exceeding read limit
            IOException exception = assertThrows(IOException.class, reader::reset);
            assertEquals("Marked position [0] is no longer valid - passed the read limit [3]", 
                exception.getMessage(), "Exception message should indicate read limit exceeded");
        }
    }

    @Test
    void markNotSupported_MarkOperation_ThrowsUnsupportedOperationException() {
        TestNullReader reader = new TestNullReader(100, false, true);
        assertFalse(reader.markSupported(), "Mark should not be supported");
        
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class, 
            () -> reader.mark(5)
        );
        assertEquals(MARK_RESET_NOT_SUPPORTED, exception.getMessage(),
            "mark() should throw UnsupportedOperationException with proper message");
        
        reader.close();
    }

    @Test
    void markNotSupported_ResetOperation_ThrowsUnsupportedOperationException() {
        TestNullReader reader = new TestNullReader(100, false, true);
        
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class, 
            reader::reset
        );
        assertEquals(MARK_RESET_NOT_SUPPORTED, exception.getMessage(),
            "reset() should throw UnsupportedOperationException with proper message");
        
        reader.close();
    }

    @Test
    void read_WithSize_ReturnsExpectedCharacters() throws Exception {
        TestNullReader reader = new TestNullReader(TEST_READER_SIZE);
        
        for (int i = 0; i < TEST_READER_SIZE; i++) {
            assertEquals(i, reader.read(), "Character at position " + i);
        }
        
        reader.close();
    }

    @Test
    void readAtEndOfFile_ReturnsEofIndicator() throws Exception {
        TestNullReader reader = new TestNullReader(TEST_READER_SIZE);
        
        // Exhaust the reader
        for (int i = 0; i < TEST_READER_SIZE; i++) {
            reader.read();
        }
        
        assertEquals(-1, reader.read(), "Read at EOF should return -1");
        reader.close();
    }

    @Test
    void readAfterEndOfFile_ThrowsExceptionWithExpectedMessage() throws Exception {
        TestNullReader reader = new TestNullReader(1);
        reader.read();  // Read the only character
        
        IOException exception = assertThrows(IOException.class, reader::read);
        assertEquals("Read after end of file", exception.getMessage(),
            "Exception message should indicate read after EOF");
        
        reader.close();
    }

    @Test
    void close_ResetsPositionToZero() throws Exception {
        TestNullReader reader = new TestNullReader(TEST_READER_SIZE);
        reader.read();
        reader.read();
        
        reader.close();
        
        assertEquals(0, reader.getPosition(), "Position should reset to 0 after close");
    }

    @Test
    void readCharArray_ReadsExpectedData() throws Exception {
        char[] buffer = new char[10];
        TestNullReader reader = new TestNullReader(15);
        
        int firstRead = reader.read(buffer);
        assertEquals(buffer.length, firstRead, "First read should fill buffer");
        for (int i = 0; i < firstRead; i++) {
            assertEquals(i, buffer[i], "Buffer content at index " + i);
        }
    }

    @Test
    void readCharArrayAfterEnd_ReturnsEofIndicator() throws Exception {
        char[] buffer = new char[10];
        TestNullReader reader = new TestNullReader(5);
        
        // Exhaust the reader
        reader.read(buffer);
        
        int result = reader.read(buffer);
        assertEquals(-1, result, "Read at EOF should return -1");
        reader.close();
    }

    @Test
    void readPartialCharArray_WithOffsetAndLength_ReadsExpectedData() throws Exception {
        char[] buffer = new char[10];
        TestNullReader reader = new TestNullReader(15);
        
        int offset = 2;
        int length = 4;
        int readCount = reader.read(buffer, offset, length);
        
        assertEquals(length, readCount, "Should read specified length");
        for (int i = offset; i < offset + length; i++) {
            assertEquals(i - offset, buffer[i], "Buffer content at index " + i);
        }
        reader.close();
    }

    @Test
    void skip_WithinSize_SkipsExpectedCharacters() throws Exception {
        try (TestNullReader reader = new TestNullReader(10, true, false)) {
            reader.read(); // Position=1
            
            long skipped = reader.skip(5);
            assertEquals(5, skipped, "Skipped characters count");
            assertEquals(6, reader.read(), "Character after skip");
        }
    }

    @Test
    void skipBeyondEnd_ReturnsRemainingCharacters() throws Exception {
        try (TestNullReader reader = new TestNullReader(3, true, false)) {
            reader.read(); // Position=1
            long skipped = reader.skip(5);
            assertEquals(2, skipped, "Should skip remaining characters");
            assertEquals(-1, reader.read(), "Next read should be EOF");
        }
    }

    @Test
    void skipAfterEnd_ThrowsExceptionWithExpectedMessage() throws Exception {
        try (TestNullReader reader = new TestNullReader(2, true, false)) {
            reader.skip(3); // Skip beyond end
            IOException exception = assertThrows(IOException.class, () -> reader.skip(1));
            assertEquals("Skip after end of file", exception.getMessage(),
                "Exception should indicate skip after EOF");
        }
    }
}