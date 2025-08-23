package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the {@link Elements#nextAll()} method.
 */
public class ElementsNextAllTest {

    /**
     * Verifies that calling the no-argument nextAll() method on an empty Elements
     * collection returns a new, distinct Elements instance.
     *
     * This ensures the method adheres to the contract of returning a new collection
     * for subsequent filtering and does not simply return the original instance,
     * which could lead to unexpected behavior.
     */
    @Test
    public void nextAllOnEmptyElementsReturnsNewInstance() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the nextAll() method to get all following siblings.
        Elements nextSiblings = emptyElements.nextAll();

        // Assert: The returned collection must be a new instance, not a reference to the original.
        assertNotSame("Expected a new Elements instance to be returned.", emptyElements, nextSiblings);
    }
}