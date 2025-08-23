package org.jsoup.select;

import org.junit.Test;

/**
 * Tests for the {@link Elements} class, focusing on edge cases and exception handling.
 */
public class ElementsTest {

    /**
     * Verifies that calling the remove() method with a negative index
     * on an Elements collection correctly throws an ArrayIndexOutOfBoundsException.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void removeWithNegativeIndexThrowsException() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();
        int invalidIndex = -1;

        // Act: Attempt to remove an element using the invalid negative index.
        // The test expects this line to throw the exception.
        elements.remove(invalidIndex);

        // Assert: The test passes if an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}