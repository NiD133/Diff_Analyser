package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This test verifies the behavior of the {@link Elements#empty()} method.
 */
public class Elements_ESTestTest117 {

    /**
     * Tests that the empty() method removes all child nodes from each element in the collection
     * but does not remove the elements themselves from the collection.
     */
    @Test
    public void empty_shouldRemoveChildNodesFromElementsWithoutAlteringTheCollection() {
        // Arrange: Create a document with a structure that includes elements with children.
        String html = "<div><p>Some text</p></div> <span></span>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements elements = doc.select("div, span"); // Select a <div> with a child and an empty <span>
        int originalSize = elements.size();

        // Pre-condition check to ensure the initial state is as expected.
        assertEquals("Test setup should select two elements.", 2, originalSize);
        assertEquals("The <div> element should initially have one child node.", 1, elements.get(0).childNodeSize());

        // Act: Call the empty() method on the collection of elements.
        Elements returnedElements = elements.empty();

        // Assert: The method should modify the elements in-place but not the collection itself.

        // 1. The method should return the same Elements instance to allow for chaining.
        assertSame("The empty() method should return the same instance.", elements, returnedElements);

        // 2. The size of the Elements collection should remain unchanged.
        assertEquals("The number of elements in the collection should not change.", originalSize, elements.size());

        // 3. The child nodes of each element in the collection should have been removed.
        for (Element element : elements) {
            assertTrue("Element '<" + element.tagName() + ">' should be empty after the call.", element.childNodeSize() == 0);
        }

        // 4. Verify the underlying document's HTML has been modified as expected.
        assertEquals("The children of the selected elements should be removed from the DOM.",
            "<div></div> <span></span>", doc.body().html().trim());
    }
}