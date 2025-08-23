package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Tokeniser} class, focusing on tag creation.
 */
public class TokeniserTest {

    @Test
    public void createTagPendingWithFalseArgumentShouldCreateAnEndTag() {
        // Arrange: A Tokeniser requires a TreeBuilder for its construction.
        // For this test, an XmlTreeBuilder is used, but no input needs to be parsed.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call createTagPending with 'false' to request an end tag.
        Token.Tag pendingTag = tokeniser.createTagPending(false);

        // Assert: Verify that a non-null end tag was created.
        assertNotNull("The created pending tag should not be null.", pendingTag);
        assertTrue("The tag should be an end tag.", pendingTag.isEndTag());
        assertFalse("The tag should not be a start tag.", pendingTag.isStartTag());
    }
}