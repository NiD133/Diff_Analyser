package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;

/**
 * This test class contains tests for the XmlTreeBuilder.
 * This specific test focuses on the behavior of the initialiseParse method.
 */
// The original class name is kept to match the context provided.
public class XmlTreeBuilder_ESTestTest9 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that initialiseParse correctly sets the default XML namespace
     * on both the builder and its associated parser, even when given unusual input.
     * The standard XML namespace should always be set by default for an XML parse.
     */
    @Test
    public void initialiseParseSetsDefaultXmlNamespaceRegardlessOfInput() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser xmlParser = Parser.xmlParser();

        // Use an input string that resembles a namespace attribute but is not a valid one.
        // This ensures the default namespace setting is robust and not affected by input content.
        String trickyInput = "xmlns:#text";
        StringReader inputReader = new StringReader(trickyInput);
        String baseUri = trickyInput; // The original test used the input as the base URI.

        String expectedDefaultNamespace = "http://www.w3.org/XML/1998/namespace";

        // Act
        xmlTreeBuilder.initialiseParse(inputReader, baseUri, xmlParser);

        // Assert
        // The tree builder should adopt the standard XML default namespace upon initialization.
        assertEquals("The XmlTreeBuilder should have the default XML namespace.",
                expectedDefaultNamespace, xmlTreeBuilder.defaultNamespace());

        // The parser configured by the builder should also have the correct default namespace.
        assertEquals("The Parser should have the default XML namespace.",
                expectedDefaultNamespace, xmlParser.defaultNamespace());
    }
}