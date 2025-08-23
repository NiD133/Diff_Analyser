package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that the read(buffer, offset, length) method correctly reads a single
     * character into the buffer at the specified offset.
     */
    @Test
    public void readIntoBufferWithOffsetShouldPlaceCharacterCorrectly() throws IOException {
        // Arrange
        final String inputData = "test_string";
        final Reader sourceReader = new StringReader(inputData);
        
        // The bound is intentionally set high to not interfere with the read operation.
        final BoundedReader boundedReader = new BoundedReader(sourceReader, 100);

        final char[] buffer = new char[5];
        final int offset = 2;
        final int lengthToRead = 1;

        // Act
        final int charsRead = boundedReader.read(buffer, offset, lengthToRead);

        // Assert
        // 1. Verify that the correct number of characters was read.
        assertEquals("Should have read exactly one character.", 1, charsRead);

        // 2. Verify the contents of the buffer.
        // The first character 't' from "test_string" should be at index 2.
        // The rest of the buffer should remain untouched (filled with null characters).
        char[] expectedBuffer = new char[]{'\0', '\0', 't', '\0', '\0'};
        assertArrayEquals("The read character should be at the correct offset in the buffer.", expectedBuffer, buffer);
    }
}