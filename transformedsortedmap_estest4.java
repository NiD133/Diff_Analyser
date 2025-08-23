package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link TransformedSortedMap}.
 * This class focuses on testing the behavior of view methods like tailMap.
 */
public class TransformedSortedMap_ESTestTest4 { // Note: Class name kept from original for context.

    /**
     * Tests that the tailMap() method returns a view that correctly reflects
     * the state of the underlying map.
     */
    @Test
    public void tailMapShouldReturnCorrectViewForExistingKey() {
        // --- Arrange ---
        final SortedMap<Integer, Integer> baseMap = new TreeMap<>();
        final Integer key = 850;
        final Integer value = 850;

        // The value transformer is not exercised in this test, as we add data
        // directly to the base map. It is included to create the decorator.
        final Transformer<Integer, Integer> valueTransformer = ConstantTransformer.constantTransformer(value);

        // Create the decorated map. The key transformer is null (no transformation).
        final SortedMap<Integer, Integer> transformedMap =
            TransformedSortedMap.transformingSortedMap(baseMap, null, valueTransformer);

        // Add an element directly to the underlying map to test the view functionality.
        baseMap.put(key, value);
        baseMap.put(key - 1, value); // Add a key that should be excluded from the tailMap.

        // --- Act ---
        final SortedMap<Integer, Integer> tailMap = transformedMap.tailMap(key);

        // --- Assert ---
        assertNotNull("The tailMap view should not be null", tailMap);
        assertFalse("The tailMap should not be empty", tailMap.isEmpty());
        assertEquals("The tailMap should contain one element", 1, tailMap.size());
        assertTrue("The tailMap should contain the specified key", tailMap.containsKey(key));
        assertEquals("The first key in the tailMap should be the key used for the view", key, tailMap.firstKey());
    }
}