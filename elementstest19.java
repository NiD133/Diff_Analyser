package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link Elements#remove(int)} method.
 */
public class Elements_ESTestTest19 {

    /**
     * Verifies that the {@link Elements#remove(int)} method removes the element at the specified index
     * from both the Elements list and the underlying DOM structure.
     */
    @Test
    public void removeByIndexShouldReturnElementAndDetachItFromDom() {
        // Arrange: Set up a document with a clear parent-child structure.
        Document doc = Jsoup.parse("<div><p>Test Paragraph</p><span>Test Span</span></div>");
        Elements children = doc.select("div > *"); // Selects the <p> and <span> elements

        int initialSize = children.size();
        assertEquals("Initially, there should be two child elements.", 2, initialSize);

        Element paragraphElement = children.get(0);
        assertEquals("The first element should be the paragraph.", "p", paragraphElement.tagName());
        assertNotNull("The paragraph should have a parent before being removed.", paragraphElement.parent());

        // Act: Remove the first element (the paragraph) from the Elements collection.
        Element removedElement = children.remove(0);

        // Assert: Check the state after the removal.
        // 1. The correct element was returned by the remove method.
        assertEquals("The removed element should be the paragraph element.", paragraphElement, removedElement);

        // 2. The element was removed from the Elements list.
        assertEquals("The size of the Elements list should be reduced by one.", initialSize - 1, children.size());

        // 3. The element was detached from the DOM (its parent is now null).
        assertNull("The removed element should be detached from the DOM (parent is null).", removedElement.parent());

        // 4. The original parent element in the DOM no longer contains the removed element.
        Element parentDiv = doc.selectFirst("div");
        assertNotNull(parentDiv);
        assertEquals("The parent div should now have only one child.", 1, parentDiv.childrenSize());
        assertEquals("The remaining child should be the span element.", "span", parentDiv.child(0).tagName());
    }
}