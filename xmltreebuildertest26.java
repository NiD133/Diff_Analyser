package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for the internal state management of the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the parser clears its internal state (reader and tokeniser) after a parse operation.
     * This is crucial for preventing resource leaks and ensuring the parser instance can be safely reused
     * without carrying over state from a previous run.
     */
    @Test
    public void parserClearsInternalStateAfterParse() {
        // Arrange
        String xmlToParse = "<data>Some XML</data>";
        Parser parser = Parser.xmlParser();

        // Act: Parse the XML string. The Jsoup.parse method uses and modifies the state of the provided parser instance.
        Jsoup.parse(xmlToParse, "", parser);

        // Assert: After parsing, the internal TreeBuilder should have its state cleared.
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNull(treeBuilder.reader, "The internal reader should be null after parsing is complete.");
        assertNull(treeBuilder.tokeniser, "The internal tokeniser should be null after parsing is complete.");
    }
}