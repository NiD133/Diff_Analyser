package org.jsoup.parser;

import org.jsoup.nodes.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for TagSet customization and the inheritance behavior when a TagSet is copied.
 */
@DisplayName("A TagSet")
public class TagSetCustomizationTest {

    @Nested
    @DisplayName("when copied")
    class WhenCopied {

        @Test
        @DisplayName("inherits customizers from its source")
        void copiedTagSetInheritsCustomizersFromSource() {
            // Arrange: Create a source TagSet with a customizer and create a copy.
            TagSet sourceTagSet = TagSet.Html();
            sourceTagSet.onNewTag(tag -> tag.set(Tag.RcData)); // Customize source to set the RcData flag

            TagSet copiedTagSet = new TagSet(sourceTagSet);

            // Act: Look up a tag in both the source and the copy.
            Tag tagFromSource = sourceTagSet.valueOf("script", NamespaceHtml);
            Tag tagFromCopy = copiedTagSet.valueOf("script", NamespaceHtml);

            // Assert: The customizer from the source should apply to both.
            assertTrue(tagFromSource.is(Tag.RcData), "Source should apply its own customizer.");
            assertTrue(tagFromCopy.is(Tag.RcData), "Copy should inherit the customizer from its source.");
        }

        @Test
        @DisplayName("can be customized without affecting its source")
        void customizerOnCopiedTagSetDoesNotAffectSource() {
            // Arrange: Create a source TagSet and a copy.
            TagSet sourceTagSet = TagSet.Html();
            TagSet copiedTagSet = new TagSet(sourceTagSet);

            // Add a new customizer, but only to the copy.
            copiedTagSet.onNewTag(tag -> tag.set(Tag.Void)); // Customize copy to set the Void flag

            // Act: Look up a new, unknown tag in both sets.
            Tag tagFromCopy = copiedTagSet.valueOf("custom-tag", NamespaceHtml);
            Tag tagFromSource = sourceTagSet.valueOf("custom-tag", NamespaceHtml);

            // Assert: The customizer should only apply to the copy.
            assertTrue(tagFromCopy.is(Tag.Void), "The copied TagSet should apply its own customizer.");
            assertFalse(tagFromSource.is(Tag.Void), "A customizer added to a copy should not affect the source TagSet.");
        }
    }
}