package org.jsoup.select;

import org.junit.Test;

/**
 * This test focuses on the behavior of the {@link Elements#deselect(int)} method.
 * Note: The original class name and inheritance are preserved as they might be part of a larger, generated test suite.
 */
public class Elements_ESTestTest154 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling deselect() on an empty Elements collection
     * throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void deselectOnEmptyListThrowsException() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act & Assert: Attempt to deselect an element at index 0.
        // Since the list is empty, this action is expected to throw an
        // IndexOutOfBoundsException. The @Test annotation handles the assertion.
        emptyElements.deselect(0);
    }
}