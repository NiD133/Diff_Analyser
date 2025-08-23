package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Tag.SelfClose;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest8 {

    @Test
    void onNewTagCustomizerCanSetUnknownTagsAsSelfClosing() {
        // Arrange: Configure a parser to treat any new, unknown tag as self-closing.
        // This is useful for parsing documents with custom elements like <my-tag/>,
        // ensuring they don't incorrectly consume subsequent content.
        Parser parser = Parser.htmlParser();
        parser.tagSet().onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.set(SelfClose);
            }
        });

        String htmlInput = "<custom-data />Bar <script />Text";

        // Act
        Document doc = Jsoup.parse(htmlInput, parser);
        Element body = doc.body();

        // Assert: Verify the custom tag and the known tag were handled as expected.

        // 1. The unknown <custom-data> tag should be treated as self-closing and be empty.
        Element customTag = body.selectFirst("custom-data");
        assertNotNull(customTag, "The custom tag should be present in the document.");
        assertTrue(customTag.children().isEmpty(), "The custom tag should have no children.");
        assertEquals("", customTag.text(), "The custom tag should contain no text.");

        // 2. The known <script> tag should NOT be affected by the customizer. It should parse
        // normally, containing the subsequent text.
        Element scriptTag = body.selectFirst("script");
        assertNotNull(scriptTag, "The script tag should be present in the document.");
        assertEquals("Text", scriptTag.text(), "The script tag should contain the text that follows it.");

        // 3. Verify the complete HTML output of the body to ensure the overall structure is correct.
        String expectedHtml = "<custom-data></custom-data>Bar\n<script>Text</script>";
        assertEquals(expectedHtml, body.html());
    }
}