package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedMapTest} for exercising the {@link TransformedSortedMap}.
 *
 * This class is named 'Test3' to distinguish it from other tests in the suite.
 * It focuses on specific transformation scenarios not covered by the abstract tests.
 */
public class TransformedSortedMapTestTest3<K, V> extends AbstractSortedMapTest<K, V> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedMap<K, V> makeObject() {
        return TransformedSortedMap.transformingSortedMap(new TreeMap<>(),
            (Transformer<? super K, ? extends K>) TransformerUtils.nopTransformer(),
            (Transformer<? super V, ? extends V>) TransformerUtils.nopTransformer());
    }

    /**
     * Tests that `put` transforms the key, and that subsequent read/remove operations
     * must use the transformed key.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testMapWithKeyTransformer() {
        // --- Arrange ---
        final String[] stringNumbers = {"1", "3", "5", "7", "2", "4", "6"};
        final Transformer<String, Integer> keyTransformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // The map transforms String keys into Integer keys. Values are not transformed.
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            (Transformer<? super K, ? extends K>) keyTransformer,
            null);
        assertEquals(0, map.size(), "Map should be empty initially");

        // --- Act & Assert: put and access ---
        for (int i = 0; i < stringNumbers.length; i++) {
            final String currentString = stringNumbers[i];
            final Integer transformedKey = Integer.valueOf(currentString);

            map.put((K) currentString, (V) currentString);

            assertEquals(i + 1, map.size());
            assertTrue(map.containsKey(transformedKey), "Map should contain the TRANSFORMED key");
            assertTrue(map.containsValue(currentString), "Map should contain the original value as it's not transformed");
            assertEquals(currentString, map.get(transformedKey), "get() must use the TRANSFORMED key");

            // Accessing with the ORIGINAL key type should fail as the underlying TreeMap expects Integers.
            final SortedMap<K, V> finalMap = map;
            assertThrows(ClassCastException.class, () -> finalMap.containsKey(currentString),
                "Accessing with original key type should throw ClassCastException");
        }

        // --- Act & Assert: remove ---
        final String stringToRemove = stringNumbers[0];
        final Integer transformedKeyToRemove = Integer.valueOf(stringToRemove);

        // Removing with the ORIGINAL key type should fail.
        final SortedMap<K, V> finalMap = map;
        assertThrows(ClassCastException.class, () -> finalMap.remove(stringToRemove),
            "Removing with original key type should throw ClassCastException");

        // Removing with the TRANSFORMED key should succeed.
        assertEquals(stringToRemove, map.remove(transformedKeyToRemove), "remove() must use the TRANSFORMED key");
        assertFalse(map.containsKey(transformedKeyToRemove));
    }

    /**
     * Tests that `put` transforms the value, and that `get` and `remove` operations
     * return the transformed value.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testMapWithValueTransformer() {
        // --- Arrange ---
        final String[] stringNumbers = {"1", "3", "5", "7", "2", "4", "6"};
        final Transformer<String, Integer> valueTransformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // The map transforms String values into Integer values. Keys are not transformed.
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) valueTransformer);
        assertEquals(0, map.size(), "Map should be empty initially");

        // --- Act & Assert: put and access ---
        for (int i = 0; i < stringNumbers.length; i++) {
            final String currentString = stringNumbers[i];
            final Integer transformedValue = Integer.valueOf(currentString);

            map.put((K) currentString, (V) currentString);

            assertEquals(i + 1, map.size());
            assertTrue(map.containsKey(currentString), "Map should contain the original key");
            assertTrue(map.containsValue(transformedValue), "Map should contain the TRANSFORMED value");
            assertFalse(map.containsValue(currentString), "Map should not contain the original value");
            assertEquals(transformedValue, map.get(currentString), "get() should return the TRANSFORMED value");
        }

        // --- Act & Assert: remove ---
        final String keyToRemove = stringNumbers[0];
        final Integer expectedRemovedValue = Integer.valueOf(keyToRemove);

        // Removing with the original key should succeed and return the TRANSFORMED value.
        assertEquals(expectedRemovedValue, map.remove(keyToRemove), "remove() should return the TRANSFORMED value");
        assertFalse(map.containsKey(keyToRemove));
    }

    /**
     * Tests that calling setValue() on an entry from the entry set correctly
     * transforms the new value before updating the map.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetSetValueOnValueTransformedMap() {
        // --- Arrange ---
        final Transformer<String, Integer> valueTransformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) valueTransformer);
        map.put((K) "A", (V) "1");
        map.put((K) "B", (V) "2");

        // --- Act & Assert: Update value via Entry from iterator ---
        final Map.Entry<K, V> entryFromIterator = map.entrySet().iterator().next(); // Entry for key "A"
        entryFromIterator.setValue((V) "100");

        assertEquals(Integer.valueOf(100), entryFromIterator.getValue(), "Entry value from iterator should be transformed");
        assertEquals(Integer.valueOf(100), map.get("A"), "Map value should be updated via iterator's entry");

        // --- Act & Assert: Update value via Entry from toArray() ---
        // Note: TreeMap entrySet toArray is ordered by key.
        final Map.Entry<K, V>[] entryArray = map.entrySet().toArray(new Map.Entry[0]);
        final Map.Entry<K, V> entryFromArray = entryArray[1]; // Entry for key "B"
        entryFromArray.setValue((V) "200");

        assertEquals(Integer.valueOf(200), entryFromArray.getValue(), "Entry value from array should be transformed");
        assertEquals(Integer.valueOf(200), map.get("B"), "Map value should be updated via array's entry");
    }
}