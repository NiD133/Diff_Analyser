package org.jsoup.parser;

import org.junit.Test;
import java.io.IOException;
import java.io.PipedReader;
import java.io.UncheckedIOException;
import static org.junit.Assert.*;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class focuses on specific test cases for better clarity.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that initialiseParse throws an UncheckedIOException when the provided
     * Reader fails during an I/O operation. This is simulated using an unconnected
     * PipedReader, which throws an IOException upon the first read attempt.
     */
    @Test
    public void initialiseParseWithFaultyReaderThrowsUncheckedIOException() {
        // Arrange: Set up a builder and a reader that is guaranteed to fail.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        
        // An unconnected PipedReader will throw an IOException on any read attempt.
        PipedReader faultyReader = new PipedReader();
        String baseUri = "http://example.com";

        // Act & Assert: Ensure the method call throws the expected exception and wraps the correct cause.
        try {
            xmlTreeBuilder.initialiseParse(faultyReader, baseUri, parser);
            fail("Expected an UncheckedIOException to be thrown due to the faulty reader.");
        } catch (UncheckedIOException e) {
            // The method is expected to wrap the underlying IOException from the reader.
            assertNotNull("The UncheckedIOException should have a cause.", e.getCause());
            assertTrue("The cause should be an instance of IOException.", e.getCause() instanceof IOException);
            assertEquals("Pipe not connected", e.getCause().getMessage());
        }
    }
}