package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on exception handling.
 */
public class DefaultIndenterTest {

    /**
     * Tests that if the underlying JsonGenerator throws an IOException during writing,
     * the writeIndentation() method correctly propagates this exception.
     */
    @Test
    public void writeIndentation_whenGeneratorThrowsIoException_shouldPropagateException() throws Exception {
        // Arrange: Create a JsonGenerator that is guaranteed to fail on any write operation.
        final String expectedErrorMessage = "Simulated I/O error from writer";

        Writer failingWriter = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException(expectedErrorMessage);
            }

            @Override
            public void flush() { /* No-op for this test */ }

            @Override
            public void close() { /* No-op for this test */ }
        };

        JsonFactory factory = new JsonFactory();
        // The generator will use our failingWriter
        JsonGenerator failingGenerator = factory.createGenerator(failingWriter);
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;

        // Act & Assert
        try {
            // Use an indentation level > 16 to ensure the indenter attempts to write.
            // The internal cache in DefaultIndenter holds up to 16 levels of indentation.
            indenter.writeIndentation(failingGenerator, 20);
            fail("Expected an IOException to be thrown by the generator");
        } catch (IOException e) {
            // Verify that the propagated exception is the one we simulated.
            assertEquals(expectedErrorMessage, e.getMessage());
        } finally {
            // Ensure resources are closed, even though the generator is a mock.
            if (failingGenerator != null) {
                failingGenerator.close();
            }
        }
    }
}