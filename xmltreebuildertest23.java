package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
class XmlTreeBuilderTest {

    /**
     * Tests that the XML parser can gracefully handle an XML declaration
     * that is not explicitly closed with '?&gt;'. This is a common case in
     * malformed or truncated XML streams.
     */
    @Test
    void shouldParseXmlWithUnclosedDeclaration() {
        // Arrange: An XML string with an unclosed declaration.
        String xmlWithUnclosedDecl = "<?xml version='1.0'><val>One</val>";

        // Act: Parse the string using the XML parser.
        Document doc = Jsoup.parse(xmlWithUnclosedDecl, "", Parser.xmlParser());
        String parsedText = doc.select("val").text();

        // Assert: The content should be parsed correctly despite the malformed declaration.
        assertEquals("One", parsedText);
    }
}