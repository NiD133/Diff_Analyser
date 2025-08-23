package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements#unwrap()} method.
 */
@DisplayName("Elements#unwrap")
class ElementsUnwrapTest {

    @Test
    @DisplayName("should remove selected elements but keep their children")
    void unwrapShouldRemoveSelectedElementsButKeepChildren() {
        // Arrange: Set up the initial HTML structure.
        // The HTML contains two <p> tags that we want to remove.
        String html = "<p><a>One</a> Two</p> Three <i>Four</i> <p>Fix <i>Six</i></p>";
        Document doc = Jsoup.parseBodyFragment(html);

        // Act: Select all <p> elements and unwrap them.
        // This should remove the <p> tags, promoting their contents to be siblings
        // of the other nodes.
        doc.select("p").unwrap();

        // Assert: Verify that the <p> tags are gone but their content remains.
        String expectedHtml = "<a>One</a> Two Three <i>Four</i> Fix <i>Six</i>";
        String actualHtml = TextUtil.stripNewlines(doc.body().html());

        assertEquals(expectedHtml, actualHtml);
    }
}