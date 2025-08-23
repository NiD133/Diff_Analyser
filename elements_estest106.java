package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class contains an improved version of the original test case.
 * Note: The original class name and inheritance are preserved to demonstrate a direct
 * refactoring of the provided code. In a real-world scenario, these would also be
 * simplified to standard conventions (e.g., class named "ElementsTest").
 */
public class Elements_ESTestTest106 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the {@link Elements#last()} method returns null
     * when called on an empty collection.
     */
    @Test
    public void lastShouldReturnNullForEmptyCollection() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Attempt to get the last element from the empty collection.
        Element lastElement = emptyElements.last();

        // Assert: The result must be null, as the collection is empty.
        assertNull("last() on an empty collection should return null", lastElement);
    }
}