package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that {@link Elements#selectFirst(String)} returns null
     * when the CSS selector does not match any element in the collection.
     */
    @Test
    public void selectFirstShouldReturnNullForNonMatchingSelector() {
        // Arrange: Create a collection of elements from a sample HTML structure.
        String html = "<div><p>An element</p></div><span>Another element</span>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getAllElements(); // Contains html, head, body, div, p, span

        // A CSS selector that is guaranteed not to match anything in the HTML above.
        String nonMatchingSelector = "a.non-existent-class";

        // Act: Attempt to find an element using the non-matching selector.
        Element foundElement = elements.selectFirst(nonMatchingSelector);

        // Assert: Verify that no element was found.
        assertNull("selectFirst should return null when no element matches the selector.", foundElement);
    }
}