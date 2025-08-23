package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Elements_ESTestTest121 {

    /**
     * Tests that calling .after() on an Elements collection throws an exception if it contains an element
     * that does not have a parent. This is because .after() inserts a sibling, which requires a parent node.
     * The root <html> element is a common example of an element without a parent.
     */
    @Test
    public void afterThrowsExceptionWhenElementLacksParent() {
        // Arrange: Create a document. The `getAllElements()` method returns a list
        // that includes the root <html> element, which has no parent.
        Document doc = Parser.parse("<html><body><p>Hello</p></body></html>");
        Elements elements = doc.getAllElements();

        // Act & Assert
        try {
            // This call will iterate through the elements and fail on the first one (<html>),
            // as it cannot insert a sibling to a node without a parent.
            elements.after("<br>");
            fail("Should have thrown an IllegalArgumentException because the root element has no parent.");
        } catch (IllegalArgumentException e) {
            // The exception is thrown by an internal check: Validate.notNull(parent, "Object must not be null")
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}