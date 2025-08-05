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
 * Test suite for {@link TransformedSortedMap}.
 * This class tests the transformation functionality of the map.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
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

    @Override
    public SortedMap<K, V> makeObject() {
        return TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                TransformerUtils.nopTransformer(),
                TransformerUtils.nopTransformer()
        );
    }

    @Test
    void testTransformingSortedMapWithNoKeyTransformer() {
        SortedMap<K, V> baseMap = new TreeMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");

        SortedMap<K, V> transformedMap = TransformedSortedMap.transformingSortedMap(
                baseMap,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, transformedMap.size());
        assertEquals("1", transformedMap.get("A"));
        assertEquals("2", transformedMap.get("B"));
        assertEquals("3", transformedMap.get("C"));

        transformedMap.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    @Test
    void testTransformedSortedMapWithNoKeyTransformer() {
        SortedMap<K, V> baseMap = new TreeMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");

        SortedMap<K, V> transformedMap = TransformedSortedMap.transformedSortedMap(
                baseMap,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, transformedMap.size());
        assertEquals(Integer.valueOf(1), transformedMap.get("A"));
        assertEquals(Integer.valueOf(2), transformedMap.get("B"));
        assertEquals(Integer.valueOf(3), transformedMap.get("C"));

        transformedMap.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    @Test
    void testKeyTransformation() {
        Object[] elements = {"1", "3", "5", "7", "2", "4", "6"};

        SortedMap<K, V> mapWithKeyTransformation = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER,
                null
        );

        assertEquals(0, mapWithKeyTransformation.size());

        for (int i = 0; i < elements.length; i++) {
            mapWithKeyTransformation.put((K) elements[i], (V) elements[i]);
            assertEquals(i + 1, mapWithKeyTransformation.size());
            assertTrue(mapWithKeyTransformation.containsKey(Integer.valueOf((String) elements[i])));
            assertThrows(ClassCastException.class, () -> mapWithKeyTransformation.containsKey(elements[i]));
            assertTrue(mapWithKeyTransformation.containsValue(elements[i]));
            assertEquals(elements[i], mapWithKeyTransformation.get(Integer.valueOf((String) elements[i])));
        }

        assertThrows(ClassCastException.class, () -> mapWithKeyTransformation.remove(elements[0]));
        assertEquals(elements[0], mapWithKeyTransformation.remove(Integer.valueOf((String) elements[0])));

        SortedMap<K, V> mapWithValueTransformation = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(0, mapWithValueTransformation.size());

        for (int i = 0; i < elements.length; i++) {
            mapWithValueTransformation.put((K) elements[i], (V) elements[i]);
            assertEquals(i + 1, mapWithValueTransformation.size());
            assertTrue(mapWithValueTransformation.containsValue(Integer.valueOf((String) elements[i])));
            assertFalse(mapWithValueTransformation.containsValue(elements[i]));
            assertTrue(mapWithValueTransformation.containsKey(elements[i]));
            assertEquals(Integer.valueOf((String) elements[i]), mapWithValueTransformation.get(elements[i]));
        }

        assertEquals(Integer.valueOf((String) elements[0]), mapWithValueTransformation.remove(elements[0]));

        Set<Map.Entry<K, V>> entrySet = mapWithValueTransformation.entrySet();
        Map.Entry<K, V>[] entryArray = entrySet.toArray(new Map.Entry[0]);
        entryArray[0].setValue((V) "66");
        assertEquals(Integer.valueOf(66), entryArray[0].getValue());
        assertEquals(Integer.valueOf(66), mapWithValueTransformation.get(entryArray[0].getKey()));

        Map.Entry<K, V> entry = entrySet.iterator().next();
        entry.setValue((V) "88");
        assertEquals(Integer.valueOf(88), entry.getValue());
        assertEquals(Integer.valueOf(88), mapWithValueTransformation.get(entry.getKey()));
    }
}