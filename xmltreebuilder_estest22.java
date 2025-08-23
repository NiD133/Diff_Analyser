package org.jsoup.parser;

import org.junit.Test;

/**
 * This class contains tests for the {@link XmlTreeBuilder}.
 * This specific test focuses on the behavior of the parse method with null input.
 */
public class XmlTreeBuilder_ESTestTest22 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that calling the parse() method with a null input string
     * results in a NullPointerException. This is the expected behavior,
     * as the method requires a valid string to create an internal StringReader.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullInputStringShouldThrowNullPointerException() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String nullInput = null;
        String baseUri = "https://example.com/"; // A valid base URI to isolate the cause

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        xmlTreeBuilder.parse(nullInput, baseUri);
    }
}