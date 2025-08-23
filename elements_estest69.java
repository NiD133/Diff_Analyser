package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for exception handling in the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling removeIf() with a null predicate throws a NullPointerException.
     * This is the expected behavior inherited from {@link java.util.ArrayList#removeIf(java.util.function.Predicate)}.
     */
    @Test(expected = NullPointerException.class)
    public void removeIfWithNullPredicateThrowsNullPointerException() {
        // Arrange: Create a non-empty Elements collection.
        Elements elements = new Elements(new Element("p"));

        // Act: Attempt to call removeIf with a null predicate.
        // The test will pass if this line throws a NullPointerException.
        elements.removeIf(null);
    }
}