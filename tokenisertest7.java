package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the {@link Tokeniser}, focusing on its handling of specific edge cases.
 */
public class TokeniserTest {

    /**
     * Tests the parser's ability to handle a title text that is longer than the internal
     * character reader's buffer. This is a white-box test to ensure that the tokeniser
     * correctly accumulates character tokens across multiple buffer reads without losing data.
     */
    @Test
    void parsesTitleLongerThanBufferCorrectly() {
        // Arrange: Create a title string that is intentionally longer than the parser's internal buffer.
        final String titleFragment = "A very, very long title ";
        final StringBuilder titleBuilder = new StringBuilder(BufferSize * 2);
        while (titleBuilder.length() <= BufferSize) {
            titleBuilder.append(titleFragment);
        }
        final String expectedTitle = titleBuilder.toString();
        final String html = "<title>" + expectedTitle + "</title>";

        // Act: Parse the HTML containing the long title.
        Document doc = Jsoup.parse(html);

        // Assert: Verify that the title was parsed correctly, despite its length.
        Element titleElement = doc.selectFirst("title");
        assertNotNull(titleElement, "The <title> element should be found.");

        // 1. Check the title content through the high-level Document.title() accessor.
        assertEquals(expectedTitle, doc.title(), "Document's title should match the expected long string.");

        // 2. Check the title content directly from the element's text.
        assertEquals(expectedTitle, titleElement.text(), "The <title> element's text should match.");

        // 3. Verify the underlying TextNode to ensure its integrity.
        assertTrue(titleElement.childNode(0) instanceof TextNode, "The first child of <title> should be a TextNode.");
        TextNode titleTextNode = (TextNode) titleElement.childNode(0);
        assertEquals(expectedTitle, titleTextNode.getWholeText(), "The TextNode's content should match.");
    }
}