package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;

/**
 * Tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Tests that the read(char[], int, int) method correctly reads a single character
     * into the specified offset of a buffer.
     */
    @Test
    public void readWithOffsetAndLengthShouldReadIntoCorrectBufferPosition() throws IOException {
        // Arrange: Set up a SequenceReader with a single source reader.
        StringReader sourceReader = new StringReader("directoryFilter");
        SequenceReader sequenceReader = new SequenceReader(Collections.singletonList(sourceReader));

        char[] buffer = new char[3];
        int offset = 1;
        int lengthToRead = 1;

        // Act: Attempt to read one character into the middle of the buffer.
        int charsRead = sequenceReader.read(buffer, offset, lengthToRead);

        // Assert: Verify that exactly one character was read and placed correctly.
        assertEquals("The number of characters read should be 1.", 1, charsRead);

        // The first character 'd' should be at index 1. The rest of the buffer
        // should remain untouched (i.e., contain the default char value '\u0000').
        char[] expectedBuffer = {'\u0000', 'd', '\u0000'};
        assertArrayEquals("The buffer content is incorrect.", expectedBuffer, buffer);
    }
}