package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.SortedMap;

import org.junit.Test;

/**
 * Test suite for {@link TreeBag}.
 * Note: The original test class name 'TreeBag_ESTestTest3' was preserved,
 * but a more descriptive name like 'TreeBagTest' would be preferable.
 */
public class TreeBag_ESTestTest3 {

    /**
     * Tests that the internal map of a newly created TreeBag is empty.
     * This verifies the correct initialization of the bag's internal state.
     */
    @Test
    public void getMapOnNewBagShouldReturnEmptyMap() {
        // Arrange: Create a new, empty TreeBag.
        final TreeBag<Locale.Category> bag = new TreeBag<>();

        // Act: Retrieve the internal map.
        // The getMap() method is protected, so this test relies on package-private access.
        final SortedMap<Locale.Category, ?> internalMap = bag.getMap();

        // Assert: The internal map should be empty.
        assertTrue("A new TreeBag should have an empty internal map.", internalMap.isEmpty());
    }
}