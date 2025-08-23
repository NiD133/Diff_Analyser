package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 * This class contains the improved version of the original test.
 */
public class Elements_ESTestTest13 {

    /**
     * Tests that the set() method correctly replaces an element at a specific index.
     * It should update both the Elements list and the underlying DOM, and return the
     * element that was replaced.
     */
    @Test
    public void setShouldReplaceElementInListAndDomAndReturnReplacedElement() {
        // Arrange
        // Parsing an empty body fragment creates a document with <html>, <head>, and <body> tags.
        Document doc = Parser.parseBodyFragment("", "");
        Elements elements = doc.getAllElements(); // The list will contain [<html>, <head>, <body>]

        // We will replace the <head> element, which is at index 1.
        Element originalHeadElement = elements.get(1);
        assertEquals("head", originalHeadElement.tagName()); // Sanity check

        Element newElement = new Element("div");

        // Act
        // Replace the <head> element with the new <div> element.
        Element replacedElement = elements.set(1, newElement);

        // Assert
        // 1. Verify that the method returns the element that was replaced.
        assertSame("The returned element should be the one that was originally at the index.",
            originalHeadElement, replacedElement);

        // 2. Verify that the Elements list is updated with the new element.
        assertSame("The new element should now be at the specified index in the list.",
            newElement, elements.get(1));
        assertEquals("The size of the list should remain the same.", 3, elements.size());

        // 3. Verify that the underlying DOM has been modified correctly.
        assertNull("The replaced element should be detached from the DOM (no parent).", replacedElement.parent());
        assertNotNull("The new element should be attached to the DOM (has a parent).", newElement.parent());
        assertEquals("The new element's parent should be the <html> tag.", "html", newElement.parent().tagName());

        // Confirm the DOM structure by querying it.
        assertNull("Document.head() should be null as the <head> element was replaced.", doc.head());
        assertNotNull("The new <div> element should be selectable from the document.", doc.selectFirst("html > div"));
    }
}