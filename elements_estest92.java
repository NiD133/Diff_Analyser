package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Collection;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that the constructor throws a NullPointerException when initialized with a null collection.
     * This ensures the constructor correctly handles invalid input by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullCollection() {
        // Attempting to create an Elements object from a null collection is an invalid state.
        // The underlying ArrayList constructor is expected to throw this exception.
        new Elements((Collection<Element>) null);
    }
}