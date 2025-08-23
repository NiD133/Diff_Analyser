package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for TransformedSortedMap.
 * This particular test focuses on exception propagation from the decorated map.
 */
// The original class name is kept for context, though in a real scenario,
// it would be part of a larger TransformedSortedMapTest class.
public class TransformedSortedMap_ESTestTest19 {

    /**
     * Tests that an IllegalArgumentException from the underlying map's subMap method
     * is correctly propagated by the TransformedSortedMap.
     */
    @Test
    public void subMapShouldPropagateExceptionWhenKeyIsOutOfRangeOfUnderlyingMap() {
        // Arrange
        final TreeMap<Integer, String> baseMap = new TreeMap<>();
        final Integer boundaryKey = 835;

        // Create an empty sub-map view of the base map.
        // The valid key range for this view is [835, 835), which is empty.
        final SortedMap<Integer, String> underlyingSubMap = baseMap.subMap(boundaryKey, boundaryKey);

        // Decorate the sub-map. No transformers are needed as we are only testing exception propagation.
        final SortedMap<Integer, String> transformedMap =
            new TransformedSortedMap<>(underlyingSubMap, null, null);

        // Act & Assert
        try {
            // Attempt to create a new sub-map. This should fail because the fromKey (835)
            // is not within the allowed range of the underlyingSubMap.
            transformedMap.subMap(boundaryKey, boundaryKey);
            fail("Expected an IllegalArgumentException because the fromKey is out of the sub-map's range.");
        } catch (final IllegalArgumentException e) {
            // Verify that the expected exception is caught and has the correct message,
            // confirming it was propagated from the underlying TreeMap.
            assertEquals("fromKey out of range", e.getMessage());
        }
    }
}