package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that the get() method can retrieve a known, pre-defined tag
     * from the default HTML TagSet using its name and namespace.
     */
    @Test
    public void getShouldReturnKnownTagFromHtmlSet() {
        // Arrange: Get the default HTML tag set and define the tag to look for.
        TagSet htmlTagSet = TagSet.Html();
        final String tagName = "pre";
        final String htmlNamespace = "http://www.w3.org/1999/xhtml";

        // Act: Attempt to retrieve the <pre> tag.
        Tag preTag = htmlTagSet.get(tagName, htmlNamespace);

        // Assert: Verify that the correct Tag object was returned.
        assertNotNull("The 'pre' tag should exist in the default HTML TagSet.", preTag);
        assertEquals("The tag name should be correct.", tagName, preTag.getName());
        assertTrue("The 'pre' tag should be a block-level element.", preTag.isBlock());
        assertTrue("The 'pre' tag should preserve whitespace.", preTag.preserveWhitespace());
    }
}