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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Unit tests for {@link NullReader}.
 */
public class NullReaderTest {

    // =================================================================================
    // Constructor and State Tests
    // =================================================================================

    @Test
    public void defaultConstructor_shouldCreateZeroSizeReader() {
        // Arrange & Act
        final NullReader reader = new NullReader();

        // Assert
        assertEquals("Size should be 0", 0, reader.getSize());
        assertEquals("Position should be 0", 0, reader.getPosition());
        assertTrue("Mark should be supported", reader.markSupported());
    }

    @Test
    public void sizeConstructor_shouldCreateReaderOfSpecifiedSize() {
        // Arrange & Act
        final long size = 100L;
        final NullReader reader = new NullReader(size);

        // Assert
        assertEquals("Size should match constructor argument", size, reader.getSize());
        assertEquals("Position should be 0", 0, reader.getPosition());
        assertTrue("Mark should be supported by default", reader.markSupported());
    }

    @Test
    public void fullConstructor_shouldSetAllProperties() {
        // Arrange & Act
        final long size = 50L;
        final NullReader reader = new NullReader(size, false, true); // mark not supported, throw EOF

        // Assert
        assertEquals("Size should match constructor argument", size, reader.getSize());
        assertFalse("Mark support should be false as specified", reader.markSupported());
    }

    @Test
    public void singletonInstance_shouldBeZeroSizeAndNotThrowEof() throws IOException {
        // Arrange
        final NullReader reader = NullReader.INSTANCE;

        // Assert
        assertEquals("Instance size should be 0", 0, reader.getSize());
        assertTrue("Instance should support mark", reader.markSupported());
        assertEquals("Reading from empty instance should return EOF", -1, reader.read());
    }

    @Test
    public void close_shouldResetReaderState() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10);
        reader.skip(5);
        reader.mark(5);

        // Act
        reader.close();

        // Assert
        assertEquals("Position should be reset to 0", 0, reader.getPosition());
        // A call to reset() after close() should fail as the mark is cleared.
        try {
            reader.reset();
            fail("reset() should fail after close()");
        } catch (final IOException e) {
            assertEquals("Exception message for reset after close", "No position has been marked", e.getMessage());
        }
    }

    // =================================================================================
    // Read Tests
    // =================================================================================

    @Test
    public void readChar_shouldReturnZeroAndAdvancePosition() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10);

        // Act
        final int charRead = reader.read();

        // Assert
        assertEquals("Character read should be 0", 0, charRead);
        assertEquals("Position should advance by 1", 1, reader.getPosition());
    }

    @Test
    public void readChar_shouldReturnEofAtEnd() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(1); // Does not throw EOFException

        // Act
        reader.read(); // Read the only character
        final int result = reader.read(); // Read at EOF

        // Assert
        assertEquals("Should return -1 at EOF", -1, result);
    }

    @Test(expected = EOFException.class)
    public void readChar_shouldThrowEofException_whenPastEofAndThrowingIsEnabled() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(1, true, true); // Throws EOFException

        // Act
        reader.read(); // Read the only character
        reader.read(); // Should throw
    }

    @Test(expected = IOException.class)
    public void readChar_shouldThrowIoException_whenReadingAfterEof() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(1);
        reader.read(); // position becomes 1
        reader.read(); // returns -1, sets eof flag to true

        // Act
        reader.read(); // Should throw IOException("Read after end of file")
    }

    @Test
    public void readArray_shouldFillNothingButAdvancePosition() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(20);
        final char[] buffer = {'a', 'b', 'c', 'd', 'e'};

        // Act
        final int count = reader.read(buffer);

        // Assert
        assertEquals("Should report reading 5 chars", 5, count);
        assertEquals("Position should advance by 5", 5, reader.getPosition());
        assertArrayEquals("Buffer should be unchanged", new char[]{'a', 'b', 'c', 'd', 'e'}, buffer);
    }

    @Test
    public void readArray_shouldReturnPartialCount_whenReadingPastEof() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(3);
        final char[] buffer = new char[5];

        // Act
        final int count = reader.read(buffer);

        // Assert
        assertEquals("Should only read up to size", 3, count);
        assertEquals("Position should be at the end", 3, reader.getPosition());
    }

    @Test
    public void readArray_shouldReturnZero_whenLengthIsZero() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10);

        // Act
        final int count = reader.read(new char[5], 1, 0);

        // Assert
        assertEquals("Should read 0 chars", 0, count);
        assertEquals("Position should not change", 0, reader.getPosition());
    }

    @Test(expected = NullPointerException.class)
    public void readArray_shouldThrowNullPointerException_whenBufferIsNull() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10);

        // Act
        reader.read((char[]) null);
    }

    // =================================================================================
    // Skip Tests
    // =================================================================================

    @Test
    public void skip_shouldAdvancePosition() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(20);

        // Act
        final long skipped = reader.skip(10);

        // Assert
        assertEquals("Should report skipping 10 chars", 10, skipped);
        assertEquals("Position should advance by 10", 10, reader.getPosition());
    }

    @Test
    public void skip_shouldStopAtEof_whenNotThrowingException() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(5);

        // Act
        final long skipped = reader.skip(10);

        // Assert
        assertEquals("Should only skip up to size", 5, skipped);
        assertEquals("Position should be at the end", 5, reader.getPosition());
    }

    @Test(expected = EOFException.class)
    public void skip_shouldThrowEofException_whenPastEofAndThrowingIsEnabled() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(5, true, true);

        // Act
        reader.skip(10); // Should throw
    }

    @Test
    public void skip_shouldHandleNegativeValue() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(20);
        reader.skip(10); // position = 10

        // Act
        final long skipped = reader.skip(-5);

        // Assert
        assertEquals("Should report skipping -5 chars", -5, skipped);
        assertEquals("Position should move backward by 5", 5, reader.getPosition());
    }

    // =================================================================================
    // Mark and Reset Tests
    // =================================================================================

    @Test
    public void reset_shouldReturnToMarkedPosition() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(20);
        reader.skip(10);
        reader.mark(5); // Mark at position 10

        // Act
        reader.skip(5); // Move to position 15
        reader.reset();

        // Assert
        assertEquals("Position should be reset to marked position", 10, reader.getPosition());
    }

    @Test(expected = IOException.class)
    public void reset_shouldThrowIOException_whenNotMarked() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10);

        // Act
        reader.reset(); // Should throw
    }

    @Test(expected = UncheckedIOException.class)
    public void mark_shouldThrowException_whenNotSupported() {
        // Arrange
        final NullReader reader = new NullReader(10, false, false);

        // Act
        // mark() is overridden to throw UncheckedIOException wrapping an UnsupportedOperationException
        reader.mark(5);
    }

    @Test(expected = IOException.class)
    public void reset_shouldThrowException_whenNotSupported() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(10, false, false);

        // Act
        reader.reset();
    }

    @Test(expected = IOException.class)
    public void reset_shouldThrowIoException_whenReadLimitExceeded() throws IOException {
        // Arrange
        final NullReader reader = new NullReader(20);
        reader.skip(5);
        reader.mark(2); // Mark at position 5 with a read limit of 2

        // Act
        reader.skip(3); // Read 3 chars, which is > read limit of 2
        reader.reset(); // Should throw
    }
}