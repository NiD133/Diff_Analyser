package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link Elements#unwrap()} method.
 */
public class ElementsUnwrapTest {

    /**
     * Verifies that the unwrap() method removes an element from the DOM
     * but does not remove it from the Elements collection it was called on.
     */
    @Test
    public void unwrapShouldModifyDomButNotTheCollection() {
        // Arrange: Create a document and select its body element.
        // The getAllElements() call on the body will return a collection
        // containing just the body element itself.
        Document document = new Document("");
        Element body = document.body();
        Elements elements = body.getAllElements();

        // Sanity check to ensure the initial state is as expected.
        assertEquals("The collection should initially contain one element.", 1, elements.size());

        // Act: Call the unwrap() method. This action should remove the body
        // element from the document tree but not from the 'elements' list.
        Elements returnedElements = elements.unwrap();

        // Assert: Confirm the collection is not empty and its contents are unchanged.
        assertFalse("The Elements collection should not be empty after unwrap().", returnedElements.isEmpty());
        assertEquals("The collection size should remain 1 after unwrap().", 1, returnedElements.size());
        assertSame("The unwrap() method should return the same instance.", elements, returnedElements);
    }
}