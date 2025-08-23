package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for the constructor of the {@link Elements} class.
 */
public class ElementsConstructorTest {

    /**
     * Verifies that the varargs constructor throws a NullPointerException
     * when a null array is passed as an argument. The underlying implementation
     * relies on Arrays.asList(), which does not permit null array references.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullArray() {
        // Attempt to create an Elements object with a null varargs array,
        // which is expected to throw a NullPointerException.
        new Elements((Element[]) null);
    }
}