package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TransformedMultiValuedMap}.
 * <p>
 * This class maintains the generic structure for compatibility with the abstract
 * test suite but includes specific, concrete tests for factory methods.
 * </p>
 *
 * @param <K> the type of the keys in this map
 * @param <V> the type of the values in this map
 */
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    /** A transformer that converts a String representation of a number to an Integer. */
    private static final Transformer<String, Integer> STRING_TO_INTEGER =
        TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
            TransformerUtils.<K>nopTransformer(), TransformerUtils.<V>nopTransformer());
    }

    /**
     * Tests that the transformedMap() factory correctly transforms both the existing
     * elements of a map and any new elements added subsequently.
     */
    @Test
    public void transformedMapFactory_shouldTransformExistingAndNewElements() {
        // Arrange
        final MultiValuedMap<String, Object> originalMap = new ArrayListValuedHashMap<>();
        originalMap.put("A", "1");
        originalMap.put("B", "2");
        originalMap.put("C", "3");

        // Act: Decorate the map. The factory method should transform existing contents.
        // The map's value type is Object to hold both original Strings and transformed Integers.
        // A SuppressWarnings is used because the factory method's signature requires a less
        // specific transformer type than the one we are providing.
        @SuppressWarnings("unchecked")
        final MultiValuedMap<String, Object> transformedMap = TransformedMultiValuedMap.transformedMap(
            originalMap, null, (Transformer<? super Object, ? extends Object>) STRING_TO_INTEGER);

        // Assert: Verify that the initial elements were transformed.
        assertEquals(3, transformedMap.size(), "Map size should be unchanged after transformation");
        assertTrue(transformedMap.get("A").contains(1), "Value for 'A' should be transformed to Integer 1");
        assertTrue(transformedMap.get("B").contains(2), "Value for 'B' should be transformed to Integer 2");
        assertTrue(transformedMap.get("C").contains(3), "Value for 'C' should be transformed to Integer 3");

        // Act: Add a new element to the transformed map.
        transformedMap.put("D", "4");

        // Assert: Verify the new element was also transformed.
        assertEquals(4, transformedMap.size(), "Map size should increase after adding a new element");
        assertTrue(transformedMap.get("D").contains(4), "New value for 'D' should be transformed to Integer 4");
    }

    /**
     * Tests that the transformedMap() factory works correctly with an initially empty map,
     * transforming elements as they are added.
     */
    @Test
    public void transformedMapFactory_shouldTransformElementsAddedToEmptyMap() {
        // Arrange
        final MultiValuedMap<String, Object> originalMap = new ArrayListValuedHashMap<>();

        // Act: Decorate the empty map.
        @SuppressWarnings("unchecked")
        final MultiValuedMap<String, Object> transformedMap = TransformedMultiValuedMap.transformedMap(
            originalMap, null, (Transformer<? super Object, ? extends Object>) STRING_TO_INTEGER);

        // Assert: The decorated map should initially be empty.
        assertTrue(transformedMap.isEmpty(), "Transformed map of an empty map should be empty");

        // Act: Add a new element.
        transformedMap.put("A", "1");

        // Assert: Verify the new element was transformed.
        assertEquals(1, transformedMap.size());
        assertTrue(transformedMap.get("A").contains(1), "Value for 'A' should be transformed to Integer 1");
    }

}