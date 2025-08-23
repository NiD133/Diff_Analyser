package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#wrap(String)} method.
 */
public class ElementsWrapTest {

    /**
     * Verifies that calling wrap() on an empty Elements collection
     * returns the same empty collection without causing an error.
     */
    @Test
    public void wrapOnEmptyElementsShouldReturnEmptyElements() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();
        String wrapperHtml = "<div></div>";

        // Act: Attempt to wrap the empty collection.
        Elements result = emptyElements.wrap(wrapperHtml);

        // Assert: The collection should remain empty and be the same instance.
        assertTrue("The resulting collection should still be empty.", result.isEmpty());
        assertSame("The wrap() method should return the same instance for chaining.", emptyElements, result);
    }
}