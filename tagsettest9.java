package org.jsoup.parser;

import org.junit.jupiter.api.Test;
import java.util.function.Consumer;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest9 {

    @Test
    void multipleCustomizersAreChainedAndAppliedCorrectly() {
        // Arrange: Create a standard HTML TagSet and define two distinct customizers.
        // Customizer 1: Makes the 'script' tag self-closing.
        Consumer<Tag> makeScriptSelfClosing = tag -> {
            if (tag.normalName().equals("script")) {
                tag.set(Tag.SelfClose);
            }
        };

        // Customizer 2: Makes any tag that is not a known HTML tag an RCDATA tag.
        Consumer<Tag> makeUnknownTagsRcData = tag -> {
            if (!tag.isKnownTag()) {
                tag.set(Tag.RcData);
            }
        };

        TagSet tags = TagSet.Html();
        tags.onNewTag(makeScriptSelfClosing);
        tags.onNewTag(makeUnknownTagsRcData);

        // Act: Retrieve a known tag ('script') and an unknown tag ('custom-tag').
        // The customizers are invoked when the tags are created via valueOf().
        Tag scriptTag = tags.valueOf("script", NamespaceHtml);
        Tag customTag = tags.valueOf("custom-tag", NamespaceHtml);

        // Assert: Verify that the correct customizers were applied to each tag.

        // 1. Assertions for the known 'script' tag
        assertTrue(scriptTag.is(Tag.SelfClose),
            "The first customizer should have made the 'script' tag self-closing.");
        assertFalse(scriptTag.is(Tag.RcData),
            "The 'script' tag is a known tag, so the second customizer should not apply.");

        // 2. Assertions for the unknown 'custom-tag'
        assertTrue(customTag.is(Tag.RcData),
            "The second customizer should have made the unknown 'custom-tag' an RCDATA tag.");
        assertFalse(customTag.is(Tag.SelfClose),
            "The 'custom-tag' is not a 'script' tag, so the first customizer should not apply.");
    }
}