package org.apache.commons.collections4.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Contains tests for the {@link TransformedSortedMap#getSortedMap()} method.
 */
public class TransformedSortedMap_ESTestTest9 {

    /**
     * Tests that getSortedMap() returns the map instance it directly decorates,
     * even when that decorated map is another TransformedSortedMap.
     */
    @Test
    public void testGetSortedMapReturnsImmediateDecoratedMap() {
        // Arrange
        final SortedMap<String, Integer> originalMap = new TreeMap<>();
        final Transformer<Object, Object> nopTransformer = NOPTransformer.nopTransformer();

        // Create the first layer of decoration over the original map.
        final TransformedSortedMap<String, Integer> firstDecorator =
                new TransformedSortedMap<>(originalMap, nopTransformer, nopTransformer);

        // Create a second layer of decoration over the first decorator.
        final TransformedSortedMap<String, Integer> secondDecorator =
                TransformedSortedMap.transformingSortedMap(firstDecorator, nopTransformer, nopTransformer);

        // Act
        // Retrieve the map that the second decorator is wrapping.
        final SortedMap<String, Integer> retrievedMap = secondDecorator.getSortedMap();

        // Assert
        // The method should return the map it directly wraps, which is the first decorator.
        assertSame("getSortedMap should return the immediate decorated map instance", firstDecorator, retrievedMap);
        assertEquals("The retrieved map should be empty, same as the original", 0, retrievedMap.size());
    }
}