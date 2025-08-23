package org.apache.commons.collections4.map;

import static org.junit.Assert.assertThrows;

import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;

/**
 * Contains tests for {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that a ClassCastException from the underlying map is propagated
     * when headMap() is called with a key that is not comparable.
     */
    @Test
    public void testHeadMapWithNonComparableKeyThrowsClassCastException() {
        // Arrange
        // A TreeMap with natural ordering requires its keys to be Comparable.
        final SortedMap<Object, Object> underlyingMap = new TreeMap<>();
        final SortedMap<Object, Object> transformedMap =
            TransformedSortedMap.transformingSortedMap(underlyingMap, null, null);

        // An object that does not implement Comparable, like another map.
        final Object nonComparableKey = new TreeMap<>();

        // Act & Assert
        // The headMap() call is delegated to the underlying TreeMap, which will
        // throw a ClassCastException because the key is not Comparable.
        assertThrows(ClassCastException.class, () -> {
            transformedMap.headMap(nonComparableKey);
        });
    }
}