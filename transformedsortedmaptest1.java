package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedMapTest} for exercising the {@link TransformedSortedMap}.
 */
// Renamed class for clarity and to follow standard naming conventions.
public class TransformedSortedMapTest<K, V> extends AbstractSortedMapTest<K, V> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    /**
     * Creates a new transformed map with no-op transformers for the abstract test cases.
     */
    @Override
    public SortedMap<K, V> makeObject() {
        // Use type inference with a type witness to avoid complex and unsafe casts.
        return TransformedSortedMap.transformingSortedMap(new TreeMap<>(),
            TransformerUtils.<K>nopTransformer(),
            TransformerUtils.<V>nopTransformer());
    }

    /**
     * Tests that the transformingSortedMap() factory method does not transform existing values,
     * but does transform new values that are added to the map.
     */
    @Test
    // This warning is unavoidable because the test uses concrete types (String, Integer)
    // but exists within a generic test class <K, V>.
    @SuppressWarnings("unchecked")
    public void testTransformingMapFactory_transformsNewValuesOnly() {
        // ARRANGE
        // A source map with String values that will be decorated.
        final SortedMap<K, V> sourceMap = new TreeMap<>();
        sourceMap.put((K) "A", (V) "1");
        sourceMap.put((K) "B", (V) "2");
        sourceMap.put((K) "C", (V) "3");

        // A transformer that converts String values to Integers.
        final Transformer<V, V> valueTransformer =
            (Transformer<V, V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // ACT
        // Decorate the source map. The factory method should not alter existing entries.
        final SortedMap<K, V> transformedMap =
            TransformedSortedMap.transformingSortedMap(sourceMap, null, valueTransformer);

        // ASSERT
        // Verify that the size is correct and existing values were NOT transformed.
        assertEquals(3, transformedMap.size(), "Size should be unchanged after decoration");
        assertEquals("1", transformedMap.get("A"), "Existing value for key 'A' should be untouched");
        assertEquals("2", transformedMap.get("B"), "Existing value for key 'B' should be untouched");
        assertEquals("3", transformedMap.get("C"), "Existing value for key 'C' should be untouched");

        // ACT
        // Add a new entry. The value is a String that the transformer can process.
        transformedMap.put((K) "D", (V) "4");

        // ASSERT
        // Verify that the new value was transformed from a String to an Integer.
        assertEquals(Integer.valueOf(4), transformedMap.get("D"), "Newly added value should be transformed");
    }
}