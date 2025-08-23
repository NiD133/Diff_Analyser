package org.apache.commons.collections4.map;

import static org.junit.Assert.assertThrows;

import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;

/**
 * Tests for {@link TransformedSortedMap}.
 * This class focuses on a specific test case for improved clarity.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that tailMap() throws a ClassCastException if the provided key
     * is not compatible with the underlying map's ordering.
     * <p>
     * The TransformedSortedMap should propagate the exception from the decorated map.
     */
    @Test
    public void testTailMapWithIncompatibleKeyTypeThrowsException() {
        // Arrange: Create a TransformedSortedMap wrapping a TreeMap.
        // A standard TreeMap uses natural ordering, so keys must implement Comparable.
        final SortedMap<Object, Object> underlyingMap = new TreeMap<>();
        final SortedMap<Object, Object> transformedMap =
            TransformedSortedMap.transformingSortedMap(underlyingMap, null, null);

        // Use a key that does not implement Comparable, like the map itself.
        final Object nonComparableKey = new Object();

        // Act & Assert: Expect a ClassCastException when calling tailMap.
        // The exception originates from the underlying TreeMap, which cannot compare
        // the non-comparable key.
        assertThrows(ClassCastException.class, () -> {
            transformedMap.tailMap(nonComparableKey);
        });
    }
}