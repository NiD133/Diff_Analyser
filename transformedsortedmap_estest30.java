package org.apache.commons.collections4.map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

/**
 * Tests for {@link TransformedSortedMap}.
 * This class focuses on a specific test case that was part of a larger generated suite.
 */
public class TransformedSortedMap_ESTestTest30 { // Retaining original class name for context

    /**
     * Tests that the comparator() method correctly delegates to the underlying sorted map.
     */
    @Test
    public void testComparatorDelegatesToUnderlyingMapComparator() {
        // Arrange
        // 1. Create a mock comparator to be used by the underlying map.
        @SuppressWarnings("unchecked")
        final Comparator<Object> mockComparator = mock(Comparator.class);

        // 2. The underlying map is a TreeMap initialized with our mock comparator.
        final SortedMap<Object, Object> underlyingMap = new TreeMap<>(mockComparator);

        // 3. The transformers are not relevant for this test, so mocks are used as placeholders.
        @SuppressWarnings("unchecked")
        final Transformer<Object, Object> mockKeyTransformer = mock(Transformer.class);
        @SuppressWarnings("unchecked")
        final Transformer<Object, Object> mockValueTransformer = mock(Transformer.class);

        // 4. Create the TransformedSortedMap to be tested, decorating the underlying map.
        final SortedMap<Object, Object> transformedMap =
            TransformedSortedMap.transformedSortedMap(underlyingMap, mockKeyTransformer, mockValueTransformer);

        // Act
        // Retrieve the comparator from the transformed map.
        final Comparator<?> retrievedComparator = transformedMap.comparator();

        // Assert
        // The returned comparator should be the exact same instance as the one from the underlying map.
        assertSame("The comparator should be the one from the decorated map", mockComparator, retrievedComparator);
    }
}