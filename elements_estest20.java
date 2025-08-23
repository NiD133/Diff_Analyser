package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Elements_ESTestTest20 extends Elements_ESTest_scaffolding {

    @Test
    public void removeByIndexReturnsDetachedElementWithZeroedSiblingIndex() {
        // Arrange
        // A document parsed from an empty string has one child: the <html> element.
        Document doc = Parser.parse("", "");
        Elements elements = doc.children(); // This list contains just the <html> element.

        // Act
        // The remove() method removes the element from the list and also detaches it from the DOM.
        // We capture the returned (removed) element to inspect its state.
        Element removedElement = elements.remove(0);

        // Assert
        // When an element is detached from the DOM tree, it no longer has a parent or siblings.
        // Its sibling index is consequently reset to 0. This test verifies that behavior.
        assertEquals("The sibling index of an element removed from the DOM should be 0.", 0, removedElement.siblingIndex());
    }
}