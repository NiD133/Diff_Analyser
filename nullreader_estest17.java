package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    @Test
    public void skipAfterEOFShouldThrowIOException() throws IOException {
        // Arrange: Create a NullReader and advance it to the end of the file.
        // The default constructor creates a reader with a size of 0.
        final NullReader reader = new NullReader();
        
        // A single read() call on a zero-size reader sets its internal EOF flag.
        reader.read();

        // Act & Assert
        try {
            // Attempting to skip after the stream is at EOF should fail.
            // The negative value is incidental; any skip call would fail here.
            reader.skip(-1);
            fail("Expected an IOException to be thrown when skipping after EOF.");
        } catch (final IOException e) {
            // Verify that the correct exception was thrown.
            final String expectedMessage = "Skip after end of file";
            assertEquals("The exception message should match the expected text.",
                         expectedMessage, e.getMessage());
        }
    }
}