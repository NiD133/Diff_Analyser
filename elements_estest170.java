package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class, focusing on edge cases for traversal methods.
 */
public class ElementsTest {

    @Test
    public void prevAllOnEmptyListWithNullSelectorReturnsNewEmptyList() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call prevAll() with a null selector string. This tests the edge case
        // where the method receives null input on an empty collection.
        Elements result = emptyElements.prevAll((String) null);

        // Assert: The method should return a new, empty Elements instance,
        // rather than modifying or returning the original instance.
        assertNotSame("prevAll should return a new instance, not the original.", emptyElements, result);
        assertTrue("The resulting list of elements should be empty.", result.isEmpty());
    }
}