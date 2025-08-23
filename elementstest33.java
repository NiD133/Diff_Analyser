package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * An improved test suite for the Elements class, focusing on the functionality
 * of the {@link Elements#next()} method. This version prioritizes clarity and
 * maintainability.
 */
public class ImprovedElementsNextTest {

    /**
     * Verifies that the {@code next()} method, when invoked on a collection of elements,
     * correctly returns a new collection containing only the unique, immediate
     * next sibling of each element from the original set.
     *
     * <p><b>Test Logic:</b></p>
     * <ol>
     *     <li><b>Arrange:</b> A standard HTML document is created, which by default contains
     *     {@code <html>}, {@code <head>}, and {@code <body>} tags. A collection is
     *     initialized with these three elements.</li>
     *     <li><b>Act:</b> The {@code next()} method is called on this collection.</li>
     *     <li><b>Assert:</b> The test asserts that the resulting collection contains exactly one
     *     element. This is the expected outcome because, within the initial set, only the
     *     {@code <head>} element has a direct next sibling (the {@code <body>} element).</li>
     * </ol>
     */
    @Test
    public void testNextOnCollectionReturnsUniqueImmediateSiblings() {
        // Arrange: Set up a clear HTML structure and select a set of elements.
        // Jsoup.parse("") creates a default document: <html><head></head><body></body></html>
        Document doc = Jsoup.parse("");
        Element htmlElement = doc.child(0);
        Element headElement = doc.head();
        Element bodyElement = doc.body();

        // Create an Elements collection where only one element (<head>) has a next sibling.
        Elements initialElements = new Elements(htmlElement, headElement, bodyElement);

        // Act: Call the .next() method to retrieve the next siblings.
        Elements nextSiblingElements = initialElements.next();

        // Assert: Verify that the resulting collection contains only the single, correct next sibling.
        // We expect only the <body> element, which is the next sibling of <head>.
        assertNotNull("The result of next() should not be null.", nextSiblingElements);
        assertEquals("Should find exactly one next sibling element.", 1, nextSiblingElements.size());

        Element foundSibling = nextSiblingElements.first();
        assertNotNull("The first element in the result should not be null.", foundSibling);
        assertEquals("The found sibling should be the <body> tag.", "body", foundSibling.tagName());
    }
}