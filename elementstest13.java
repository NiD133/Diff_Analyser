package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for HTML modification methods in the {@link Elements} class.
 * This suite verifies that methods like {@code prepend}, {@code append}, and {@code html}
 * correctly modify all elements within the collection.
 */
public class ElementsTestTest13 {

    @Test
    void prependAndAppendShouldModifyAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");
        String prependHtml = "<b>Start</b>";
        String appendHtml = "<i>End</i>";

        // Act
        paragraphs.prepend(prependHtml).append(appendHtml);

        // Assert
        // Verify that all elements in the collection were modified as expected.
        assertEquals(3, paragraphs.size());
        assertEquals("<p><b>Start</b>One<i>End</i></p>", TextUtil.stripNewlines(paragraphs.get(0).outerHtml()), "First element should be modified.");
        assertEquals("<p><b>Start</b>Two<i>End</i></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()), "Second element should be modified.");
        assertEquals("<p><b>Start</b>Three<i>End</i></p>", TextUtil.stripNewlines(paragraphs.get(2).outerHtml()), "Third element should be modified.");
    }

    @Test
    void htmlShouldReplaceInnerContentOfAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");
        String newInnerHtml = "<span>New Content</span>";

        // Act
        paragraphs.html(newInnerHtml);

        // Assert
        // Verify that the inner HTML of all elements was replaced.
        assertEquals(3, paragraphs.size());
        String expectedOuterHtml = "<p><span>New Content</span></p>";
        assertEquals(expectedOuterHtml, TextUtil.stripNewlines(paragraphs.get(0).outerHtml()), "First element's content should be replaced.");
        assertEquals(expectedOuterHtml, TextUtil.stripNewlines(paragraphs.get(1).outerHtml()), "Second element's content should be replaced.");
        assertEquals(expectedOuterHtml, TextUtil.stripNewlines(paragraphs.get(2).outerHtml()), "Third element's content should be replaced.");
    }
}