package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the {@link Elements#unwrap()} method on an empty collection
     * returns the same empty collection instance without causing an error. This ensures
     * predictable behavior and supports method chaining even on empty result sets.
     */
    @Test
    public void unwrapOnEmptyElementsShouldReturnSameEmptyInstance() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the unwrap() method on the empty collection.
        Elements result = emptyElements.unwrap();

        // Assert: The method should return the same instance, which should still be empty.
        assertTrue("The resulting collection should be empty.", result.isEmpty());
        assertSame("The method should return the same instance to allow for chaining.", emptyElements, result);
    }
}