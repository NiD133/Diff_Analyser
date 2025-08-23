package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void removeAttrOnEmptyElementsShouldBeSafeAndReturnEmpty() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();
        String attributeToRemove = "class";

        // Act: Attempt to remove an attribute from the empty collection.
        Elements resultElements = emptyElements.removeAttr(attributeToRemove);

        // Assert: The operation should not throw an exception and should return an empty collection.
        // The same instance should be returned to allow for method chaining.
        assertTrue("The resulting collection should be empty.", resultElements.isEmpty());
        assertSame("The method should return the same instance for chaining.", emptyElements, resultElements);
    }
}