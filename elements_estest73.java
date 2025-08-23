package org.jsoup.select;

import org.junit.Test;

/**
 * Test suite for the {@link Elements} class.
 * Note: The original test class name `Elements_ESTestTest73` suggests it was
 * auto-generated. This version uses a more conventional name and structure.
 */
public class Elements_ESTestTest73 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling remove() with an index on an empty Elements collection
     * throws an IndexOutOfBoundsException, as expected for a list-like object.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeByIndexFromEmptyListThrowsIndexOutOfBoundsException() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act & Assert: Attempt to remove an element at index 0.
        // Since the list is empty, this action is expected to throw an
        // IndexOutOfBoundsException, which is verified by the @Test annotation.
        emptyElements.remove(0);
    }
}