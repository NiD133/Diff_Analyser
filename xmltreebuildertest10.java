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
     * Tests that the XML parser can gracefully handle an End-Of-File (EOF)
     * that occurs in the middle of a tag's attribute.
     */
    @Test
    void handlesEofWithinTagGracefully() {
        // Arrange: Input XML with an unclosed tag and an unterminated attribute at the very end.
        String malformedXml = "<img src=asdf onerror=\"alert(1)\" x=";
        String expectedXml = "<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>";

        // Act: Parse the input using the XML parser.
        Document doc = Jsoup.parse(malformedXml, "", Parser.xmlParser());
        String actualXml = doc.html();

        // Assert: The parser should correctly quote the unquoted attribute, provide an empty
        // value for the unterminated attribute, and close the tag.
        assertEquals(expectedXml, actualXml);
    }
}