package org.jsoup.parser;

import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link TagSet} customization.
 */
public class TagSetTest {

    /**
     * Verifies that a customizer, added via {@link TagSet#onNewTag(java.util.function.Consumer)},
     * can modify newly created tags. This test specifically checks that only unknown tags are
     * affected, while known HTML tags remain unchanged.
     */
    @Test
    void onNewTagCustomizerModifiesOnlyUnknownTags() {
        // Arrange: Create a TagSet and add a customizer to make any unknown tag self-closing.
        TagSet tagSet = TagSet.Html();
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Act & Assert:
        // The customizer should only apply to tags that are not part of the standard HTML set.

        // 1. A known, standard HTML tag ('script') should NOT be modified.
        Tag knownTag = tagSet.valueOf("script", NamespaceHtml);
        assertFalse(knownTag.is(Tag.SelfClose),
            "A known tag like 'script' should not be affected by the customizer.");

        // 2. A known tag with different casing should also NOT be modified, as HTML tags are case-insensitive.
        Tag knownTagUpperCase = tagSet.valueOf("SCRIPT", NamespaceHtml);
        assertFalse(knownTagUpperCase.is(Tag.SelfClose),
            "Known tags should be recognized case-insensitively and not be modified.");

        // 3. An unknown, custom tag ('custom-tag') SHOULD be modified by the customizer.
        Tag unknownTag = tagSet.valueOf("custom-tag", NamespaceHtml);
        assertTrue(unknownTag.is(Tag.SelfClose),
            "An unknown tag like 'custom-tag' should be made self-closing by the customizer.");
    }
}