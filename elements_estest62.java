package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for {@link Elements}.
 * This class focuses on the behavior of the set() method.
 */
public class ElementsSetTest {

    /**
     * Verifies that calling set() with an index that is out of the list's bounds
     * throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setShouldThrowExceptionForIndexOutOfBounds() {
        // Arrange: Create a list of elements.
        // A default shell document contains 4 elements: #root, html, head, and body.
        Document doc = Document.createShell("http://example.com");
        Elements elements = doc.getAllElements();
        
        int outOfBoundsIndex = elements.size() + 10; // Any index >= size() is invalid.
        Element newElement = new Element("p");

        // Act: Attempt to set an element at an index that does not exist.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        elements.set(outOfBoundsIndex, newElement);
    }
}