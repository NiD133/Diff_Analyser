package org.jsoup.parser;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import static org.junit.Assert.*;

/**
 * Test suite for {@link XmlTreeBuilder}, focusing on I/O error handling.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that when the underlying Reader throws an IOException during parsing,
     * the XmlTreeBuilder wraps it in an UncheckedIOException.
     */
    @Test
    public void parseWithFailingReaderShouldThrowUncheckedIOException() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String expectedErrorMessage = "Simulated I/O error";
        String baseUri = "http://example.com";

        // Create a custom Reader that always throws an IOException to simulate a read failure.
        Reader faultyReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException(expectedErrorMessage);
            }

            @Override
            public void close() {
                // No-op
            }
        };

        // Act & Assert
        try {
            xmlTreeBuilder.parse(faultyReader, baseUri);
            fail("An UncheckedIOException should have been thrown due to the reader's failure.");
        } catch (UncheckedIOException e) {
            // Verify that the thrown exception is a wrapper around the original IOException.
            Throwable cause = e.getCause();
            assertNotNull("The UncheckedIOException should have a cause.", cause);
            assertTrue("The cause should be an instance of IOException.", cause instanceof IOException);
            assertEquals("The cause's message should match the original error.", expectedErrorMessage, cause.getMessage());
        }
    }
}