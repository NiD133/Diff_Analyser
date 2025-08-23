package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Elements} class, focusing on DOM manipulation methods.
 */
public class ElementsTest {

    /**
     * Verifies that calling the `after()` method on an empty Elements collection
     * returns the same instance. This ensures that method chaining is supported
     * even when the collection contains no elements.
     */
    @Test
    public void afterOnEmptyCollectionShouldReturnSameInstanceForChaining() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the after() method. On an empty collection, this should be a no-op
        // that returns the original object to allow for method chaining.
        Elements result = emptyElements.after("<span></span>");

        // Assert: The method should return the same instance.
        assertSame("Expected the same instance to be returned for chaining", emptyElements, result);
    }
}