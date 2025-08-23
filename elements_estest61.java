package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 * This class contains tests for methods that modify the list of elements.
 */
public class ElementsModificationTest {

    /**
     * Verifies that the {@link Elements#set(int, Element)} method throws an
     * {@link IllegalArgumentException} when a null element is provided. The method
     * must not allow null elements in the collection.
     */
    @Test
    public void setShouldThrowExceptionWhenElementIsNull() {
        // Arrange: Create an Elements collection with one item to ensure index 0 is valid.
        Elements elements = new Elements(new Element("p"));

        try {
            // Act: Attempt to replace an element with null.
            elements.set(0, null);
            fail("Expected an IllegalArgumentException to be thrown for a null element.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}