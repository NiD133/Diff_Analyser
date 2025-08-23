package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SequenceReader}.
 * This class contains the improved test case.
 */
public class SequenceReaderTest {

    /**
     * Tests that calling read() with a buffer and negative values for offset and length
     * correctly throws an IndexOutOfBoundsException.
     */
    @Test
    public void readWithBufferShouldThrowExceptionForNegativeOffsetAndLength() {
        // Arrange: Create a SequenceReader and a buffer for reading.
        // The reader can be empty, as the exception is thrown before any reading occurs.
        SequenceReader sequenceReader = new SequenceReader(Collections.<Reader>emptyList());
        char[] buffer = new char[7];
        int invalidOffset = -1;
        int invalidLength = -1;

        // Act & Assert
        try {
            sequenceReader.read(buffer, invalidOffset, invalidLength);
            fail("Expected an IndexOutOfBoundsException to be thrown for negative offset and length.");
        } catch (IndexOutOfBoundsException e) {
            // Success: The expected exception was caught.
            // Verify the exception message contains details about the invalid arguments.
            String expectedMessageContent = "Array Size=7, offset=-1, length=-1";
            assertTrue(
                "The exception message should contain the invalid offset and length.",
                e.getMessage().contains(expectedMessageContent)
            );
        } catch (IOException e) {
            // The read method signature includes IOException, but it's not expected in this specific scenario.
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}