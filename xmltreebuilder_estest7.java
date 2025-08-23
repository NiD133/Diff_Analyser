package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the XmlTreeBuilder class, focusing on node insertion logic.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that insertCommentFor() correctly adds a comment node
     * as a child of the current element in the parse tree.
     */
    @Test
    public void insertCommentForAddsCommentToCurrentElement() {
        // Arrange: Set up the builder and parse an initial tag to create a context.
        // This leaves the <root> element as the current element on the builder's stack.
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document doc = xmlBuilder.parse("<root>", "http://example.com/");
        Element root = doc.child(0);

        // Create the comment token to be inserted.
        Token.Comment commentToken = new Token.Comment();
        String commentText = "This is a test comment";
        commentToken.data(commentText);

        // Act: Insert the comment using the method under test.
        xmlBuilder.insertCommentFor(commentToken);

        // Assert: Verify that the comment was added as a child of the <root> element.
        assertEquals("The root element should now have one child node.", 1, root.childNodeSize());

        Node insertedNode = root.childNode(0);
        assertTrue("The inserted node should be an instance of Comment.", insertedNode instanceof Comment);

        Comment insertedComment = (Comment) insertedNode;
        assertEquals("The comment's data should match the token's data.", commentText, insertedComment.getData());
    }
}