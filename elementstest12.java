package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Elements} class, focusing on method chaining behavior.
 */
public class ElementsTest {

    /**
     * Verifies that the tagName() method returns the same Elements instance,
     * which is a common pattern for enabling method chaining.
     */
    @Test
    public void tagName_shouldReturnSameInstance_forMethodChaining() {
        // Arrange: Create a document and select a group of elements.
        Document doc = Document.createShell(""); // An empty document with html, head, and body tags.
        Elements allElements = doc.getAllElements();
        final String newTagName = "div";

        // Act: Rename the tag of all selected elements.
        Elements returnedElements = allElements.tagName(newTagName);

        // Assert: The method should return the same Elements instance to allow for chaining.
        assertSame("The tagName() method must return the same instance for chaining.", allElements, returnedElements);

        // Assert: Also, verify that the elements were actually renamed.
        for (Element el : allElements) {
            assertEquals(newTagName, el.tagName());
        }
    }
}