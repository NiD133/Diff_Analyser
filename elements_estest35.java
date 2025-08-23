package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#html(String)} method.
 */
public class ElementsTest {

    @Test
    public void htmlSetterOnEmptyElementsDoesNothing() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the html() setter on the empty collection.
        // This should not throw an exception and should return the same instance for chaining.
        Elements result = emptyElements.html("<p>Some HTML</p>");

        // Assert: The collection should remain empty, and the returned object should be the
        // same instance as the original, confirming the chaining contract.
        assertTrue("The collection should still be empty after the operation.", emptyElements.isEmpty());
        assertSame("The method should return 'this' to allow for chaining.", emptyElements, result);
    }
}