package org.jsoup.select;

import org.junit.Test;

/**
 * Test suite for the {@link Elements} class, focusing on exception handling.
 */
public class ElementsTest {

    /**
     * Verifies that calling {@link Elements#deselect(int)} with a negative index
     * throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void deselectThrowsExceptionForNegativeIndex() {
        // Arrange: Create an empty list of elements.
        Elements elements = new Elements();
        int invalidIndex = -1;

        // Act: Attempt to deselect an element at a negative index.
        // Assert: The test expects an ArrayIndexOutOfBoundsException, verified by the
        // @Test(expected=...) annotation.
        elements.deselect(invalidIndex);
    }
}