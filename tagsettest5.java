package org.jsoup.parser;

import org.junit.jupiter.api.Test;
import java.util.function.Consumer;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link TagSet} customization capabilities.
 */
public class TagSetTest {

    /**
     * Verifies that a customizer set via {@link TagSet#onNewTag(Consumer)} is applied to all tags,
     * regardless of how they are introduced to the TagSet.
     * This test covers four scenarios:
     * 1. Pre-existing tags when a TagSet is copied (e.g., from {@link TagSet#Html()}).
     * 2. Known tags retrieved via {@code valueOf()}.
     * 3. New, unknown tags created via {@code valueOf()}.
     * 4. Externally created tags added via {@code add()}.
     */
    @Test
    void onNewTagAppliesCustomizationToAllTags() {
        // Arrange: Create a TagSet and register a customizer to make all tags self-closing.
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> tag.set(Tag.SelfClose));

        // Act & Assert

        // 1. Verify a pre-existing, known tag (e.g., "script") is customized.
        // The customizer should be applied when TagSet.Html() copies the default tags.
        Tag scriptTag = tagSet.get("script", NamespaceHtml);
        assertNotNull(scriptTag);
        assertTrue(scriptTag.is(Tag.SelfClose),
            "Known tags should be customized upon TagSet copy.");

        // 2. Verify a known tag fetched via valueOf() is customized.
        Tag scriptTagFromValueOf = tagSet.valueOf("SCRIPT", NamespaceHtml);
        assertTrue(scriptTagFromValueOf.is(Tag.SelfClose),
            "Known tags from valueOf() should be customized.");

        // 3. Verify a new, unknown tag created via valueOf() is customized.
        Tag customTag = tagSet.valueOf("custom", NamespaceHtml);
        assertTrue(customTag.is(Tag.SelfClose),
            "Newly created tags from valueOf() should be customized.");

        // 4. Verify an externally created tag is customized upon being added.
        Tag externalTag = new Tag("foo", NamespaceHtml);
        assertFalse(externalTag.is(Tag.SelfClose),
            "An externally created tag should not be customized before being added to the set.");

        tagSet.add(externalTag);
        assertTrue(externalTag.is(Tag.SelfClose),
            "Externally created tags should be customized upon being added to the set.");
    }
}