package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementsTestTest32 {

    @Test
    @DisplayName("textNodes() should return only the direct child text nodes of selected elements")
    void shouldReturnDirectChildTextNodes() {
        // Arrange: Setup HTML with text nodes at various levels to test selection logic.
        // The goal is to verify that only direct child text nodes of the selected <p> elements are returned.
        // - "One" and "Five" are siblings to the <p> tags, not children.
        // - "Two" is a direct child of the first <p> tag.
        // - "Three" is nested within an <a> tag, so it's not a *direct* child of <p>.
        // - "Four" is a direct child of the second <p> tag.
        String html = "One<p>Two<a>Three</a></p><p>Four</p>Five";
        Document doc = Jsoup.parse(html);

        // Act: Select all <p> elements and get their direct text nodes.
        List<TextNode> actualTextNodes = doc.select("p").textNodes();

        // Assert: The result should contain the text from the direct child TextNodes only.
        List<String> expectedTexts = asList("Two", "Four");
        List<String> actualTexts = actualTextNodes.stream()
                                                  .map(TextNode::text)
                                                  .collect(Collectors.toList());

        assertEquals(expectedTexts, actualTexts, "Should only find direct child text nodes 'Two' and 'Four'");
    }
}