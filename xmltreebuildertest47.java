package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the Jsoup XML Tree Builder.
 * This class was refactored from the generated name XmlTreeBuilderTestTest47 for clarity.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML parser should correctly handle self-closing and empty tags")
    void xmlParserHandlesSelfClosingAndEmptyTags() {
        // Arrange
        // In XML, unlike HTML, any tag can be self-closing. This test verifies that the XML parser
        // correctly handles a mix of explicitly self-closed tags (<div/>, <p/>) and standard
        // empty tags (<div></div>, <foo></foo>).
        String xml = "<div id='1'/><p/><div>Foo</div><div></div><foo></foo>";
        Parser parser = Parser.xmlParser().setTrackErrors(10);

        // Act
        Document doc = Jsoup.parse(xml, "", parser);
        ParseErrorList errors = parser.getErrors();

        // Assert
        assertEquals(0, errors.size(), "Parsing should complete without errors for valid XML.");

        // The expected output demonstrates how Jsoup serializes the parsed structure.
        // By default, Jsoup.parse uses an XML parser but may retain HTML-like output settings.
        // This can lead to nuanced behavior:
        // 1. Explicitly self-closed tags like <div/> and <p/> are preserved as such.
        // 2. Empty known HTML block tags like <div></div> are normalized to a self-closing form.
        // 3. Empty unknown tags like <foo></foo> may retain their start and end tags.
        String expectedOutput = "<div id=\"1\" /><p /><div>Foo</div><div /><foo></foo>";
        assertEquals(expectedOutput, TextUtil.stripNewlines(doc.outerHtml()));
    }
}