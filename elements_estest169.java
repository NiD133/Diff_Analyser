package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the {@link Elements#clone()} method.
 */
public class ElementsCloneTest {

    @Test
    public void cloneCreatesIndependentCopyOfElements() {
        // Arrange: Create a document and select a list of elements.
        String html = "<div><p>One</p><p>Two</p></div>";
        Document doc = Parser.parse(html, ""); // baseUri is not needed for this test.
        Elements originalElements = doc.select("p");

        // Act: Clone the Elements object.
        Elements clonedElements = originalElements.clone();

        // Assert: Verify the properties of the cloned object.
        
        // 1. The clone must be a different object instance from the original.
        assertNotSame("The cloned object should be a new instance.", originalElements, clonedElements);

        // 2. The clone must be equal in content to the original.
        // This checks that size, order, and contained elements are the same.
        assertEquals("The cloned list should have the same elements as the original.", originalElements, clonedElements);

        // 3. (Optional but good) Modifying the cloned list's structure should not affect the original.
        // This confirms it's a shallow copy of the list, not a reference to the same list.
        clonedElements.remove(0);
        assertEquals("Original list size should be unchanged after modifying the clone.", 2, originalElements.size());
        assertEquals("Cloned list size should be smaller after modification.", 1, clonedElements.size());
    }
}