package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 * This specific test focuses on the behavior of the not() method.
 */
public class ElementsTest { // Renamed from Elements_ESTestTest27 for clarity

    @Test
    public void notShouldReturnEmptyElementsWhenCalledOnEmptyElements() {
        // Arrange: Create an empty list of elements. The specific selector doesn't
        // matter here, but we use a realistic one for better readability.
        Elements emptyElements = new Elements();
        String selector = ".any-selector";

        // Act: Call the not() method on the empty list.
        Elements result = emptyElements.not(selector);

        // Assert: The resulting list should also be empty.
        assertNotNull("The not() method should never return null.", result);
        assertTrue(
            "Calling not() on an empty Elements collection should return an empty collection.",
            result.isEmpty()
        );
    }
}