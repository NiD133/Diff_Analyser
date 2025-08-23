package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for TagSet customization.
 */
public class TagSetTest {

    @Test
    void parserHonorsCustomTagProperties() {
        // Arrange
        // 1. Create a custom TagSet, starting from the HTML defaults.
        TagSet customTagSet = TagSet.Html();

        // 2. Define a new tag named "custom".
        //    Configure it to be a block-level tag that preserves whitespace.
        customTagSet.valueOf("custom", NamespaceHtml)
            .set(Tag.PreserveWhitespace)
            .set(Tag.Block);

        // 3. Create a parser that uses our custom tag definitions.
        Parser parser = Parser.htmlParser().tagSet(customTagSet);
        String htmlWithCustomTag = "<body><custom>\n\nFoo\n Bar</custom></body>";

        // Act
        Document doc = Jsoup.parse(htmlWithCustomTag, parser);
        Element customElement = doc.expectFirst("custom");
        Tag customTag = customElement.tag();

        // Assert
        // 1. Verify the tag's properties were correctly configured.
        assertTrue(customTag.preserveWhitespace(), "Custom tag should be configured to preserve whitespace.");
        assertTrue(customTag.isBlock(), "Custom tag should be configured as a block tag.");

        // 2. Verify the parser respected the 'preserveWhitespace' property by checking the output HTML.
        String expectedHtml = "<custom>\n" +
                              "\n" +
                              "Foo\n" +
                              " Bar" +
                              "</custom>";
        assertEquals(expectedHtml, customElement.outerHtml(), "Whitespace within the custom tag should be preserved during parsing.");
    }
}