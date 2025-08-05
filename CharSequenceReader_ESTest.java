/*
 * Copyright (C) 2013 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import org.junit.Test;

/**
 * Understandable tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructor_withNullCharSequence_throwsNullPointerException() {
        // Act
        new CharSequenceReader(null);
    }

    // --- read() Tests ---

    @Test
    public void read_whenCharsAvailable_returnsChar() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abc");

        // Act & Assert
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
    }

    @Test
    public void read_atEndOfStream_returnsMinusOne() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("a");
        reader.read(); // Consume the only character

        // Act & Assert
        assertEquals(-1, reader.read());
    }

    @Test
    public void read_onEmptySource_returnsMinusOne() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("");

        // Act & Assert
        assertEquals(-1, reader.read());
    }

    @Test(expected = IOException.class)
    public void read_afterClose_throwsIOException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");
        reader.close();

        // Act
        reader.read(); // Should throw
    }

    // --- read(char[], int, int) Tests ---

    @Test
    public void readIntoArray_shouldFillArrayAndReturnLength() throws IOException {
        // Arrange
        String sourceText = "abcdef";
        CharSequenceReader reader = new CharSequenceReader(sourceText);
        char[] destination = new char[6];

        // Act
        int charsRead = reader.read(destination, 0, 6);

        // Assert
        assertEquals(6, charsRead);
        assertArrayEquals(sourceText.toCharArray(), destination);
    }

    @Test
    public void readIntoArray_whenSourceIsSmallerThanArray_shouldReadAllAvailableChars() throws IOException {
        // Arrange
        String sourceText = "abc";
        CharSequenceReader reader = new CharSequenceReader(sourceText);
        char[] destination = new char[10];

        // Act
        int charsRead = reader.read(destination, 0, 10);

        // Assert
        assertEquals(3, charsRead);
        // Verify that only the first 3 characters of the destination were written to.
        assertArrayEquals(new char[]{'a', 'b', 'c'}, Arrays.copyOf(destination, 3));
    }

    @Test
    public void readIntoArray_withOffsetAndLength_shouldFillSubArray() throws IOException {
        // Arrange
        String sourceText = "abcdefgh";
        CharSequenceReader reader = new CharSequenceReader(sourceText);
        char[] destination = new char[10];
        Arrays.fill(destination, 'X'); // Pre-fill to check bounds

        // Act: Read 5 chars from source into destination, starting at index 2
        int charsRead = reader.read(destination, 2, 5);

        // Assert
        assertEquals(5, charsRead);
        assertArrayEquals(new char[]{'X', 'X', 'a', 'b', 'c', 'd', 'e', 'X', 'X', 'X'}, destination);
    }

    @Test
    public void readIntoArray_atEndOfStream_returnsMinusOne() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abc");
        reader.read(new char[3]); // Read all content

        // Act
        int charsRead = reader.read(new char[3]);

        // Assert
        assertEquals(-1, charsRead);
    }

    @Test
    public void readIntoArray_withZeroLength_returnsZero() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abc");

        // Act
        int charsRead = reader.read(new char[3], 0, 0);

        // Assert
        assertEquals(0, charsRead);
    }

    @Test(expected = IOException.class)
    public void readIntoArray_afterClose_throwsIOException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");
        char[] destination = new char[4];
        reader.close();

        // Act
        reader.read(destination, 0, 4); // Should throw
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readIntoArray_withInvalidBounds_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act
        reader.read(new char[4], -1, 4); // Negative offset
    }

    // --- read(CharBuffer) Tests ---

    @Test
    public void readIntoCharBuffer_shouldFillBuffer() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abcdef");
        CharBuffer destination = CharBuffer.allocate(10);

        // Act
        int charsRead = reader.read(destination);

        // Assert
        assertEquals(6, charsRead);
        assertEquals(6, destination.position());
        destination.flip();
        assertEquals("abcdef", destination.toString());
    }

    @Test
    public void readIntoCharBuffer_atEndOfStream_returnsMinusOne() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abc");
        CharBuffer destination = CharBuffer.allocate(10);
        reader.read(destination); // Read all content

        // Act
        int charsRead = reader.read(destination);

        // Assert
        assertEquals(-1, charsRead);
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void readIntoCharBuffer_withReadOnlyBuffer_throwsReadOnlyBufferException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");
        CharBuffer readOnlyBuffer = CharBuffer.wrap("destination").asReadOnlyBuffer();

        // Act
        reader.read(readOnlyBuffer);
    }

    // --- skip() Tests ---

    @Test
    public void skip_shouldAdvancePosition() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abcdef");

        // Act
        long charsSkipped = reader.skip(3);

        // Assert
        assertEquals(3L, charsSkipped);
        assertEquals('d', reader.read());
    }

    @Test
    public void skip_pastEndOfStream_shouldSkipToTheEnd() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abc");

        // Act
        long charsSkipped = reader.skip(100);

        // Assert
        assertEquals(3L, charsSkipped);
        assertEquals(-1, reader.read());
    }

    @Test
    public void skip_atEndOfStream_shouldReturnMinusOne() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("a");
        reader.read(); // Consume the only character

        // Act
        long charsSkipped = reader.skip(10); // Try to skip when at the end

        // Assert
        assertEquals(-1L, charsSkipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void skip_withNegativeValue_throwsIllegalArgumentException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act
        reader.skip(-1);
    }

    // --- mark() and reset() Tests ---

    @Test
    public void markSupported_shouldReturnTrue() {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act & Assert
        assertTrue("CharSequenceReader should support mark()", reader.markSupported());
    }

    @Test
    public void markAndReset_shouldReturnToMarkedPosition() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abcdef");
        reader.read(); // pos=1, next is 'b'
        reader.read(); // pos=2, next is 'c'

        // Act
        reader.mark(10); // Mark at position 2 ('c')
        reader.read();   // pos=3, next is 'd'
        reader.read();   // pos=4, next is 'e'
        reader.reset();  // Reset to mark

        // Assert
        assertEquals("Should have reset to the marked character 'c'", 'c', reader.read());
    }

    @Test
    public void reset_withoutMark_shouldResetToStart() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("abcdef");
        reader.skip(3);

        // Act
        reader.reset();

        // Assert
        assertEquals('a', reader.read());
    }

    @Test(expected = IllegalArgumentException.class)
    public void mark_withNegativeReadAheadLimit_throwsIllegalArgumentException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act
        reader.mark(-1);
    }

    @Test(expected = IOException.class)
    public void reset_afterClose_throwsIOException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");
        reader.mark(1);
        reader.close();

        // Act
        reader.reset(); // Should throw
    }

    // --- ready() Tests ---

    @Test
    public void ready_whenOpenAndCharsAvailable_returnsTrue() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act & Assert
        assertTrue(reader.ready());
    }

    @Test
    public void ready_whenOpenAndNoCharsAvailable_returnsTrue() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("");

        // Act & Assert
        // A reader is "ready" even at EOF, as it's ready to return -1.
        assertTrue(reader.ready());
    }

    @Test(expected = IOException.class)
    public void ready_afterClose_throwsIOException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");
        reader.close();

        // Act
        reader.ready(); // Should throw
    }

    // --- close() Tests ---

    @Test
    public void close_canBeCalledMultipleTimes() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test");

        // Act
        reader.close();
        reader.close(); // Should not throw
    }
}