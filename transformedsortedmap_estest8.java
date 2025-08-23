package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// The original test class name is kept for context.
public class TransformedSortedMap_ESTestTest8 extends TransformedSortedMap_ESTest_scaffolding {

    /**
     * Tests that the headMap() method returns a correctly functioning view of the map.
     * The view should contain all entries with keys strictly less than the 'toKey'.
     */
    @Test
    public void headMapShouldReturnViewWithElementsLessThanToKey() {
        // Arrange
        // Create a base sorted map and add an element.
        final SortedMap<Integer, Integer> baseMap = new TreeMap<>();
        baseMap.put(-17, -17);

        // Decorate the map with an identity transformer, which performs no transformation.
        // This ensures we are testing the view logic of TransformedSortedMap itself.
        final Transformer<Integer, Integer> identityTransformer = NOPTransformer.nopTransformer();
        final TransformedSortedMap<Integer, Integer> transformedMap =
                new TransformedSortedMap<>(baseMap, identityTransformer, identityTransformer);

        // Act
        // Get the headMap, which should contain all entries with keys less than 0.
        final SortedMap<Integer, Integer> headMap = transformedMap.headMap(0);

        // Assert
        // The resulting view should contain exactly one element: the entry for key -17.
        assertEquals("The headMap should contain one element", 1, headMap.size());
        assertTrue("The headMap should contain the key -17", headMap.containsKey(-17));
    }
}