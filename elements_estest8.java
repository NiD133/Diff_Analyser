package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class, focusing on the toggleClass method.
 */
public class ElementsToggleClassTest {

    @Test
    public void toggleClassOnEmptyCollectionShouldBeANoOp() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Attempt to toggle a class on the empty collection.
        Elements result = emptyElements.toggleClass("test-class");

        // Assert: The collection should remain empty and the original instance should be returned.
        assertTrue("The Elements collection should remain empty after the operation.", result.isEmpty());
        assertSame("The method should return the same instance to allow for chaining.", emptyElements, result);
    }
}