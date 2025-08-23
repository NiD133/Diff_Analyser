package org.jsoup.parser;

import org.junit.Test;

import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link XmlTreeBuilder} class.
 * This test focuses on input validation for the initialization phase.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that initialiseParse() throws an IllegalArgumentException when the input Reader is null.
     * This ensures the method correctly validates its inputs and fails fast with a clear error message,
     * preventing potential NullPointerExceptions later in the parsing process.
     */
    @Test
    public void initialiseParseWithNullReaderThrowsIllegalArgumentException() {
        // Arrange: Create the necessary objects for the test.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = Parser.xmlParser();
        String baseUri = "https://example.com/";
        Reader nullReader = null;

        // Act & Assert: Attempt the invalid operation and verify the expected exception.
        try {
            xmlTreeBuilder.initialiseParse(nullReader, baseUri, parser);
            fail("Expected an IllegalArgumentException to be thrown for a null input reader.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected, informative message.
            assertEquals("The parameter 'input' must not be null.", e.getMessage());
        }
    }
}