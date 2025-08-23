package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Tests that calling the {@link Elements#next()} method on an empty Elements
     * collection returns a new, empty collection rather than null or the same instance.
     */
    @Test
    public void nextOnEmptyElementsShouldReturnNewEmptyList() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the next() method to get the next sibling elements.
        Elements result = emptyElements.next();

        // Assert: The result should be a new, non-null, empty Elements collection.
        assertNotNull("The result of next() should not be null.", result);
        assertTrue("The resulting collection should be empty.", result.isEmpty());
        assertNotSame("A new Elements instance should be returned.", emptyElements, result);
    }
}