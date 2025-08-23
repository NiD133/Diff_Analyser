package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Tokeniser's handling of CDATA sections.
 */
public class TokeniserTestTest6 {

    /**
     * Tests if the parser correctly handles a CDATA section that is larger than its internal buffer.
     * This ensures that the tokeniser does not truncate or corrupt data when its buffer is refilled
     * during the parsing of a large CDATA block.
     */
    @Test
    public void cdataSectionLargerThanBufferIsParsedCorrectly() {
        // Arrange: Create a CDATA content string that is intentionally larger than the parser's internal buffer
        // to test the buffer-refilling logic.
        StringBuilder cdataBuilder = new StringBuilder(BufferSize * 2);
        String cdataSample = "Quite a lot of CDATA <><><><>";
        while (cdataBuilder.length() < BufferSize * 1.5) {
            cdataBuilder.append(cdataSample);
        }
        String largeCdataContent = cdataBuilder.toString();
        String htmlWithLargeCdata = "<p><![CDATA[" + largeCdataContent + "]]></p>";

        // Act: Parse the HTML containing the large CDATA section.
        Document parsedDocument = Jsoup.parse(htmlWithLargeCdata);

        // Assert: Verify that the CDATA content was parsed completely and correctly.
        Elements pElements = parsedDocument.select("p");
        assertEquals(1, pElements.size(), "Exactly one <p> element should be found.");

        Element pElement = pElements.first();
        assertNotNull(pElement, "The <p> element should not be null.");
        assertEquals(1, pElement.childNodeSize(), "The <p> element should have exactly one child node.");

        Node childNode = pElement.childNode(0);
        assertInstanceOf(TextNode.class, childNode, "The child node should be a TextNode, as CDATA is parsed into text.");

        // Verify the content of the parsed TextNode.
        TextNode cdataNode = (TextNode) childNode;
        assertEquals(largeCdataContent, cdataNode.getWholeText(), "The text content of the node should match the original CDATA content.");
        assertEquals(largeCdataContent, pElement.text(), "The text of the parent element should also match the original CDATA content.");
    }
}