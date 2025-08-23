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
        return TransformedSortedMap.transformingSortedMap(new TreeMap<>(), (Transformer<? super K, ? extends K>) TransformerUtils.nopTransformer(), (Transformer<? super V, ? extends V>) TransformerUtils.nopTransformer());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTransformedMap() {
        final Object[] els = { "1", "3", "5", "7", "2", "4", "6" };
        SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER, null);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put((K) els[i], (V) els[i]);
            assertEquals(i + 1, map.size());
            assertTrue(map.containsKey(Integer.valueOf((String) els[i])));
            final SortedMap<K, V> finalMap1 = map;
            final int finalI = i;
            assertThrows(ClassCastException.class, () -> finalMap1.containsKey(els[finalI]));
            assertTrue(map.containsValue(els[i]));
            assertEquals(els[i], map.get(Integer.valueOf((String) els[i])));
        }
        final SortedMap<K, V> finalMap = map;
        assertThrows(ClassCastException.class, () -> finalMap.remove(els[0]));
        assertEquals(els[0], map.remove(Integer.valueOf((String) els[0])));
        map = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put((K) els[i], (V) els[i]);
            assertEquals(i + 1, map.size());
            assertTrue(map.containsValue(Integer.valueOf((String) els[i])));
            assertFalse(map.containsValue(els[i]));
            assertTrue(map.containsKey(els[i]));
            assertEquals(Integer.valueOf((String) els[i]), map.get(els[i]));
        }
        assertEquals(Integer.valueOf((String) els[0]), map.remove(els[0]));
        final Set<Map.Entry<K, V>> entrySet = map.entrySet();
        final Map.Entry<K, V>[] array = entrySet.toArray(new Map.Entry[0]);
        array[0].setValue((V) "66");
        assertEquals(Integer.valueOf(66), array[0].getValue());
        assertEquals(Integer.valueOf(66), map.get(array[0].getKey()));
        final Map.Entry<K, V> entry = entrySet.iterator().next();
        entry.setValue((V) "88");
        assertEquals(Integer.valueOf(88), entry.getValue());
        assertEquals(Integer.valueOf(88), map.get(entry.getKey()));
    }
}
