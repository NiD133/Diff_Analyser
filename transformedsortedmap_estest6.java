package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains tests for {@link TransformedSortedMap}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that lastKey() returns null when the `transformedSortedMap` factory
     * transforms the only existing key in the decorated map into null.
     */
    @Test
    public void testLastKeyReturnsNullWhenExistingKeyIsTransformedToNull() {
        // --- Arrange ---

        // A standard TreeMap throws a NullPointerException if a null key is inserted.
        // To test the transformation to a null key, we must provide a comparator
        // that can handle nulls. A mock comparator serves this purpose well.
        @SuppressWarnings("unchecked")
        final Comparator<Object> nullSafeComparator = mock(Comparator.class);
        when(nullSafeComparator.compare(any(), any())).thenReturn(1);

        // Create the underlying map with our null-safe comparator and add an initial entry.
        final SortedMap<Object, Integer> underlyingMap = new TreeMap<>(nullSafeComparator);
        underlyingMap.put("initialKey", 100);

        // This key transformer will convert any key it receives into null.
        final Transformer<Object, Object> nullKeyTransformer = key -> null;

        // --- Act ---

        // The `transformedSortedMap` factory method processes existing entries.
        // It will take the entry ("initialKey", 100), transform the key to `null`
        // using the transformer, and re-insert it into the underlying map.
        final SortedMap<Object, Integer> transformedMap =
                TransformedSortedMap.transformedSortedMap(underlyingMap, nullKeyTransformer, null);

        // Retrieve the last key from the map after the transformation.
        final Object lastKey = transformedMap.lastKey();

        // --- Assert ---

        // The original key "initialKey" was transformed to null, so lastKey() should return null.
        assertNull("The lastKey should be null after the original key was transformed to null", lastKey);

        // Add supporting assertions to verify the map's state.
        assertEquals("Map should contain exactly one entry", 1, transformedMap.size());
        assertTrue("Map should contain the transformed null key", transformedMap.containsKey(null));
    }
}