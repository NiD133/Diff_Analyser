package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XML parser's handling of malformed input.
 */
public class XmlParserTest {

    /**
     * Tests that the XML parser can gracefully handle a string containing an unclosed tag.
     * This is a test for robustness against malformed input.
     */
    @Test
    public void gracefullyHandlesUnclosedTagInXml() {
        // Arrange: A malformed XML string with an unclosed tag and an unescaped ampersand.
        String malformedXml = "{O_&<vinI";

        // Act: Parse the string using the XML parser.
        Document doc = Jsoup.parse(malformedXml, "", Parser.xmlParser());

        // Assert: The parser should not crash. It should treat the text as a text node
        // (with the ampersand escaped) and the unclosed tag as a self-closing element.
        String expectedOutput = "{O_&amp;<vinI />";
        assertEquals(expectedOutput, doc.outerHtml());
    }
}