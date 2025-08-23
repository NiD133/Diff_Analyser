package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that the last() method correctly retrieves the last element from a collection.
     * For a basic shell document created with Document.createShell(), the element list is
     * [html, head, title, body], so the last element should be the 'body' tag.
     */
    @Test
    public void lastShouldReturnTheFinalElementInTheList() {
        // Arrange: Create a basic HTML document and get all its elements.
        Document doc = Document.createShell("https://example.com");
        Elements allElements = doc.getAllElements();

        // Act: Get the last element from the collection.
        Element lastElement = allElements.last();

        // Assert: Verify that the retrieved element is indeed the <body> tag.
        assertNotNull("The last element should not be null.", lastElement);
        assertEquals("The tag name of the last element should be 'body'.", "body", lastElement.tagName());
        assertTrue("The <body> element is expected to be a block-level element.", lastElement.isBlock());
    }
}