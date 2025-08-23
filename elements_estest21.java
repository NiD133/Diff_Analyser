package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Elements#remove()} method.
 */
public class ElementsRemoveTest {

    /**
     * Verifies that calling remove() on an empty Elements collection returns the same instance,
     * ensuring that method chaining is supported even in this edge case.
     */
    @Test
    public void removeOnEmptyListShouldReturnSameInstanceForChaining() {
        // Arrange: Create an empty list of elements.
        Elements emptyElements = new Elements();

        // Act: Call the remove() method.
        Elements result = emptyElements.remove();

        // Assert: The returned object should be the same instance as the original.
        assertSame(
            "The remove() method should return the same instance for fluent chaining.",
            emptyElements,
            result
        );
    }
}