package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedWriter;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable} to ensure it correctly handles I/O exceptions.
 */
public class TextHelpAppendableIoExceptionTest {

    @Test
    public void writeColumnQueues_shouldPropagateIOException_whenUnderlyingWriterFails() {
        // Arrange: Create a TextHelpAppendable with an underlying writer that is known
        // to fail. A PipedWriter that is not connected to a PipedReader will throw an
        // IOException upon any write attempt.
        final PipedWriter failingWriter = new PipedWriter();
        final TextHelpAppendable textHelpAppendable = new TextHelpAppendable(failingWriter);

        // The actual data for columns and styles is not relevant to this test,
        // as the write operation will fail immediately. Empty lists are sufficient.
        final List<Queue<String>> emptyColumnQueues = Collections.emptyList();
        final List<TextStyle> emptyStyles = Collections.emptyList();

        // Act & Assert
        try {
            textHelpAppendable.writeColumnQueues(emptyColumnQueues, emptyStyles);
            fail("Expected an IOException to be thrown because the underlying PipedWriter is not connected.");
        } catch (final IOException e) {
            // Verify that the expected IOException is caught and that its message
            // confirms the cause of the failure originated from the PipedWriter.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}