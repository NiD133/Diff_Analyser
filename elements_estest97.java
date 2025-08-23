package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that the Elements constructor correctly creates an empty Elements object
     * when initialized with an empty source collection.
     */
    @Test
    public void constructorWithCollectionCreatesEmptyElementsFromEmptySource() {
        // Arrange: Create an empty source collection of elements.
        Collection<Element> emptySourceCollection = new ArrayList<>();

        // Act: Create a new Elements object using the empty collection.
        Elements elements = new Elements(emptySourceCollection);

        // Assert: The newly created Elements object should be empty.
        assertTrue("The Elements object initialized from an empty collection should be empty.", elements.isEmpty());
    }
}