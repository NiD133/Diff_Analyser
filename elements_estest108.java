package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test for the {@link Elements#first()} method.
 */
public class ElementsFirstTest {

    /**
     * Verifies that calling first() on an empty Elements collection returns null,
     * as there is no first element to retrieve.
     */
    @Test
    public void firstShouldReturnNullWhenElementsIsEmpty() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Attempt to get the first element from the empty collection.
        Element firstElement = emptyElements.first();

        // Assert: The result should be null, as specified by the method's contract.
        assertNull("Expected first() to return null for an empty Elements collection", firstElement);
    }
}