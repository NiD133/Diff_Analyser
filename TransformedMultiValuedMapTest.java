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

/**
 * Unit tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                TransformerUtils.<K>nopTransformer(),
                TransformerUtils.<V>nopTransformer()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDecorateWithTransformation() {
        MultiValuedMap<K, V> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");

        MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformingMap(
                baseMap,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, transformedMap.size());
        assertTrue(transformedMap.get((K) "A").contains("1"));
        assertTrue(transformedMap.get((K) "B").contains("2"));
        assertTrue(transformedMap.get((K) "C").contains("3"));

        transformedMap.put((K) "D", (V) "4");
        assertTrue(transformedMap.get((K) "D").contains(Integer.valueOf(4)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTransformExistingMap() {
        MultiValuedMap<K, V> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");

        MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformedMap(
                baseMap,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(3, transformedMap.size());
        assertTrue(transformedMap.get((K) "A").contains(Integer.valueOf(1)));
        assertTrue(transformedMap.get((K) "B").contains(Integer.valueOf(2)));
        assertTrue(transformedMap.get((K) "C").contains(Integer.valueOf(3)));

        transformedMap.put((K) "D", (V) "4");
        assertTrue(transformedMap.get((K) "D").contains(Integer.valueOf(4)));

        // Test with an initially empty map
        MultiValuedMap<K, V> emptyBaseMap = new ArrayListValuedHashMap<>();
        MultiValuedMap<K, V> emptyTransformedMap = TransformedMultiValuedMap.transformedMap(
                emptyBaseMap,
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(0, emptyTransformedMap.size());
        emptyTransformedMap.put((K) "D", (V) "4");
        assertEquals(1, emptyTransformedMap.size());
        assertTrue(emptyTransformedMap.get((K) "D").contains(Integer.valueOf(4)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testKeyTransformation() {
        Object[] elements = { "1", "3", "5", "7", "2", "4", "6" };

        MultiValuedMap<K, V> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER,
                null
        );

        assertEquals(0, map.size());
        for (int i = 0; i < elements.length; i++) {
            map.put((K) elements[i], (V) elements[i]);
            assertEquals(i + 1, map.size());
            assertTrue(map.containsKey(Integer.valueOf((String) elements[i])));
            assertFalse(map.containsKey(elements[i]));
            assertTrue(map.containsValue(elements[i]));
            assertTrue(map.get((K) Integer.valueOf((String) elements[i])).contains(elements[i]));
        }

        Collection<V> removedCollection = map.remove(elements[0]);
        assertNotNull(removedCollection);
        assertEquals(0, removedCollection.size());
        assertTrue(map.remove(Integer.valueOf((String) elements[0])).contains(elements[0]));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testValueTransformation() {
        Object[] elements = { "1", "3", "5", "7", "2", "4", "6" };

        MultiValuedMap<K, V> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );

        assertEquals(0, map.size());
        for (int i = 0; i < elements.length; i++) {
            map.put((K) elements[i], (V) elements[i]);
            assertEquals(i + 1, map.size());
            assertTrue(map.containsValue(Integer.valueOf((String) elements[i])));
            assertFalse(map.containsValue(elements[i]));
            assertTrue(map.containsKey(elements[i]));
            assertTrue(map.get((K) elements[i]).contains(Integer.valueOf((String) elements[i])));
        }

        assertTrue(map.remove(elements[0]).contains(Integer.valueOf((String) elements[0])));
    }
}