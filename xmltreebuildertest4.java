package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder.
 * This class focuses on verifying the XML parsing behavior, particularly how the parser is supplied
 * to the Jsoup class and handles malformed XML.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("The XML parser should correctly handle an unmatched closing tag")
    public void jsoupParseWithXmlParserHandlesUnmatchedClosingTag() {
        // Arrange: Define an XML string with an unmatched closing tag </bar>.
        // The expectation is that the parser will ignore it.
        String malformedXml = "<doc><val>One<val>Two</val></bar>Three</doc>";
        String expectedOutput = "<doc><val>One<val>Two</val>Three</val></doc>";

        // Act: Parse the malformed XML string using Jsoup, configured with an XML parser.
        Document doc = Jsoup.parse(malformedXml, "", Parser.xmlParser());

        // Assert: Verify that the parser ignored the unmatched </bar> tag
        // and correctly structured the document.
        assertEquals(expectedOutput, TextUtil.stripNewlines(doc.html()));
    }
}