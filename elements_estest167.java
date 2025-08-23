package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.List;

/**
 * Test cases for the {@link Elements} constructor.
 */
public class ElementsConstructorTest {

    /**
     * Verifies that the constructor throws a NullPointerException when initialized with a null list.
     * This is the expected behavior, as an Elements object cannot be created from a null collection.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenConstructedWithNullList() {
        // Attempting to create an Elements object from a null list should throw an exception.
        // The @Test(expected=...) annotation handles the verification.
        new Elements((List<Element>) null);
    }
}