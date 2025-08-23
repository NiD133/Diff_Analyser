package org.jsoup.select;

import org.junit.Test;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the {@link Elements#eq(int)} method with a negative index
     * throws an {@link ArrayIndexOutOfBoundsException}.
     * <p>
     * The `eq(int)` method is designed to retrieve an element by its index. A negative
     * index is an invalid argument for list access and is expected to fail fast
     * by throwing an exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void eqShouldThrowExceptionForNegativeIndex() {
        // Arrange: Create an empty list of elements. The behavior for a negative index
        // is consistent regardless of whether the list is empty or not.
        Elements elements = new Elements();

        // Act & Assert: Calling eq() with any negative index should throw.
        // The @Test(expected=...) annotation handles the assertion.
        elements.eq(-1);
    }
}