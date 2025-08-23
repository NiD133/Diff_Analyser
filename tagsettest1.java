package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link TagSet}, focusing on dynamic tag creation and caching.
 */
public class TagSetTest {

    /**
     * Verifies that when a new tag is created dynamically (e.g., by renaming an element)
     * with case-preservation enabled, the new Tag object is correctly configured and
     * cached in the parser's TagSet.
     */
    @Test
    void dynamicTagCreationWithCasePreservationIsCorrect() {
        // Arrange: Setup a parser that preserves case and has a known set of HTML tags.
        Parser parser = Parser.htmlParser().settings(ParseSettings.preserveCase);
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", parser);
        TagSet tagSet = parser.tagSet();

        // Pre-conditions: Verify that a standard tag is known and the custom tag does not yet exist.
        assertTrue(tagSet.get("p", NamespaceHtml).isKnownTag(), "Pre-condition: 'p' should be a known tag.");
        assertNull(tagSet.get("FOO", NamespaceHtml), "Pre-condition: 'FOO' should not exist in the tag set yet.");

        // Act: Dynamically create a new tag by renaming an existing element.
        // This action should trigger the creation and caching of a new Tag object.
        Element pElement = doc.expectFirst("p");
        pElement.tagName("FOO");
        Tag newFooTag = pElement.tag();

        // Assert: The new tag has the correct properties and is now cached in the TagSet.
        assertAll("Verify properties of the new 'FOO' tag",
            () -> assertEquals("FOO", newFooTag.name(), "Tag name should be preserved as 'FOO'."),
            () -> assertEquals("foo", newFooTag.normalName(), "Normalized name should be 'foo'."),
            () -> assertEquals(NamespaceHtml, newFooTag.namespace(), "Namespace should be HTML."),
            () -> assertFalse(newFooTag.isKnownTag(), "A dynamically created tag should not be marked as 'known'.")
        );

        assertAll("Verify the TagSet is updated correctly",
            () -> assertSame(newFooTag, tagSet.get("FOO", NamespaceHtml), "The new tag should be retrievable via get()."),
            () -> assertSame(newFooTag, tagSet.valueOf("FOO", NamespaceHtml), "The new tag should be retrievable via valueOf()."),
            () -> assertNull(tagSet.get("FOO", "SomeOtherNamespace"), "The tag should not exist in a different namespace.")
        );
    }
}