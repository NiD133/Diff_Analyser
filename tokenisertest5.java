package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Tokeniser}.
 * This class focuses on specific tokenisation scenarios.
 */
// The original class name 'TokeniserTestTest5' was renamed for clarity and convention.
public class TokeniserTest {

    /**
     * Tests that the parser can handle comments that are larger than the internal read buffer.
     * This ensures that the tokeniser correctly buffers and processes data when a token
     * spans across buffer boundaries, preventing data loss or corruption.
     */
    @Test
    void parsesCommentLargerThanReadBuffer() {
        // Arrange
        // Create a comment string that is deliberately larger than the tokeniser's internal buffer
        // to verify correct buffer management.
        StringBuilder commentBuilder = new StringBuilder(BufferSize * 2);
        while (commentBuilder.length() < BufferSize + 20) {
            commentBuilder.append("This is a part of a very large comment. ");
        }
        String largeCommentText = commentBuilder.toString();
        String html = "<p><!-- " + largeCommentText + " --></p>";
        String expectedCommentData = " " + largeCommentText + " ";

        // Act
        Document doc = Jsoup.parse(html);

        // Assert
        Element pElement = doc.selectFirst("p");
        assertNotNull(pElement, "The <p> element should be parsed successfully.");
        
        assertTrue(pElement.childNodeSize() > 0, "The <p> element should contain a child node.");
        Node childNode = pElement.childNode(0);

        assertInstanceOf(Comment.class, childNode, "The child node of <p> should be a Comment.");
        Comment commentNode = (Comment) childNode;

        assertEquals(expectedCommentData, commentNode.getData(), "The comment data should be parsed completely and correctly.");
    }
}