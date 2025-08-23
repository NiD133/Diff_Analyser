package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the empty() method on an already empty Elements collection
     * results in the collection remaining empty.
     */
    @Test
    public void emptyOnEmptyCollectionShouldRemainEmpty() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the empty() method on the collection.
        Elements result = emptyElements.empty();

        // Assert: The collection should still be empty and the method should return the same instance.
        assertTrue("The collection should remain empty after calling empty()", result.isEmpty());
        assertSame("The empty() method should return the same instance for chaining", emptyElements, result);
    }
}