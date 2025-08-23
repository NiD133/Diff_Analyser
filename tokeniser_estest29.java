package org.jsoup.nodes;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link Comment#asXmlDeclaration()} method.
 *
 * Note: This test was originally located in a test suite for the `Tokeniser` class,
 * but its actual subject is the `Comment` node's functionality.
 */
public class CommentTest {

    /**
     * Verifies that asXmlDeclaration() returns null for a Comment node
     * whose content is not a valid XML declaration.
     * A valid XML declaration must start with a '?' or '!' character.
     */
    @Test
    public void asXmlDeclarationReturnsNullForRegularComment() {
        // Arrange: Create a standard comment with content that does not resemble an XML declaration.
        Comment comment = new Comment("/Q");

        // Act: Attempt to interpret the comment as an XML declaration.
        XmlDeclaration declaration = comment.asXmlDeclaration();

        // Assert: The method should return null, as the comment is not a declaration.
        assertNull("A comment with plain text content should not be converted to an XmlDeclaration.", declaration);
    }
}