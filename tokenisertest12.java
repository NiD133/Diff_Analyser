package org.jsoup.parser;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Contains a test case for the Tokeniser, focusing on its ability to handle
 * edge cases related to buffer boundaries.
 */
@DisplayName("Tokeniser Edge Cases")
public class TokeniserTestTest12 {

    @Test
    @DisplayName("Should correctly parse CDATA when its end tag spans a buffer boundary")
    public void shouldCorrectlyParseCdataWhenEndTagSpansBufferBoundary() {
        // Arrange: Create markup where the CDATA end tag "]]>" is split across the
        // CharacterReader's internal buffer boundary. This verifies that the tokeniser
        // correctly manages its state when the buffer is refilled at a critical point.

        final String cdataStart = "<![CDATA[";
        final String cdataEnd = "]]>";

        // To create the edge case, we craft the input so the first ']' of the "]]>"
        // end tag is the very last character in the first buffer read.
        // The length of the content preceding the end tag must be (BufferSize - 1).
        // Therefore, the CDATA content length is calculated as:
        final int cdataContentLength = BufferSize - cdataStart.length() - 1;

        // Generate the CDATA content.
        char[] contentChars = new char[cdataContentLength];
        Arrays.fill(contentChars, 'x');
        final String expectedCdataContent = new String(contentChars);

        // Assemble the final HTML to be parsed.
        final String html = cdataStart + expectedCdataContent + cdataEnd;

        // Act: Parse the crafted HTML input.
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, "");
        Node firstChild = document.body().childNode(0);

        // Assert: Verify that the parsed node is a CDataNode and its content is intact.
        CDataNode cdataNode = assertInstanceOf(CDataNode.class, firstChild,
                "The parsed node should be a CDataNode.");
        assertEquals(expectedCdataContent, cdataNode.text(),
                "The CDATA content should be extracted correctly without corruption.");
    }
}