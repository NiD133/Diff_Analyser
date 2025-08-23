package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.SortedMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the TreeBag class.
 */
public class TreeBagTest {

    /**
     * Tests that the internal map returned by getMap() is correctly updated
     * after an element is added to the bag.
     */
    @Test
    public void getMap_shouldReturnNonEmptyMap_whenElementIsAdded() {
        // Arrange: Create an empty TreeBag.
        // The default constructor uses the natural ordering of elements.
        final TreeBag<String> bag = new TreeBag<>();
        final String element = "A";

        // Act: Add a single element to the bag.
        bag.add(element);

        // Assert: Verify that the internal map reflects the new state.
        // Note: getMap() is a protected method, so this is a white-box test
        // verifying the bag's internal state.
        final SortedMap<String, AbstractMapBag.MutableInteger> internalMap = bag.getMap();

        assertFalse("The internal map should not be empty after adding an element.", internalMap.isEmpty());
        assertEquals("The internal map should contain exactly one entry.", 1, internalMap.size());
    }
}