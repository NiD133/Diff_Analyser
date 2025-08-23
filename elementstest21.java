package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link Elements#remove()} method.
 */
public class ElementsRemoveTest {

    /**
     * Verifies that calling the remove() method on an empty Elements collection
     * returns the same instance. This is important for ensuring that method
     * chaining works correctly even when the collection is empty.
     */
    @Test
    public void callingRemoveOnEmptyCollectionShouldReturnSameInstanceForChaining() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the remove() method, which should remove all elements from the DOM.
        Elements result = emptyElements.remove();

        // Assert: The method should return the same Elements instance to allow for
        // method chaining, even if no operation was performed.
        assertSame("Expected remove() on an empty collection to return the same instance", emptyElements, result);
    }
}