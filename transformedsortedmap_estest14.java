package org.apache.commons.collections4.map;

import static org.junit.Assert.fail;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

/**
 * Tests for edge cases in {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that creating a TransformedSortedMap from another one that has a circular
     * reference to itself results in a StackOverflowError.
     *
     * This scenario can occur if the protected 'map' field is manually manipulated.
     * The factory method {@code transformingSortedMap} internally calls methods on the
     * map being decorated, leading to infinite recursion if the map decorates itself.
     */
    @Test
    public void testDecoratingMapWithCircularReferenceThrowsStackOverflowError() {
        // Arrange
        // 1. Create a base map to initialize the TransformedSortedMap.
        final SortedMap<String, String> baseMap = new TreeMap<>();
        final TransformedSortedMap<String, String> circularMap =
            TransformedSortedMap.transformingSortedMap(baseMap, null, null);

        // 2. Create a circular reference by making the decorated map point to the decorator itself.
        // This is a non-standard setup for testing purposes, requiring access to the protected 'map' field.
        circularMap.map = circularMap;

        // Act & Assert
        // 3. Attempting to decorate the map that now contains a circular reference.
        // This should cause infinite recursion and result in a StackOverflowError.
        try {
            TransformedSortedMap.transformingSortedMap(circularMap, (Transformer<String, String>) null, (Transformer<String, String>) null);
            fail("A StackOverflowError was expected but not thrown.");
        } catch (final StackOverflowError e) {
            // This is the expected outcome, so the test passes.
        }
    }
}