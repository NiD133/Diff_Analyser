package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link Elements} class, focusing on attribute manipulation.
 */
public class Elements_ESTestTest49 {

    /**
     * Verifies that calling the attr(key, value) method on an empty Elements collection
     * does not cause an error and returns the same empty collection.
     */
    @Test
    public void attrOnEmptyElementsShouldReturnEmptyElements() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Attempt to set an attribute on the elements in the empty collection.
        Elements result = emptyElements.attr("class", "test");

        // Assert: The method should return the original empty collection instance.
        assertTrue("The Elements collection should remain empty.", result.isEmpty());
        assertSame("The method should return the same instance for chaining.", emptyElements, result);
    }
}