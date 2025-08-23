package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readLine() correctly handles a pushed-back newline character.
     * <p>
     * When a newline is pushed back, readLine() should consume that character,
     * return an empty string, and not advance the file pointer in the underlying data source.
     */
    @Test
    public void readLineWithPushedBackNewlineDoesNotAdvanceFilePointer() throws IOException {
        // Arrange: Create a data source and push back a newline character.
        byte[] sourceData = new byte[]{ 'a', 'b', 'c', 'd' };
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Pre-condition: Ensure the file pointer is at the beginning.
        assertEquals("Initial file pointer should be at the start.", 0L, fileOrArray.getFilePointer());

        fileOrArray.pushBack((byte) '\n');

        // Act: Read a line. This should consume only the pushed-back character.
        String line = fileOrArray.readLine();

        // Assert: The line should be empty and the pointer should not have moved.
        assertEquals("The line should be empty when the first character is a pushed-back newline.", "", line);
        assertEquals("File pointer should not advance after reading a pushed-back character.", 0L, fileOrArray.getFilePointer());
    }
}