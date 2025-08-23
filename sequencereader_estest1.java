package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Contains tests for the {@link SequenceReader} class, focusing on its core functionalities.
 */
public class SequenceReaderTest {

    /**
     * Tests that calling skip() with a value larger than the remaining content
     * skips all available characters and returns the number of characters actually skipped.
     */
    @Test
    public void skipPastEndOfStreamShouldReturnTotalCharactersSkipped() throws IOException {
        // Arrange
        String content1 = "Hello";
        String content2 = " World";
        long totalLength = content1.length() + content2.length();
        long charactersToSkip = totalLength + 10; // A number larger than the total content

        Reader reader1 = new StringReader(content1);
        Reader reader2 = new StringReader(content2);
        
        // Use the simpler varargs constructor for SequenceReader
        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader2)) {
            // Act
            long actualSkippedCount = sequenceReader.skip(charactersToSkip);

            // Assert
            assertEquals("Should skip all characters up to the end of the stream", 
                         totalLength, actualSkippedCount);
            
            // Further verify that the stream is at its end
            assertEquals("Stream should be at the end after skipping all characters", 
                         -1, sequenceReader.read());
        }
    }
}