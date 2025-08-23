package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Understandable tests for the {@link SequenceReader} class, focusing on
 * reading into a portion of a character array.
 */
public class SequenceReaderTest {

    @Test
    @DisplayName("read(char[], int, int) should read from multiple sources sequentially")
    void read_intoCharArrayPortion_readsSequentiallyAndHandlesEof() throws IOException {
        // Arrange
        final String input1 = "Foo";
        final String input2 = "Bar";
        final char[] buffer = new char[10]; // Initialized with '\0' characters

        try (Reader reader = new SequenceReader(new StringReader(input1), new StringReader(input2))) {
            // Act 1: Read the first part ("Foo") into the middle of the buffer.
            final int charsRead1 = reader.read(buffer, 3, 3);

            // Assert 1: Verify "Foo" was read correctly.
            assertEquals(3, charsRead1, "Should have read 3 chars from the first reader.");
            char[] expectedBuffer1 = new char[10];
            System.arraycopy(input1.toCharArray(), 0, expectedBuffer1, 3, input1.length());
            assertArrayEquals(expectedBuffer1, buffer, "Buffer should contain 'Foo' at offset 3.");

            // Act 2: Read the second part ("Bar") into the start of the buffer.
            final int charsRead2 = reader.read(buffer, 0, 3);

            // Assert 2: Verify "Bar" was read, overwriting the start of the buffer.
            assertEquals(3, charsRead2, "Should have read 3 chars from the second reader.");
            char[] expectedBuffer2 = expectedBuffer1.clone(); // Start with the previous buffer state
            System.arraycopy(input2.toCharArray(), 0, expectedBuffer2, 0, input2.length());
            assertArrayEquals(expectedBuffer2, buffer, "Buffer should be updated with 'Bar' at offset 0.");

            // Act 3: Attempt to read past the end of the sequence.
            final int eof = reader.read(buffer);

            // Assert 3: Verify end-of-file is reached.
            assertEquals(-1, eof, "Should return -1 to indicate the end of the sequence.");
        }
    }

    @Test
    @DisplayName("read(char[], int, int) should throw IndexOutOfBoundsException for invalid bounds")
    void read_intoCharArrayPortion_throwsIndexOutOfBoundsForInvalidBounds() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("test"))) {
            final char[] buffer = new char[10];

            // Assert that calling read with a negative offset throws an exception.
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, -1, 1),
                "Offset cannot be negative.");

            // Assert that calling read with a negative length throws an exception.
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 0, -1),
                "Length cannot be negative.");

            // Assert that calling read where offset + length exceeds buffer size throws an exception.
            assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 6, 5),
                "Offset + length cannot exceed buffer length.");
        }
    }

    @Test
    @DisplayName("read(char[], int, int) should throw NullPointerException for a null buffer")
    void read_intoCharArrayPortion_throwsNullPointerExceptionForNullBuffer() throws IOException {
        try (Reader reader = new SequenceReader(new StringReader("test"))) {
            // Assert that calling read with a null buffer throws an exception.
            assertThrows(NullPointerException.class, () -> reader.read(null, 0, 1),
                "Buffer cannot be null.");
        }
    }
}