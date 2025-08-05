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

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NullReader}.
 */
@DisplayName("NullReader")
class NullReaderTest {

    /**
     * A test implementation of NullReader that returns the character position
     * instead of a null character. This makes assertions more specific.
     */
    private static final class TestNullReader extends NullReader {
        TestNullReader(final long size) {
            super(size);
        }

        TestNullReader(final long size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        @Override
        protected int processChar() {
            // Return the character corresponding to the position before it was incremented
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = 0; i < length; i++) {
                chars[offset + i] = (char) (startPos + i);
            }
        }
    }

    // Use the same message as in java.io.InputStream.reset() in OpenJDK 8.0.275-1.
    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";

    @Test
    @DisplayName("close() should reset the reader's position")
    void close_shouldResetPosition() throws IOException {
        // Arrange
        final int size = 5;
        try (final TestNullReader reader = new TestNullReader(size)) {
            reader.read();
            reader.read();
            assertTrue(reader.getPosition() > 0, "Position should have advanced before close");

            // Act
            reader.close();

            // Assert
            assertEquals(0, reader.getPosition(), "Position should be reset to 0 after close");
        }
    }

    @Nested
    @DisplayName("read() method")
    class ReadTests {
        @Test
        @DisplayName("should return sequential characters and then -1 at EOF")
        void read_shouldReturnSequentialCharsThenEOF() throws IOException {
            // Arrange
            final int size = 5;
            try (final Reader reader = new TestNullReader(size)) {
                // Act & Assert
                for (int i = 0; i < size; i++) {
                    assertEquals(i, reader.read(), "Character at position " + i + " should match");
                }
                assertEquals(-1, reader.read(), "Should return -1 at end of file");
            }
        }

        @Test
        @DisplayName("should throw IOException when reading after EOF")
        void read_afterEOF_shouldThrowIOException() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(2)) {
                reader.read(); // pos=1
                reader.read(); // pos=2
                assertEquals(-1, reader.read(), "Confirm reader is at EOF");

                // Act & Assert
                final IOException e = assertThrows(IOException.class, reader::read, "Reading after EOF should throw");
                assertEquals("Read after end of file", e.getMessage());
            }
        }

        @Test
        @DisplayName("should throw EOFException at end of stream if configured")
        void read_whenConfiguredToThrowOnEOF_shouldThrowEOFException() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(2, false, true)) {
                reader.read(); // pos=1
                reader.read(); // pos=2, at EOF

                // Act & Assert
                assertThrows(EOFException.class, reader::read, "Reading at EOF should throw EOFException");
            }
        }
    }

    @Nested
    @DisplayName("read(char[]) method")
    class ReadCharArrayTests {
        @Test
        @DisplayName("should fill the buffer when there is enough data")
        void readCharArray_whenEnoughData_shouldFillBuffer() throws IOException {
            // Arrange
            final char[] buffer = new char[10];
            try (final Reader reader = new TestNullReader(15)) {
                // Act
                final int charsRead = reader.read(buffer);

                // Assert
                assertEquals(buffer.length, charsRead, "Should have read a full buffer");
                for (int i = 0; i < charsRead; i++) {
                    assertEquals(i, buffer[i], "Character at index " + i + " is incorrect");
                }
            }
        }

        @Test
        @DisplayName("should read only remaining data when buffer is larger")
        void readCharArray_whenBufferIsLarger_shouldReadRemaining() throws IOException {
            // Arrange
            final char[] buffer = new char[10];
            try (final Reader reader = new TestNullReader(5)) {
                // Act
                final int charsRead = reader.read(buffer);

                // Assert
                assertEquals(5, charsRead, "Should have read the 5 remaining characters");
                for (int i = 0; i < charsRead; i++) {
                    assertEquals(i, buffer[i], "Character at index " + i + " is incorrect");
                }
            }
        }

        @Test
        @DisplayName("should return -1 at end of file")
        void readCharArray_atEOF_shouldReturnMinusOne() throws IOException {
            // Arrange
            final char[] buffer = new char[10];
            try (final Reader reader = new TestNullReader(0)) {
                // Act & Assert
                assertEquals(-1, reader.read(buffer), "Should return -1 for an empty reader");
            }
        }

        @Test
        @DisplayName("should throw IOException when reading after EOF")
        void readCharArray_afterEOF_shouldThrowIOException() throws IOException {
            // Arrange
            final char[] buffer = new char[10];
            try (final Reader reader = new TestNullReader(2)) {
                reader.read(buffer); // Reads the 2 available chars
                assertEquals(-1, reader.read(buffer), "Confirm reader is at EOF");

                // Act & Assert
                final IOException e = assertThrows(IOException.class, () -> reader.read(buffer));
                assertEquals("Read after end of file", e.getMessage());
            }
        }

        @Test
        @DisplayName("with offset and length should read into the correct slice of the array")
        void readCharArray_withOffsetAndLength_shouldReadIntoSlice() throws IOException {
            // Arrange
            final char[] buffer = new char[10];
            final int offset = 2;
            final int length = 4;
            try (final Reader reader = new TestNullReader(20)) {
                // Act
                final int charsRead = reader.read(buffer, offset, length);

                // Assert
                assertEquals(length, charsRead, "Should have read 'length' characters");
                for (int i = 0; i < length; i++) {
                    // The values read are 0, 1, 2, 3
                    // They are placed in the buffer at offset 2
                    assertEquals(i, buffer[offset + i], "Character at buffer index " + (offset + i) + " is incorrect");
                }
            }
        }
    }

    @Nested
    @DisplayName("skip() method")
    class SkipTests {
        @Test
        @DisplayName("should advance the position by the specified number of characters")
        void skip_shouldAdvancePosition() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(20)) {
                reader.read(); // pos=1
                reader.read(); // pos=2

                // Act
                final long skipped = reader.skip(5);

                // Assert
                assertEquals(5, skipped, "Should report 5 characters skipped");
                assertEquals(7, reader.read(), "Next character should be at position 7 (2+5)");
            }
        }

        @Test
        @DisplayName("should skip only the remaining characters if near EOF")
        void skip_nearEOF_shouldSkipRemaining() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(10)) {
                reader.skip(8); // pos=8

                // Act
                final long skipped = reader.skip(5); // Tries to skip 5, but only 2 are left

                // Assert
                assertEquals(2, skipped, "Should skip only the 2 remaining characters");
                assertEquals(-1, reader.read(), "Reader should now be at EOF");
            }
        }

        @Test
        @DisplayName("should return -1 when called at EOF")
        void skip_atEOF_shouldReturnMinusOne() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(5)) {
                reader.skip(5); // pos=5, at EOF

                // Act & Assert
                assertEquals(-1, reader.skip(5), "skip() at EOF should return -1");
            }
        }

        @Test
        @DisplayName("should throw IOException when called after EOF")
        void skip_afterEOF_shouldThrowIOException() throws IOException {
            // Arrange
            try (final Reader reader = new TestNullReader(5)) {
                reader.skip(10); // Go to EOF
                assertEquals(-1, reader.skip(1), "Confirm at EOF");

                // Act & Assert
                final IOException e = assertThrows(IOException.class, () -> reader.skip(5));
                assertEquals("Skip after end of file", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("mark() and reset() methods")
    class MarkAndResetTests {

        @Nested
        @DisplayName("when mark is supported")
        class WhenMarkSupported {
            private Reader reader;

            @BeforeEach
            void setUp() {
                // Arrange
                reader = new TestNullReader(100, true, false);
            }

            @Test
            @DisplayName("markSupported() should return true")
            void markSupported_shouldReturnTrue() {
                assertTrue(reader.markSupported());
            }

            @Test
            @DisplayName("reset() before mark() should throw IOException")
            void reset_beforeMark_shouldThrowIOException() {
                final IOException e = assertThrows(IOException.class, reader::reset);
                assertEquals("No position has been marked", e.getMessage());
            }

            @Test
            @DisplayName("reset() should return stream to the marked position")
            void reset_shouldReturnToMarkedPosition() throws IOException {
                // Arrange
                for (int i = 0; i < 3; i++) {
                    reader.read(); // Advance to position 3
                }
                reader.mark(10); // Mark at position 3

                // Act: Read a few more characters then reset
                assertEquals(3, reader.read());
                assertEquals(4, reader.read());
                reader.reset();

                // Assert
                assertEquals(3, reader.read(), "First read after reset should be from marked position");
                assertEquals(4, reader.read());
            }

            @Test
            @DisplayName("reset() after exceeding read limit should throw IOException")
            void reset_afterExceedingReadLimit_shouldThrowIOException() throws IOException {
                // Arrange
                final int markedPosition = 3;
                final int readLimit = 5;
                for (int i = 0; i < markedPosition; i++) {
                    reader.read();
                }
                reader.mark(readLimit);

                // Act: Read past the read limit
                for (int i = 0; i < readLimit + 1; i++) {
                    reader.read();
                }

                // Assert
                final IOException e = assertThrows(IOException.class, reader::reset);
                assertEquals("Marked position [" + markedPosition + "] is no longer valid - passed the read limit [" + readLimit + "]",
                    e.getMessage());
            }
        }

        @Nested
        @DisplayName("when mark is not supported")
        class WhenMarkNotSupported {
            private Reader reader;

            @BeforeEach
            void setUp() {
                // Arrange
                reader = new TestNullReader(100, false, true);
            }

            @Test
            @DisplayName("markSupported() should return false")
            void markSupported_shouldReturnFalse() {
                assertFalse(reader.markSupported());
            }

            @Test
            @DisplayName("mark() should throw UnsupportedOperationException")
            void mark_shouldThrowException() {
                final UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, () -> reader.mark(5));
                assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage());
            }

            @Test
            @DisplayName("reset() should throw UnsupportedOperationException")
            void reset_shouldThrowException() {
                final UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, reader::reset);
                assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage());
            }
        }
    }
}