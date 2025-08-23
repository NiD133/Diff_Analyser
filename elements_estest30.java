package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void nextAllOnEmptyCollectionShouldReturnNewEmptyCollection() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Call the nextAll() method to find all following sibling elements.
        Elements result = emptyElements.nextAll();

        // Assert: The method should return a new, distinct, and empty Elements collection.
        assertTrue("The resulting collection should be empty.", result.isEmpty());
        assertNotSame("A new collection instance should be returned, not the original.", emptyElements, result);
    }
}