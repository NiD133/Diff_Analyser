package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML parser should not treat HTML void tags like <br> as self-closing")
    void xmlParserShouldNotSelfCloseHtmlVoidTags() {
        // This test verifies that the XML parser, unlike the HTML parser, does not have
        // special knowledge of HTML's "void" or "self-closing" tags.
        String input = "<br>one</br>";

        // Part 1: Demonstrate HTML parser behavior for context.
        // The HTML parser recognizes <br> as a void tag and auto-corrects the mismatched </br>.
        Document htmlDoc = Jsoup.parse(input);
        String expectedHtmlOutput = "<br>\none\n<br>";
        assertEquals(expectedHtmlOutput, htmlDoc.body().html(), "For context: HTML parser should 'fix' the input");

        // Part 2: Test the XML parser's behavior (the actual subject of the test).
        // The XML parser should treat <br> as a standard tag that requires a proper closing tag.
        // It should preserve the original structure without any special handling.
        Document xmlDoc = Jsoup.parse(input, "", Parser.xmlParser());
        String expectedXmlOutput = "<br>one</br>";
        assertEquals(expectedXmlOutput, xmlDoc.html(), "XML parser should preserve the tag structure");
    }
}