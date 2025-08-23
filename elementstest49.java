package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.internal.TextUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Elements#replaceAll(java.util.function.UnaryOperator)}.
 */
public class ElementsTest {

    /**
     * Tests that replaceAll modifies the elements in the collection and also replaces them in the underlying DOM.
     */
    @Test
    public void replaceAllShouldModifyElementsInCollectionAndInDom() {
        // Arrange
        // Note: Jsoup's parser correctly handles the original's unclosed <p> tags,
        // but using well-formed HTML here is clearer.
        String initialHtml = "<body><p>One</p><p>Two</p><p>Three</p><p>Four</p></body>";
        Document doc = Jsoup.parse(initialHtml);
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size(), "Should initially select four paragraph elements.");

        // Act
        // Replace each <p> element with a <div> element containing the same text.
        paragraphs.replaceAll(p -> {
            Element div = doc.createElement("div");
            div.text(p.text());
            return div;
        });

        // Assert
        // 1. Verify the Elements collection itself now contains the new <div> elements.
        for (Element el : paragraphs) {
            assertEquals("div", el.tagName(), "Each element in the collection should now be a 'div'.");
        }

        // 2. Verify the underlying DOM was updated to reflect the replacement.
        String expectedHtml = "<div>One</div> <div>Two</div> <div>Three</div> <div>Four</div>";
        String actualHtml = TextUtil.normalizeSpaces(doc.body().html());
        assertEquals(expectedHtml, actualHtml, "The DOM structure should be updated with div elements.");
    }
}