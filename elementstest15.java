package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements} class, focusing on DOM manipulation methods.
 */
// The class was renamed from ElementsTestTest15 to follow standard conventions.
public class ElementsTest {

    @Test
    void before_shouldInsertHtmlBeforeEachSelectedElement() {
        // Arrange: Set up the initial HTML document and select the target elements.
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        Elements links = doc.select("a");
        String htmlToInsert = "<span>foo</span>";

        // Act: Call the method under test to insert the HTML before each selected element.
        links.before(htmlToInsert);

        // Assert: Verify that the document's HTML has been modified as expected.
        String expectedHtml = "<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>";
        String actualHtml = TextUtil.stripNewlines(doc.body().html());
        assertEquals(expectedHtml, actualHtml, "The HTML should be inserted before each of the two <a> elements.");
    }
}