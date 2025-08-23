package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link org.jsoup.nodes.Comment#asXmlDeclaration()} method.
 * Note: This test focuses on behavior within the Comment class, not the Tokeniser.
 */
public class CommentAsXmlDeclarationTest {

    @Test
    public void asXmlDeclarationShouldReturnNullForCommentNotContainingAValidXmlDeclaration() {
        // Arrange: Create a Comment node whose content starts with a '?' but is not a
        // valid XML declaration. The asXmlDeclaration() method specifically checks for
        // this pattern.
        String invalidXmlDeclContent = "? this is not a valid xml declaration";
        Comment comment = new Comment(invalidXmlDeclContent);

        // Act: Attempt to convert the comment to an XML declaration.
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Assert: The result should be null because the comment's content does not
        // represent a valid XML declaration.
        assertNull("A comment that is not a valid XML declaration should not be converted.", xmlDeclaration);
    }
}