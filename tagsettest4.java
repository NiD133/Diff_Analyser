package org.jsoup.parser;

import org.jsoup.nodes.Tag;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behavior of the "known tag" status within {@link Tag} and {@link TagSet}.
 * A "known tag" is one that is part of a defined set (like the default HTML tags) or has been
 * explicitly configured, versus one created on-the-fly for an unknown element.
 */
class KnownTagTest {

    @Test
    void newlyCreatedTagIsNotKnown() {
        // A tag created directly via its constructor is not considered "known" by default.
        // given
        Tag customTag = new Tag("custom");

        // then
        assertFalse(customTag.isKnownTag());
        assertEquals("custom", customTag.name());
        assertEquals(NamespaceHtml, customTag.namespace());
    }

    @Test
    void tagsFromDefaultHtmlSetAreKnown() {
        // Tags that are part of the standard HTML set are "known" by default.
        // given
        TagSet htmlTags = TagSet.Html();

        // when
        Tag brTag = htmlTags.get("br", NamespaceHtml);

        // then
        assertNotNull(brTag);
        assertTrue(brTag.isKnownTag(), "<br> should be a known HTML tag");
        assertSame(brTag, htmlTags.valueOf("br", NamespaceHtml), "valueOf should return the same instance for a known tag");
    }

    @Test
    void tagCreatedByValueOfForUnknownNameIsNotKnown() {
        // When TagSet.valueOf() encounters a tag name not in the set, it creates a new Tag
        // instance that is not "known".
        // given
        TagSet htmlTags = TagSet.Html();

        // when
        Tag unknownTag = htmlTags.valueOf("foo", NamespaceHtml);

        // then
        assertFalse(unknownTag.isKnownTag(), "A tag for an unknown name should not be known");
    }



    @Test
    void addMakesTagKnownInSet() {
        // Explicitly adding a tag to a TagSet makes it "known" within that set.
        // given
        TagSet tags = TagSet.Html();
        Tag customTag = new Tag("custom");
        assertFalse(customTag.isKnownTag(), "Tag should not be known before being added to the set");

        // when
        tags.add(customTag);

        // then
        assertTrue(customTag.isKnownTag(), "Tag should be known after being added");
        assertSame(customTag, tags.get("custom", NamespaceHtml), "get() should return the added instance");
        assertSame(customTag, tags.valueOf("custom", NamespaceHtml), "valueOf() should return the added instance");
    }

    @Test
    void valueOfForKnownTagWithDifferentCaseFindsSameKnownTag() {
        // For case-insensitive tag sets like HTML, valueOf() should find a known tag
        // regardless of case and return the original instance.
        // given
        TagSet tags = TagSet.Html();
        Tag customTag = new Tag("custom");
        tags.add(customTag); // make it known

        // when
        Tag foundTag = tags.valueOf("Custom", NamespaceHtml);

        // then
        assertTrue(foundTag.isKnownTag());
        assertSame(customTag, foundTag, "Should return the same instance for a case-insensitive lookup");
    }

    @Test
    void modifyingPropertiesCanChangeKnownStatus() {
        // The "known" status can also be affected by direct property manipulation on the Tag.
        // given a new tag
        Tag tag = new Tag("bar");
        assertFalse(tag.isKnownTag(), "A new tag should not be known initially");

        // when: setting a property
        tag.set(Tag.Block);
        // then: it becomes known
        assertTrue(tag.isKnownTag(), "Setting a property should make the tag known");

        // when: clearing that property
        tag.clear(Tag.Block);
        // then: it remains known
        assertTrue(tag.isKnownTag(), "Clearing a regular property should not affect the 'known' status");

        // when: explicitly clearing the Known property
        tag.clear(Tag.Known);
        // then: it becomes not known
        assertFalse(tag.isKnownTag(), "Explicitly clearing the 'Known' property should make it not known");
    }
}