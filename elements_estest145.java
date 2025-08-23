package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test for the {@link Elements#hasAttr(String)} method.
 */
public class ElementsHasAttrTest {

    /**
     * Verifies that hasAttr() returns false when none of the elements in the collection
     * possess the specified attribute.
     */
    @Test
    public void hasAttrShouldReturnFalseWhenNoElementHasAttribute() {
        // Arrange: Create a document with several elements. Note that one element has a 'class'
        // attribute, but none have an 'id' attribute.
        String html = "<div><p class='greeting'>Hello</p><span>World</span></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.getAllElements(); // Contains html, head, body, div, p, span

        // Act: Check for an attribute that does not exist on any element.
        boolean hasIdAttribute = elements.hasAttr("id");

        // Assert: The result should be false, as no element has an 'id'.
        assertFalse("Expected hasAttr('id') to be false because no element has an 'id' attribute.", hasIdAttribute);
    }
}