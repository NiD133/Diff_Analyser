package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

// Renamed class for clarity, removing "Test4" and the redundant "Test".
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
                                                          TransformerUtils.<K>nopTransformer(),
                                                          TransformerUtils.<V>nopTransformer());
    }

    /**
     * Tests that the value transformer is correctly applied when adding and querying elements.
     * This test verifies that after putting a String value, the map contains the transformed
     * Integer value, not the original String.
     */
    @Test
    @SuppressWarnings("unchecked") // Cast on transformer is necessary because it changes the value type from String to Integer.
    public void testValueTransformerOnPutAndQuery() {
        // Arrange
        final String[] stringInputs = {"1", "3", "5", "7", "2", "4", "6"};
        final Transformer<String, Integer> valueTransformer =
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // The map's value type must be a supertype of both input (String) and output (Integer),
        // so we use Object. The key is not transformed.
        final MultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
                                                          null, // no key transformer
                                                          (Transformer<? super Object, ? extends Object>) valueTransformer);
        assertTrue(transformedMap.isEmpty(), "Map should be empty before adding elements");

        // Act: Add string values to the map. They should be transformed into Integers.
        for (final String input : stringInputs) {
            // The key is the same as the value for simplicity.
            transformedMap.put(input, input);
        }

        // Assert
        assertEquals(stringInputs.length, transformedMap.size(), "Map size should match the number of elements added");

        for (final String input : stringInputs) {
            final String key = input;
            final String originalValue = input;
            final Integer transformedValue = Integer.valueOf(input);

            assertTrue(transformedMap.containsKey(key), "Map should contain the key '" + key + "'");
            assertTrue(transformedMap.containsValue(transformedValue), "Map should contain the transformed value " + transformedValue);
            assertFalse(transformedMap.containsValue(originalValue), "Map should not contain the original value '" + originalValue + "'");

            final Collection<Object> values = transformedMap.get(key);
            assertNotNull(values, "get(key) should not return null for an existing key");
            assertTrue(values.contains(transformedValue), "The collection for key '" + key + "' should contain the transformed value");
        }
    }

    /**
     * Tests that the remove() operation correctly handles transformed values.
     * It should return a collection containing the transformed value, not the original.
     */
    @Test
    @SuppressWarnings("unchecked") // Cast on transformer is necessary because it changes the value type from String to Integer.
    public void testValueTransformerOnRemove() {
        // Arrange
        final Transformer<String, Integer> valueTransformer =
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        final MultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
                                                          null, // no key transformer
                                                          (Transformer<? super Object, ? extends Object>) valueTransformer);

        transformedMap.put("key1", "1");
        transformedMap.put("key2", "2");

        final String keyToRemove = "key1";
        final Integer expectedRemovedValue = 1;

        // Act
        final Collection<Object> removedValues = transformedMap.remove(keyToRemove);

        // Assert
        assertNotNull(removedValues, "Removing an existing key should return the collection of values");
        assertTrue(removedValues.contains(expectedRemovedValue), "Removed collection should contain the transformed value");
        assertEquals(1, transformedMap.size(), "Map size should be decremented after removal");
        assertFalse(transformedMap.containsKey(keyToRemove), "Map should no longer contain the removed key");
    }
}