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

public class TransformedSortedMapTestTest1<K, V> extends AbstractSortedMapTest<K, V> {

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
    void testFactory_Decorate() {
        final SortedMap<K, V> base = new TreeMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");
        final SortedMap<K, V> trans = TransformedSortedMap.transformingSortedMap(base, null, (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(3, trans.size());
        assertEquals("1", trans.get("A"));
        assertEquals("2", trans.get("B"));
        assertEquals("3", trans.get("C"));
        trans.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), trans.get("D"));
    }
}
