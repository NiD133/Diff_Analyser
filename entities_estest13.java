package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedWriter;

import static org.junit.Assert.*;

/**
 * Test suite for {@link Entities}.
 * This specific test focuses on exception handling during the escape process.
 */
public class EntitiesExceptionTest {

    /**
     * Verifies that when {@link Entities#escape(Appendable, String, Document.OutputSettings, int)}
     * attempts to write to an Appendable that throws an {@link IOException},
     * the exception is correctly wrapped and re-thrown as a {@link RuntimeException}.
     */
    @Test
    public void escapeToFailingAppendableRethrowsIOExceptionAsRuntimeException() {
        // Arrange: Set up an Appendable that will fail on any write operation.
        // A PipedWriter that is not connected to a PipedReader will throw an
        // IOException("Pipe not connected") upon the first write attempt.
        // The QuietAppendable wrapper is expected to catch this and re-throw it as a RuntimeException.
        PipedWriter unconnectedWriter = new PipedWriter();
        Appendable failingAppendable = QuietAppendable.wrap(unconnectedWriter);
        
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String inputToEscape = "text < to > escape"; // Content is irrelevant as the write will fail immediately.
        int arbitraryEscapeOptions = 0; // The specific options do not affect this exception-handling test.

        // Act & Assert
        try {
            Entities.escape(failingAppendable, inputToEscape, outputSettings, arbitraryEscapeOptions);
            fail("A RuntimeException should have been thrown because the underlying writer failed.");
        } catch (RuntimeException e) {
            // Verify that the cause of the RuntimeException is the expected IOException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue("The cause should be an IOException.", cause instanceof IOException);
            assertEquals("The IOException message is incorrect.", "Pipe not connected", cause.getMessage());
        }
    }
}